/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_star_yytv_JniInterface */

#ifndef _Included_com_star_yytv_JniInterface
#define _Included_com_star_yytv_JniInterface
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_star_yytv_JniInterface
 * Method:    getServerResponse
 * Signature: (IILjava/lang/String;Ljava/lang/String;)Ljava/lang/String;
 */
//JNIEXPORT jstring JNICALL Java_com_star_yytv_JniInterface_getServerResponse
//  (JNIEnv *, jobject, jint, jint, jstring, jstring);

/*
 * Class:     com_star_yytv_JniInterface
 * Method:    getServerResponse2
 * Signature: (II[B[B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_star_yytv_JniInterface_getServerResponse2
  (JNIEnv *, jobject, jint, jint, jbyteArray, jbyteArray);

/*
 * Class:     com_star_yytv_JniInterface
 * Method:    test
 * Signature: ([B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_star_yytv_JniInterface_test
  (JNIEnv *, jobject, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif
