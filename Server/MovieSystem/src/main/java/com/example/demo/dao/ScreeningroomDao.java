package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.pojo.Screeningroom;

public interface ScreeningroomDao extends JpaRepository<Screeningroom, Integer>  {

	List<Screeningroom> findScreeningroomByScreeningroomStatus(int screeningroomStatus);

}
