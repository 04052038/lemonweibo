package com.star.weibo4j.http;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import com.star.weibo4j.model.Constants;
import com.star.weibo4j.model.WeiboException;


/**
 * 临时存储上传图片的内容，格式，文件信息等
 * 
 */
public class ImageItem {
	private byte[] content;
	private String name;
	private String contentType;
	public ImageItem(byte[] content) throws WeiboException {
	    this(Constants.UPLOAD_MODE,content);
	}
	public ImageItem(String name,byte[] content) throws WeiboException{
		String imgtype = null;
		try {
		    imgtype = getImageType(content);
		} catch (Exception e) {
		    throw new WeiboException(e);
		}
		
	    if(imgtype!=null&&(imgtype.equalsIgnoreCase("image/gif")||imgtype.equalsIgnoreCase("image/png")
	            ||imgtype.equalsIgnoreCase("image/jpeg"))){
	    	this.content=content;
	    	this.name=name;
	    	this.contentType=imgtype;
	    }else{
	    	throw new WeiboException(
            "Unsupported image type, Only Suport JPG ,GIF,PNG!");
	    }
	}
	
	public byte[] getContent() {
		return content;
	}
	public String getName() {
		return name;
	}
	public String getContentType() {
		return contentType;
	}

//	public static String getContentType(byte[] mapObj) throws IOException {
//
//		String type = "";
//		ByteArrayInputStream bais = null;
//		MemoryCacheImageInputStream mcis = null;
//		try {
//			bais = new ByteArrayInputStream(mapObj);
//			mcis = new MemoryCacheImageInputStream(bais);
//			Iterator itr = ImageIO.getImageReaders(mcis);
//			while (itr.hasNext()) {
//				ImageReader reader = (ImageReader) itr.next();
//				if (reader instanceof GIFImageReader) {
//					type = "image/gif";
//				} else if (reader instanceof JPEGImageReader) {
//					type = "image/jpeg";
//				} else if (reader instanceof PNGImageReader) {
//					type = "image/png";
//				} else if (reader instanceof BMPImageReader) {
//					type = "application/x-bmp";
//				}
//			}
//		} finally {
//			if (bais != null) {
//				try {
//					bais.close();
//				} catch (IOException ioe) {
//
//				}
//			}
//			if (mcis != null) {
//				try {
//					mcis.close();
//				} catch (IOException ioe) {
//
//				}
//			}
//		}
//		return type;
//	}
	
	 public static String getImageType(File file){
	        if(file == null||!file.exists()){
	            return null;
	        }
	        InputStream in = null;
	        try {
	            in = new FileInputStream(file);
	            String type = getImageType(in);
	            return type;
	        } catch (IOException e) {
	            return null;
	        }finally{
	            try{
	                if(in != null){
	                    in.close();
	                }
	            }catch(IOException e){
	            }
	        }
	    }
	    
	    /**
	     * detect bytes's image type by inputstream
	     * @param in
	     * @return
	     * @see #getImageType(byte[])
	     */
	    public static String getImageType(InputStream in) {
	        if(in == null){
	            return null;
	        }
	        try{
	            byte[] bytes = new byte[8];
	            in.read(bytes);
	            return getImageType(bytes);
	        }catch(IOException e){
	            return null;
	        }
	    }

	    /**
	     * detect bytes's image type
	     * @param bytes 2~8 byte at beginning of the image file  
	     * @return image mimetype or null if the file is not image
	     */
	    public static String getImageType(byte[] bytes) {
	        if (isJPEG(bytes)) {
	            return "image/jpeg";
	        }
	        if (isGIF(bytes)) {
	            return "image/gif";
	        }
	        if (isPNG(bytes)) {
	            return "image/png";
	        }
	        if (isBMP(bytes)) {
	            return "application/x-bmp";
	        }
	        return null;
	    }

	    private static boolean isJPEG(byte[] b) {
	        if (b.length < 2) {
	            return false;
	        }
	        return (b[0] == (byte)0xFF) && (b[1] == (byte)0xD8);
	    }

	    private static boolean isGIF(byte[] b) {
	        if (b.length < 6) {
	            return false;
	        }
	        return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8'
	                && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
	    }

	    private static boolean isPNG(byte[] b) {
	        if (b.length < 8) {
	            return false;
	        }
	        return (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78
	                && b[3] == (byte) 71 && b[4] == (byte) 13 && b[5] == (byte) 10
	                && b[6] == (byte) 26 && b[7] == (byte) 10);
	    }

	    private static boolean isBMP(byte[] b) {
	        if (b.length < 2) {
	            return false;
	        }
	        return (b[0] == 0x42) && (b[1] == 0x4d);
	    }
}
