package com.example.moviesystemmanager.encryption;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Signature {
	long generatedTime;
	String phone;
	long endTime;
	int key;
	String rsaPublic;
	public Signature(){
		
	}

	public Signature(String signatureStr){
		String[] param = signatureStr.split(",");
		if(param.length!=3) {
			return;
		}
		else {
			generatedTime = Long.valueOf(param[0]);
			phone = param[1];
			endTime = Long.valueOf(param[2]);
		}
	}

	public String toString() {
		return new String(generatedTime+","+phone+","+endTime);
	}
}
