package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.pojo.TicketView;

public interface TicketViewDao extends JpaRepository<TicketView, String>{

	List<TicketView> findByOrderId(String orderId);

}
