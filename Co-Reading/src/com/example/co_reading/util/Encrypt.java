package com.example.co_reading.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt {
	public static String md5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
	
	public static String md5(InputStream is) {
		byte[] bytes = new byte[8192];
		int byteCount;
		byte[] digest = null;
		MessageDigest digester = null;

		try {
			digester = MessageDigest.getInstance("MD5");

			while ((byteCount = is.read(bytes)) > 0) {
				digester.update(bytes, 0, byteCount);
			}
			digest = digester.digest();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return toHexString(digest);
	}
    
    public static String SHA1_file(File file) {
   		InputStream is = null;
   		String SHA1 = null;	
        try {
        	is = new BufferedInputStream(new FileInputStream(file));
        	SHA1 = Encrypt.SHA1(is);
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }
        return SHA1;
	}
    
    public static String SHA1(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
  
    public static String SHA1(InputStream is) {
	    byte[] bytes = new byte[8192];
	    int byteCount;
	    byte[] digest = null;
	    MessageDigest digester = null;

	    try {
		    digester = MessageDigest.getInstance("SHA-1");

		    while ((byteCount = is.read(bytes)) > 0) {
			    digester.update(bytes, 0, byteCount);
		    }
		    digest = digester.digest();
	    } catch (IOException e) {
		    e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
		    e.printStackTrace();
	    }

	    return toHexString(digest);
    }

    public static String toHexString(byte[] keyData) {
        if (keyData == null) {
            return null;
        }
        int expectedStringLen = keyData.length * 2;
        StringBuilder sb = new StringBuilder(expectedStringLen);
        for (int i = 0; i < keyData.length; i++) {
            String hexStr = Integer.toString(keyData[i] & 0x00FF,16);
            if (hexStr.length() == 1) {
                hexStr = "0" + hexStr;
            }
            sb.append(hexStr);
        }
        return sb.toString();
    }
}
