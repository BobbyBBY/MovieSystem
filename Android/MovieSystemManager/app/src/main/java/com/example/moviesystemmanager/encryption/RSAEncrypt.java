package com.example.moviesystemmanager.encryption;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

/**
 * 
* @ClassName: RSAEncrypt 
* @Description: TODO
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:35:19 
*
 */
public class RSAEncrypt {

	/**
	 * 
	* @Title: genKeyPair 
	* @Description: TODO 获取密钥对 平均耗时350ms，应该异步获取
	* @return
	* @throws NoSuchAlgorithmException
	 */
	public static String[] genKeyPair(int length) throws NoSuchAlgorithmException {  
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");  
		keyPairGen.initialize(length,new SecureRandom());//narmally , length = 1024 or 512  
		KeyPair keyPair = keyPairGen.generateKeyPair();  
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); 
		String publicKeyString = new String(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
		String privateKeyString = new String(Base64.getEncoder().encodeToString((privateKey.getEncoded())).toString());  
		String[] result = new String[2];
		result[0] = publicKeyString;
		result[1] = privateKeyString;
		return result;
	}  

	/**
	 * 
	* @Title: encrypt 
	* @Description: TODO 加密，平均耗时400ms
	* @param str
	* @param publicKey
	* @return
	* @throws Exception
	 */
	public static String encrypt( String str, String publicKey ) throws Exception{
		byte[] decoded = Base64.getDecoder().decode(publicKey);
		//X509EncodedKeySpec
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		String outStr = Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
		return outStr;
	}

	/** 
	 * 
	* @Title: decrypt 
	* @Description: TODO 解密，平均耗时15ms
	* @param str
	* @param privateKey
	* @return
	* @throws Exception
	 */
	public static String decrypt(String str, String privateKey) throws Exception{
		byte[] inputByte = Base64.getDecoder().decode(str.getBytes("UTF-8"));
		byte[] decoded = Base64.getDecoder().decode(privateKey);  
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));  
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		String outStr = new String(cipher.doFinal(inputByte));
		//不知道为什么安卓studio解密前面会多一点东西
		outStr = outStr.substring(outStr.length()-48,outStr.length());
		return outStr;
	}
}
