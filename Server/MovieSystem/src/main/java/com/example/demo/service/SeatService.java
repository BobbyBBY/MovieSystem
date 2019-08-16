package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.demo.dao.SeatDao;
import com.example.demo.pojo.Seat;

/**
 * 
* @ClassName: SeatService 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:00:56 
*
 */
@Service
public class SeatService {

	@Resource
	private SeatDao seatDao;
	
	/**
	 * 
	* @Title: queryById 
	* @Description:  根据id查询座位
	* @param sid
	* @return
	 */
	public Seat queryById(int seatId) {
		Optional<Seat> optional = seatDao.findById(seatId);
		if(optional.isPresent()) {
			return optional.get();
		}
		else {
			return null;
		}
	}
	
	/**
	 * 
	* @Title: updateSeat 
	* @Description: 修改座位,不能删除座位，只能修改座位的状态
	* @param seat
	* @return
	 */
	public int updateSeat(Seat seat) {
		Optional<Seat> optional = seatDao.findById(seat.getSeatId());
		if(optional.isPresent()) {
			Seat temp = optional.get();
			if(temp.getSeatStatus()!=3) {
				temp.setSeatStatus(seat.getSeatStatus());
				seatDao.save(temp);
				return 1;
			}
			else {
				return 2;
			}
		}
		else {
			return 3;
		}
	}
	
	/**
	 * 
	* @Title: queryAllSeat 
	* @Description: 根据影厅id查询影厅所有座位
	* @param srid
	* @return
	 */
	public List<Seat> queryAllSeat(int screeningroomId){
		return seatDao.findByScreeningroomId(screeningroomId);
	}
	public List<Seat> queryAllSeat(int screeningroomId,int seatStatus){
		return seatDao.findByScreeningroomIdAndSeatStatus(screeningroomId,seatStatus);
	}
	
	/**
	 * 
	* @Title: queryAllSeatUsed 
	* @Description:  根据场次id查询该场次所有被使用的座位
	* @param screeningid
	* @return
	 */
	public List<Seat> queryAllSeatUsed(int screeningid){
		return seatDao.findByScreeningId(screeningid);
	}
	
	/**
	 * 
	* @Title: addSeats 
	* @Description: 批量添加座位
	* @param seatList
	* @return
	 */
	public int addSeats(List<Seat> seatList) {
		seatDao.saveAll(seatList);
		return 0;
	}
	
	
	
}
