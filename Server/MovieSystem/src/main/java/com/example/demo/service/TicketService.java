package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.demo.dao.TicketDao;
import com.example.demo.dao.TicketViewDao;
import com.example.demo.pojo.Ticket;
import com.example.demo.pojo.TicketView;

/**
 * 
* @ClassName: TicketService 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:01:01 
*
 */
@Service
public class TicketService {

	@Resource
	private TicketDao ticketDao;
	@Resource
	private TicketViewDao ticketViewDao;
	
	
	/**
	 * 
	* @Title: queryById 
	* @Description: 根据电影票id查询基本信息
	* @param ticketId
	* @return
	 */
	public TicketView queryById(String ticketId) {
		Optional<TicketView> optional = ticketViewDao.findById(ticketId);
		if(optional.isPresent()) {
			return optional.get();
		}
		else {
			return null;
		}
	}
	
	/**
	 * 
	* @Title: buildTicket 
	* @Description:  生成电影票
	* @param ticket
	* @return
	 */
	public int buildTicket(Ticket ticket) {
		//其他机制保证了分支正确
		ticketDao.save(ticket);
		return 1;
	}
	
	/**
	 * 
	* @Title: existsById 
	* @Description: 
	* @param ticketId
	* @return
	 */
	public boolean existsById(String ticketId) {
		return ticketDao.existsById(ticketId);
	}
	
	/**
	 * 
	* @Title: updateTicket 
	* @Description: 只能修改未取票的电影票,只能修改电影票状态
	* @return
	 */
	public int updateTicket(String ticketId,int ticketStatus) {
		Optional<Ticket> optional = ticketDao.findById(ticketId);
		if(optional.isPresent()) {
			Ticket temp = optional.get();
			int tempStatus = temp.getTicketStatus();
			if(tempStatus==1) {
				temp.setTicketStatus(ticketStatus);
				ticketDao.save(temp);
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
	* @Title: queryTicketsById 
	* @Description:  根据订单号查询所有电影票
	* @param orderId
	* @return
	 */
	public List<TicketView> queryTicketsByOrderId(String orderId){
		return ticketViewDao.findByOrderId(orderId);
	}
}
