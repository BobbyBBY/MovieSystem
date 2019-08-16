package com.example.demo.service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.demo.dao.PasswordDao;
import com.example.demo.encryption.Password;
import com.example.demo.encryption.Signature;
import com.example.demo.json.JsonRequest;
import com.example.demo.management.PasswordManagement;
import com.example.demo.management.SignatureManagement;
import com.example.demo.pojo.Passwordtable;

/**
 * 
* @ClassName: PasswordService 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:00:41 
*
 */
@Service
public class PasswordService {
	
	@Resource
	private PasswordDao passwordDao;
	
	/**
	 * 
	* @Title: querySlowByKeyWithDelete 
	* @Description: 查询慢密钥，如果过期，则从数据库删除该密钥。4h有效期
	* @param passwordKey
	* @param  过期sign=1
	* @return
	 */
	public Passwordtable querySlowByKeyWithDelete(JsonRequest request) {
		Optional<Passwordtable> optional = passwordDao.findById(request.passwordkey);
		long time = new Date().getTime() - 14400000;
		if(optional.isPresent()) {
			Passwordtable passwordTable = optional.get();
			request.despassword = passwordTable.getPasswordDes();
			if(time>passwordTable.getPasswordTime().getTime()) {
				//过期了
				request.sign = 1;
				deleteById(passwordTable.getPasswordKey());
			}
			else {
				request.sign = 0;
			}
			return passwordTable;
		}
		else {
			//密钥不存在
			request.sign = 11;
			return null;
		}
	}
	
	public Password getSlow() throws NoSuchAlgorithmException {
		//生成password，并写入数据库，抛出异常
		Password password =  PasswordManagement.getSlow();
		Passwordtable passwordTable = new Passwordtable(password);
		updatePassword(passwordTable);
		return password;
	}
	
	public Password queryQuickByKey(JsonRequest request) {
		Password result = PasswordManagement.getQuick(request.passwordkey);
		if(result==null) {
			request.sign = 1;
			return null;
		}
		else {
			request.despassword = result.getDESPassword();
			request.sign = 0;
			return result;
		}
	}
	
	public Passwordtable querySlowByKeyWithoutDelete(int passwordKey) {
		Optional<Passwordtable> optional = passwordDao.findById(passwordKey);
		if(optional.isPresent()) {
			return optional.get();
		}
		else {
			return null;
		}
	}
	
	public Password getQuick() throws NoSuchAlgorithmException {
		return PasswordManagement.getQuick();
	}
	
	public Signature getSignature(String phone) throws NoSuchAlgorithmException {
		//生成signature，将其密钥写入数据库
		Signature signature = SignatureManagement.getSignature(phone);
		Password password = getSlow();
		signature.setKey(password.getKey());
		signature.setRsaPublic(password.getPublicPassword());
		return signature;
		
	}
	
	public int judgeSignature(Signature signature) {
		//判断签名，若过期，删除其在数据库的密钥
		int sigSign =  SignatureManagement.judgeSignature(signature);
		if(sigSign==7) {
			deleteById(signature.getKey());
			return sigSign;
		}
		else {
			return sigSign;
		}
	}
	
	public void deleteById(int passwordKey) {
		passwordDao.deleteById(passwordKey);
	}
	
	public Passwordtable updatePassword(Passwordtable passwordTable) {
		return passwordDao.save(passwordTable);
	}

}
