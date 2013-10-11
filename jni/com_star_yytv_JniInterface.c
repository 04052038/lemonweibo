#include <jni.h>
#include <stdio.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <time.h>
#include <errno.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>

typedef struct {
	unsigned char* elem;
	int length;
	int listsize;
} Sqlist;

//#include <android/log.h>
//#define LOG_TAG "logfromc"
//#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

void initSqlist(Sqlist *L)
{
	L->elem = (unsigned char*)malloc(4096);
	memset(L->elem, 0, 4096);
	if (L->elem == NULL) exit(0);
	L->length = 0;
	L->listsize = 4096;
}

void appendSqlist(Sqlist *L, unsigned char item)
{
	unsigned char* base;
	if (L->length >= L->listsize){
		base = (unsigned char*)realloc(L->elem, L->listsize+4096);
		if (base == NULL) exit(0);
		L->elem = base;
		int i = 0;
		for (i=L->listsize+1; i<=L->listsize+4096; i++){
			L->elem[i] = 0;
		}
		L->listsize += 4096;
	}

	L->elem[L->length] = item;
	L->length ++;
}

void appendSqlistBlock(Sqlist *L, unsigned char *block, int t)
{
	int len = 0;
	if (t == 0)
		len = strlen(block);
	else
		len = t;
	unsigned char* base;
	if (L->length+len >= L->listsize){
		base = (unsigned char*)realloc(L->elem, L->listsize+4096);
		if (base == NULL) exit(0);
		L->elem = base;
		int i = 0;
		for (i=L->listsize+1; i<=L->listsize+4096; i++){
			L->elem[i] = 0;
		}
		L->listsize += 4096;
	}

	int i = 0;
	for (i=0; i<len; i++){
		L->elem[L->length+i] = block[i];
	}
	L->length += len;
}

void clearSqlist(Sqlist *L)
{
	if (L->elem != NULL)
		free(L->elem);
}

/* 128位密钥解码  */
void decrypt_block(unsigned char cipher[16], unsigned char plain[16]){
	//16字节128位密钥，每次版本发布时更新
	unsigned char keys[16] = {
		140, 161, 137,  13, 191, 230,  66, 104,
		 65, 153,  45,  15, 176,  84, 187,  22 };
	memset(plain, 0, 16);
	int i = 0;
	int len = strlen(cipher);
	for (i = 0; i < len; i++){
		plain[i] = cipher[i] ^ keys[i];
	}
}

/* HTTP response解码 */
void decrypt_body(const unsigned char *in, unsigned char *out){
	int len = strlen(in);
	int cnt = 0;
	int remainder = len % 16;

	if (remainder == 0)
		cnt = len/16;
	else {
		cnt = len/16 + 1;
	}

	int pt = 0;
	memset(out, 0, len);
	unsigned char *p = out;
	int i = 0;
	for (i=0; i<cnt; i++){
		//取16个元素
		pt += 16;
		unsigned char cipher[16];
		unsigned char plain[16];
		memset(cipher, 0, 16);

		int j = 0;
		if (pt > len){
			for (j=0; j<remainder; j++)
				cipher[j] = in[i*16+j];
			decrypt_block(cipher, plain);
			//strcat(out, plain);
			memcpy(p, plain, remainder);
			p = p + remainder;
		} else {
			for (j=0; j<16; j++)
				cipher[j] = in[i*16+j];
			decrypt_block(cipher, plain);
			//strcat(out, plain);
			memcpy(p, plain, 16);
			p = p + 16;
		}
	}
}

void export(JNIEnv *env, jbyteArray *arrRet, const unsigned char *output){
	int len = strlen(output);
	*arrRet = (*env)->NewByteArray(env, len);
	jbyte *arrRetPt = (*env)->GetByteArrayElements(env, *arrRet, 0);
	memcpy(arrRetPt, output, len);
	(*env)->ReleaseByteArrayElements(env, *arrRet, arrRetPt, 0);
}

