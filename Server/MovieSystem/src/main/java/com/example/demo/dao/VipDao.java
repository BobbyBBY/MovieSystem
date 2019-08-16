package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.pojo.Vip;

public interface VipDao extends JpaRepository<Vip, Integer>  {

	List<Vip> findByVipNameLike(String name);
}
