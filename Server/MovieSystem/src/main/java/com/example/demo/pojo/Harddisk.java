package com.example.demo.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Harddisk {
	@Id
    private String harddiskId;

    private String harddiskFilmstudio;

    private Date harddiskDecryptiontime;

    private Integer harddiskValidityduration;

    private Date harddiskExpirationtime;

    private String harddiskPassword;

    public String getHarddiskId() {
        return harddiskId;
    }

    public void setHarddiskId(String harddiskId) {
        this.harddiskId = harddiskId == null ? null : harddiskId.trim();
    }

    public String getHarddiskFilmstudio() {
        return harddiskFilmstudio;
    }

    public void setHarddiskFilmstudio(String harddiskFilmstudio) {
        this.harddiskFilmstudio = harddiskFilmstudio == null ? null : harddiskFilmstudio.trim();
    }

    public Date getHarddiskDecryptiontime() {
        return harddiskDecryptiontime;
    }

    public void setHarddiskDecryptiontime(Date harddiskDecryptiontime) {
        this.harddiskDecryptiontime = harddiskDecryptiontime;
    }

    public Integer getHarddiskValidityduration() {
        return harddiskValidityduration;
    }

    public void setHarddiskValidityduration(Integer harddiskValidityduration) {
        this.harddiskValidityduration = harddiskValidityduration;
    }

    public Date getHarddiskExpirationtime() {
        return harddiskExpirationtime;
    }

    public void setHarddiskExpirationtime(Date harddiskExpirationtime) {
        this.harddiskExpirationtime = harddiskExpirationtime;
    }

    public String getHarddiskPassword() {
        return harddiskPassword;
    }

    public void setHarddiskPassword(String harddiskPassword) {
        this.harddiskPassword = harddiskPassword == null ? null : harddiskPassword.trim();
    }
}