/* Native interface, it will be call in java code */
JNIEXPORT jbyteArray JNICALL Java_com_star_yytv_JniInterface_getServerResponse2
	(JNIEnv *env, jobject obj, jint type, jint baseUrlType, jbyteArray myUrl, jbyteArray myParaValue)
{
	int sockfd, ret, i, h;
	struct sockaddr_in servaddr;
	unsigned char reqstr[128];
	Sqlist reqlist1, reqlist2, reqpost;
	Sqlist replist;
	unsigned char repbuf[1];
	unsigned char *decPt = NULL;
	socklen_t len;
	fd_set t_set1;
	struct timeval tv;
	struct hostent *host;
	jbyteArray retStr;

	if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0 ) {
		char* tmpStr = "EXCEPTION: socket error.";
		export(env, &retStr, tmpStr);
		return retStr;
	};

	/*取得主机IP地址*/
	host = gethostbyname("www.yaoyaotv.com");
	if(host == NULL) {
		char* tmpStr = "EXCEPTION: get host ip error.";
		export(env, &retStr, tmpStr);
		return retStr;
	}

	bzero(&servaddr, sizeof(servaddr));
	servaddr.sin_family = AF_INET;
	servaddr.sin_port = htons(80);
	//servaddr.sin_addr.s_addr = inet_addr("222.73.44.49");
	servaddr.sin_addr = *((struct in_addr *)host->h_addr);

	if (connect(sockfd, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0){
		char* tmpStr = "EXCEPTION: connect server error.";
		export(env, &retStr, tmpStr);
		return retStr;
	}

	initSqlist(&reqlist1);
	initSqlist(&reqlist2);

	//0: GET method
	if (type == 0){
		//int tt = time((time_t*)NULL);
		//memset(reqstr, 0, 128);
		//sprintf(reqstr, "&t=%d", tt);

		//GET数据,myUrl已经包含参数?a=12&b=13
		unsigned char *myurl = NULL;
		jsize arrLen = (*env)->GetArrayLength(env, myUrl);
		jbyte *arrPt = (*env)->GetByteArrayElements(env, myUrl, 0);
		if (arrLen > 0){
			myurl = (unsigned char*)malloc(arrLen+1);
			memcpy(myurl, arrPt, arrLen);
			myurl[arrLen] = 0;
		}
		(*env)->ReleaseByteArrayElements(env, myUrl, arrPt, 0);

		appendSqlistBlock(&reqlist2, myurl, strlen(myurl));

		if (myurl != NULL)
			free(myurl);

		//0: keping's server base address
		if (baseUrlType == 0){
			appendSqlistBlock(&reqlist1, "GET /yytv/", 0);
			//such as www.yaoyaotv.com/yytv/[xxx/jquery?a=12&b=13]
			appendSqlistBlock(&reqlist1, reqlist2.elem, reqlist2.length);
			appendSqlistBlock(&reqlist1, " HTTP/1.1\n", 0);
		//1: baoyu's server base address
		} else if (baseUrlType == 1) {
			appendSqlistBlock(&reqlist1, "GET /beta/app/", 0);
			//such as www.yaoyaotv.com/beta/app/[queryXXX.php?a=12&b=13]
			appendSqlistBlock(&reqlist1, reqlist2.elem, reqlist2.length);
			appendSqlistBlock(&reqlist1, " HTTP/1.1\n", 0);
		}
		appendSqlistBlock(&reqlist1, "Host: www.yaoyaotv.com\n", 0);
		appendSqlistBlock(&reqlist1, "\r\n\r\n", 0);
	//1: POST method
	} else if (type == 1){
		unsigned char *myurl = NULL;
		jsize arrLen = (*env)->GetArrayLength(env, myUrl);
		jbyte *arrPt = (*env)->GetByteArrayElements(env, myUrl, 0);
		if (arrLen > 0){
			myurl = (unsigned char*)malloc(arrLen+1);
			memcpy(myurl, arrPt, arrLen);
			myurl[arrLen] = 0;
		}
		(*env)->ReleaseByteArrayElements(env, myUrl, arrPt, 0);

		appendSqlistBlock(&reqlist2, myurl, strlen(myurl));

		if (myurl != NULL)
			free(myurl);

		//0: keping's server base address
		if (baseUrlType == 0){
			appendSqlistBlock(&reqlist1, "POST /yytv/", 0);
			//such as www.yaoyaotv.com/yytv/[jadd]
			appendSqlistBlock(&reqlist1, reqlist2.elem, reqlist2.length);
			appendSqlistBlock(&reqlist1, " HTTP/1.1\n", 0);
		//1: baoyu's server base address
		} else if (baseUrlType == 1) {
			appendSqlistBlock(&reqlist1, "POST /beta/app/", 0);
			//such as www.yaoyaotv.com/beta/app/[addXXX.php]
			appendSqlistBlock(&reqlist1, reqlist2.elem, reqlist2.length);
			appendSqlistBlock(&reqlist1, " HTTP/1.1\n", 0);
		}
		appendSqlistBlock(&reqlist1, "Host: www.yaoyaotv.com\n", 0);
		appendSqlistBlock(&reqlist1, "Content-Type: application/x-www-form-urlencoded\n", 0);

		//POST数据,a=12&b=13
		unsigned char *para = NULL;
		arrLen = (*env)->GetArrayLength(env, myParaValue);
		arrPt = (*env)->GetByteArrayElements(env, myParaValue, 0);
		if (arrLen > 0){
			para = (unsigned char*)malloc(arrLen+1);
			memcpy(para, arrPt, arrLen);
			para[arrLen] = 0;
		}
		(*env)->ReleaseByteArrayElements(env, myParaValue, arrPt, 0);

		initSqlist(&reqpost);
		appendSqlistBlock(&reqpost, para, strlen(para));

		if (para != NULL)
			free(para);

		memset(reqstr, 0, 128);
		sprintf(reqstr, "%d", reqpost.length);

		appendSqlistBlock(&reqlist1, "Content-Length: ", 0);
		appendSqlistBlock(&reqlist1, reqstr, strlen(reqstr));
		appendSqlistBlock(&reqlist1, "\n\n", 0);
		appendSqlistBlock(&reqlist1, reqpost.elem, reqpost.length);
		appendSqlistBlock(&reqlist1, "\r\n\r\n", 0);
	}

	ret = write(sockfd, reqlist1.elem, reqlist1.length);
	if (ret < 0) {
		close(sockfd);
		char* tmpStr = "EXCEPTION: write error.";
		export(env, &retStr, tmpStr);

		clearSqlist(&reqlist1);
		clearSqlist(&reqlist2);
		if (type == 1)
			clearSqlist(&reqpost);
		return retStr;
	}

    FD_ZERO(&t_set1);
	FD_SET(sockfd, &t_set1);

	//select wait for max 5 seconds， then return h.
	tv.tv_sec = 5;
	tv.tv_usec = 0;
	h = 0;
	h = select(sockfd +1, &t_set1, NULL, NULL, &tv);

	//h=0, means timeout.
	if (h == 0) {
	  	close(sockfd);
	   	char* tmpStr = "EXCEPTION: select return 0, timeout.";
	   	export(env, &retStr, tmpStr);
	   	clearSqlist(&reqlist1);
	   	clearSqlist(&reqlist2);
	   	if (type == 1)
	   		clearSqlist(&reqpost);
	   	return retStr;
	}
	//h<0, means select error.
	if (h < 0) {
	   	close(sockfd);
	    char* tmpStr = "EXCEPTION: select return -1, error.";
	    export(env, &retStr, tmpStr);
	    clearSqlist(&reqlist1);
	    clearSqlist(&reqlist2);
	    if (type == 1)
	    	clearSqlist(&reqpost);
	    return retStr;
	}
    //h>0, means ready for data receive.
	if (h > 0){
	  	memset(repbuf, 0, 1);
	  	initSqlist(&replist);

	  	int headcounter = 0;
	  	i = read(sockfd, repbuf, 1);
	  	while (i == 1){
	  		//4个字节的\r\n\r\n表示http header结束
	  		//注意未考虑chunked
	  		if (headcounter < 4){
	  			if (repbuf[0] == '\r' || repbuf[0] == '\n')
	  				headcounter++;
	  			else
	  				headcounter = 0;
 	  		} else {
 	  			appendSqlist(&replist, repbuf[0]);
	  		}
	  		i = read(sockfd, repbuf, 1);
	  	}

	  	close(sockfd);
	  	if (replist.length != 0){
	  		//LOGI(repstr);
	  		decPt = (unsigned char*)malloc(replist.length+1);
	  		//解密body字符串
	  		decrypt_body(replist.elem, decPt);
	  		decPt[replist.length] = 0;
	  		export(env, &retStr, decPt);
	  	} else {
	  		char* tmpStr = "EXCEPTION: response message is null.";
	  		export(env, &retStr, tmpStr);
	  	}
	  	//memory clear
	  	clearSqlist(&reqlist1);
	  	clearSqlist(&reqlist2);
	  	if (type == 1)
	  		clearSqlist(&reqpost);
	  	clearSqlist(&replist);

	  	if (decPt != NULL)
	  		free(decPt);

		return retStr;
    }
}

