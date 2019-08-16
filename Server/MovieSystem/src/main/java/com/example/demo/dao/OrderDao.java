package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.pojo.Ordertable;

public interface OrderDao extends JpaRepository<Ordertable, String>  {

}
