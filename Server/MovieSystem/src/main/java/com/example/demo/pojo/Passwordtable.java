package com.example.demo.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.example.demo.encryption.Password;

import lombok.Data;

@Data
@Entity
public class Passwordtable {
	@Id
    private Integer passwordKey;

    private String passwordPublic;

    private String passwordPrivate;

    private String passwordDes;

    private Date passwordTime;

    public Passwordtable() {
    	
    }
    
    public Passwordtable(Password password) {
    	this.passwordKey = password.getKey();
    	this.passwordPublic = password.getPublicPassword();
    	this.passwordPrivate = password.getPrivatePassword();
    	this.passwordDes = password.getDESPassword();
    	passwordTime = new Date();
    }
    
    public Passwordtable(Integer passwordKey, String passwordPublic, String passwordPrivate, String passwordDes) {
    	this.passwordKey = passwordKey;
    	this.passwordPublic = passwordPublic;
    	this.passwordPrivate = passwordPrivate;
    	this.passwordDes = passwordDes;
    	passwordTime = new Date();
    }
    
    public Integer getPasswordKey() {
        return passwordKey;
    }

    public void setPasswordKey(Integer passwordKey) {
        this.passwordKey = passwordKey;
    }

    public String getPasswordPublic() {
        return passwordPublic;
    }

    public void setPasswordPublic(String passwordPublic) {
        this.passwordPublic = passwordPublic == null ? null : passwordPublic.trim();
    }

    public String getPasswordPrivate() {
        return passwordPrivate;
    }

    public void setPasswordPrivate(String passwordPrivate) {
        this.passwordPrivate = passwordPrivate == null ? null : passwordPrivate.trim();
    }

    public String getPasswordDes() {
        return passwordDes;
    }

    public void setPasswordDes(String passwordDes) {
        this.passwordDes = passwordDes == null ? null : passwordDes.trim();
    }

    public Date getPasswordTime() {
        return passwordTime;
    }

    public void setPasswordTime(Date passwordTime) {
        this.passwordTime = passwordTime;
    }
}