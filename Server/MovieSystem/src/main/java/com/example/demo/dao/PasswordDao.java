package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.pojo.Passwordtable;


public interface PasswordDao extends JpaRepository<Passwordtable, Integer>{
	

}
