
package com.example.demo.service;

import java.util.Date;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ScreeningDao;
import com.example.demo.dao.ScreeningViewDao;
import com.example.demo.pojo.Screening;
import com.example.demo.pojo.ScreeningView;

/**
 * 
* @ClassName: ScreeningService 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:00:51 
*
 */
@Service
public class ScreeningService {

	@Resource
	private ScreeningDao screeningDao;
	@Resource
	private ScreeningViewDao screeningViewDao;
	
	
	/**
	 * 
	* @Title: queryByMovieId 
	* @Description: 根据电影id查询从time开始当天所有场次
	* @param time 当前时间  
	* @param time2  由controller计算的当天最晚时间
	* @param movieId
	* @param pageable
	* @return
	 */
	public Page<ScreeningView> queryByMovieId(long time,long time2,String movieId,Pageable pageable){
		return screeningViewDao.findByMovieIdAndScreeningStarttimeGreaterThanAndScreeningStarttimeLessThan(movieId, new Date(time), new Date(time2),pageable);
	}
	
	/**
	 * 
	* @Title: queryByScreeningroomId 
	* @Description: 根据影厅id查询从time开始当天所有场次
	* @param time 当前时间  
	* @param time2  由controller计算的当天最晚时间
	* @param screeningroomId
	* @param pageable
	* @return
	 */
	public Page<ScreeningView> queryByScreeningroomId(long time,long time2,int screeningroomId,Pageable pageable){
		return screeningViewDao.findByScreeningroomId(new Date(time), new Date(time2), screeningroomId, pageable);
	}
	
	/**
	 * 
	* @Title: queryById 
	* @Description:  查询单个
	* @param screeningId
	* @param pageable
	* @return
	 */
	public Screening queryById(int screeningId){
		Optional<Screening> optional = screeningDao.findById(screeningId);
		if(optional.isPresent()) {
			return optional.get();
		}
		else {
			return null;
		}
	}
	
	
	/**
	 * 
	* @Title: updateScreening 
	* @Description: 更新场次
	* @param screening
	* @return
	 */
	public int updateScreening(Screening screening) {
		screeningDao.save(screening);
		return 1;
	}

	/**
	 * 
	* @Title: deleteById 
	* @Description: 删除场次，不保证删除
	* @param screeningId
	* @return
	 */
	public int deleteById(int screeningId) {
		if(!screeningDao.existsById(screeningId)) {
			return 2;
		}
		else {
			screeningDao.deleteById(screeningId);
			return 1;
		}
	}
}
