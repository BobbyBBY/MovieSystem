package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.pojo.Seat;

public interface SeatDao  extends JpaRepository<Seat, Integer> {
	List<Seat> findByScreeningroomId(Integer screeningroomId);
	List<Seat> findByScreeningroomIdAndSeatStatus(Integer screeningroomId,Integer seatSatus);
	
	@Query(value = "SELECT seat.* "
			+ "FROM seat , ticket  where ticket.seat_id = seat.seat_id "
			+ "and ticket.screening_id = ?1",
			nativeQuery = true
			)
	List<Seat> findByScreeningId(int screeningid);
}
