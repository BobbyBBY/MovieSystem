package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.pojo.Screening;

public interface ScreeningDao extends JpaRepository<Screening, Integer>  {

}
