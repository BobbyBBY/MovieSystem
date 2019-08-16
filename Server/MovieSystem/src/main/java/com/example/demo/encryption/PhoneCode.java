package com.example.demo.encryption;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneCode {

	private String phone;
	private String code;
	private Long time;
	public PhoneCode() {
		
	}
	public PhoneCode(String phone,String code) {
		this.phone = phone;
		this.code = code;
		time = new Date().getTime();
	}
}
