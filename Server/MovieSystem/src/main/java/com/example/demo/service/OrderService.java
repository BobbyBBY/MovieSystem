package com.example.demo.service;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dao.OrderDao;
import com.example.demo.dao.OrderViewDao;
import com.example.demo.pojo.Ordertable;
import com.example.demo.pojo.OrderView;

/**
 * 
* @ClassName: OrderService 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午6:59:38 
*
 */
@Service
public class OrderService {

	@Resource
	private OrderDao orderDao;
	@Resource
	private OrderViewDao orderViewDao;
	
	
	
	/**
	 * 
	* @Title: queryById 
	* @Description:  根据id查询订单
	* @param oid
	* @return
	 */
	public Ordertable queryById(String orderId) {
		Optional<Ordertable> optional = orderDao.findById(orderId);
		if(optional.isPresent()) {
			return optional.get();
		}
		else {
			return null;
		}
	}
	
	/**
	 * 
	* @Title: buildOrder 
	* @Description:  生成订单
	* @param order
	* @return
	 */
	public void buildOrder(Ordertable order) {
		//有其他机制保证分支正确
		orderDao.save(order);
	}

	/**
	 * 
	* @Title: existsById 
	* @Description: 
	* @param orderId
	* @return
	 */
	public boolean existsById(String orderId) {
		return orderDao.existsById(orderId);
	}
	
	/**
	 * 
	* @Title: updateOrder 
	* @Description:  只能修改订单状态  
	* @param orderId
	* @param orderStatus
	* @return
	 */
	public int updateOrder(String orderId,int orderStatus) {
		Optional<Ordertable> optional = orderDao.findById(orderId);
		if(optional.isPresent()) {
			Ordertable temp = optional.get();
			int tempStatus = temp.getOrderStatus();
			if(tempStatus<4) {
				//!=4&&!=5
				temp.setOrderStatus(orderStatus);
				orderDao.save(temp);
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
	* @Title: queryByAudiencePhone 
	* @Description: 根据观众手机号查询
	* @param audiencePohone
	* @param orderStatus
	* @param pageable
	* @return
	 */
	public Page<OrderView> queryByAudiencePhone(String audiencePhone,int orderStatus,Pageable pageable){
		return orderViewDao.findByAudiencePhoneAndOrderStatus(audiencePhone, orderStatus, pageable);
	}
}
