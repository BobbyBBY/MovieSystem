package com.example.demo.dao;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.pojo.Harddisk;

public interface HarddiskDao extends JpaRepository<Harddisk, String>  {
	Page<Harddisk> findByHarddiskFilmstudioLike(String name,Pageable pageable);
	Page<Harddisk> findByHarddiskExpirationtimeLessThan(Date time,Pageable pageable);
	Page<Harddisk> findByHarddiskDecryptiontimeGreaterThan(Date time,Pageable pageable);
	Page<Harddisk> findByHarddiskExpirationtimeGreaterThanAndHarddiskDecryptiontimeLessThan(Date time1,Date time2,Pageable pageable);
}
