package com.example.demo.management;


/**
 * 
* @ClassName: OrderManagement 
* @Description: 生成订单时，锁定座位。
* @author xuanpengyu@faxmail.com
* @date 2019年7月3日 上午11:24:23 
*
 */
public class OrderManagement {
	OrderManagement(){
		lock = false;
	}
	private static OrderManagement instance = new OrderManagement();
	public static synchronized OrderManagement getInstance() {
		return instance;
	}
	private boolean lock;
	
	/**
	 * 
	* @Title: peekLock 
	* @Description: 查看锁
	* @return
	 */
	public boolean peekLock() {
		return lock;
	}
	
	/**
	 * 
	* @Title: takeLock 
	* @Description: 抢占锁，10*100ms
	* @return
	 */
	public boolean takeLock() {
		int i=0;
		while(lock) {
			if(i>=10) {
				return false;
			}
			else {
				++i;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				//  自动生成的 catch 块
				return false;
			}
		}
		lock=true;
		return true;
	}
	
	/**
	 * 
	* @Title: releaseLock 
	* @Description: 释放锁
	 */
	public void releaseLock() {
		lock = false;
	}
	
}
