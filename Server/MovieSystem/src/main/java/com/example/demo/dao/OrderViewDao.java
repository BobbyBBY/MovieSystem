package com.example.demo.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.pojo.OrderView;

public interface OrderViewDao extends JpaRepository<OrderView, String> {

	Page<OrderView> findByAudiencePhoneAndOrderStatus(String audiencePhone, int orderStatus, Pageable pageable);

}
