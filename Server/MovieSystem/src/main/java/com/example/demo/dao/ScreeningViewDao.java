package com.example.demo.dao;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.pojo.ScreeningView;

public interface ScreeningViewDao extends JpaRepository<ScreeningView, Integer>{

	Page<ScreeningView> findByMovieIdAndScreeningStarttimeGreaterThanAndScreeningStarttimeLessThan(
			String movieId, Date date, Date date2, Pageable pageable);

	Page<ScreeningView> findByScreeningroomId(Date date, Date date2, int screeningroomId,
			Pageable pageable);

    
	
	
}
