package com.example.demo.dao;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.pojo.Movie;

public interface MovieDao extends JpaRepository<Movie, String> {
	Page<Movie> findByMovieNameLikeAndMovieStatusNot(String name,int movieStatus,Pageable pageable);
	Page<Movie> findByMovieOnlinetimeLessThanAndMovieOfflinetimeGreaterThanAndMovieStatusNot(Date movieOnlinetime,Date movieOfflinetime,int movieStatus,Pageable pageable);
	Page<Movie> findByMovieOnlinetimeGreaterThanAndMovieStatusNot(Date movieOnlinetime, int movieStatus, Pageable pageable);
	
//	Page<Movie> findByMovieOfflinetimeLessThanAndMovieStatusNot(Date time,int movieStatus,Pageable pageable);
//	Page<Movie> findByMovieOfflinetimeGreaterThanAndMovieStatusNot(Date time,int movieStatus,Pageable pageable);
//	Page<Movie> findByMovieOnlinetimeGreaterThanAndMovieStatusNot(Date time,int movieStatus,Pageable pageable);
}
