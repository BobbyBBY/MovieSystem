package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.pojo.Ticket;

public interface TicketDao extends JpaRepository<Ticket, String>  {

}