JNIEXPORT jbyteArray JNICALL Java_com_star_yytv_JniInterface_test
  (JNIEnv *env, jobject obj, jbyteArray myUrl)
{
	jbyteArray arrRet = NULL;
	unsigned char *myurl = NULL;
	jsize arrLen = (*env)->GetArrayLength(env, myUrl);
	jbyte *arrPt = (*env)->GetByteArrayElements(env, myUrl, 0);
	if (arrLen > 0){
		myurl = (unsigned char*)malloc(arrLen+1);
		memcpy(myurl, arrPt, arrLen);
		myurl[arrLen] = 0;
	}
	(*env)->ReleaseByteArrayElements(env, myUrl, arrPt, 0);

	//直接传回中文字符串测试
	unsigned char output[4096];
	int outputLen = strlen(myurl);
	arrRet = (*env)->NewByteArray(env, outputLen);
	jbyte *arrRetPt = (*env)->GetByteArrayElements(env, arrRet, 0);
	memcpy(arrRetPt, myurl, outputLen);
	(*env)->ReleaseByteArrayElements(env, arrRet, arrRetPt, 0);

	if (myurl != NULL)
		free(myurl);

	return arrRet;
	//encode test
	/*
	int outputSize = 4095;
	int inputSize = strlen(myurl);
	int outputLen = UrlEncode(myurl, inputSize, output, outputSize);
	if (outputLen != 0){
		arrRet = (*env)->NewByteArray(env, outputLen);
		jbyte *arrRetPt = (*env)->GetByteArrayElements(env, arrRet, 0);
		memcpy(arrRetPt, output, outputLen);
		(*env)->ReleaseByteArrayElements(env, arrRet, arrRetPt, 0);
	}
	*/

/*
	unsigned char *encinput="hello%2C%20world%21%20%E6%AC%A2%E8%BF%8E%E4%BD%A0%E3%80%82";
	int inputLen = strlen(encinput);
	//int ret = UrlDecode(encinput, inputLen, output, 4095);
	//if (ret != 0){
	//	arrRet = (*env)->NewByteArray(env, ret);
	//	jbyte *arrRetPt = (*env)->GetByteArrayElements(env, arrRet, 0);
	//	memcpy(arrRetPt, output, ret);
	//	(*env)->ReleaseByteArrayElements(env, arrRet, arrRetPt, 0);
	//}

	arrRet = (*env)->NewByteArray(env, inputLen);
	jbyte *arrRetPt = (*env)->GetByteArrayElements(env, arrRet, 0);
	memcpy(arrRetPt, encinput, inputLen);
	(*env)->ReleaseByteArrayElements(env, arrRet, arrRetPt, 0);

	if (myurl != NULL)
		free(myurl);

	return arrRet;
*/
}


/* This function will be call when the library first be load.
 * You can do some init in the libray. return which version jni it support.
 */
jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    void *venv;
    //LOGI("JNI_OnLoad!");

    if ((*vm)->GetEnv(vm, (void**)&venv, JNI_VERSION_1_4) != JNI_OK) {
        //LOGE("ERROR: GetEnv failed");
        return -1;
    }

    //add register function

     return JNI_VERSION_1_4;
}
