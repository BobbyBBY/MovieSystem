package com.example.moviesystemclient.encryption;


public class PhoneManagement {
    private static String phone;

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        PhoneManagement.phone = phone;
    }
}
