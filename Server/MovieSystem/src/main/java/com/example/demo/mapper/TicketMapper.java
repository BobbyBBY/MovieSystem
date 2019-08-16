package com.example.demo.mapper;

import com.example.demo.pojo.Ticket;

public interface TicketMapper {
    int insert(Ticket record);

    int insertSelective(Ticket record);
}