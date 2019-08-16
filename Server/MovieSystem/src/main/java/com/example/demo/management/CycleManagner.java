package com.example.demo.management;

public class CycleManagner {
	@SuppressWarnings("unused")
	private static CycleManagner instance = new CycleManagner();
	
	public static CycleManagner getInstance() {
		return instance;
	}
	
	CycleManagner(){
		PhoneCodeManagement.cycle();
		PasswordManagement.cycle();
	}
}
