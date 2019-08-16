package com.example.demo.mapper;

import com.example.demo.pojo.TicketView;

public interface TicketViewMapper {
    int insert(TicketView record);

    int insertSelective(TicketView record);
}