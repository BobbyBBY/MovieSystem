package com.example.moviesystemclient.encryption;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Password {
	int key;
	String publicPassword;
	String privatePassword;
	String DESPassword;
	Long time;
	public Password(int key, String publicPassword, String privatePassword, String DESPassword) {
		this.key = key;
		this.publicPassword = publicPassword;
		this.privatePassword = privatePassword;
		this.DESPassword = DESPassword;
		time = new Date().getTime();
	}
	public Password() {
		
	}
}
