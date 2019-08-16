package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.demo.dao.ScreeningroomDao;
import com.example.demo.pojo.Screeningroom;

/**
 * 
* @ClassName: ScreeningroomService 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:00:45 
*
 */
@Service
public class ScreeningroomService {
	@Resource
	private ScreeningroomDao screeningroomDao;
	
	/**
	 * 
	* @Title: queryById 
	* @Description: 根据id精确查找
	* @param screeningId
	* @return
	 */
	public Screeningroom queryById(int screeningId) {
		Optional<Screeningroom> optional = screeningroomDao.findById(screeningId);
		if(optional.isPresent()) {
			return optional.get();
		}
		else {
			return null;
		}
	}
	
	/**
	 * 
	* @Title: queryByStatus 
	* @Description: 根据影厅状态查询
	* @param screeningroomStatus
	* @return
	 */
	public List<Screeningroom> queryByStatus(int screeningroomStatus){
		return screeningroomDao.findScreeningroomByScreeningroomStatus(screeningroomStatus);
	}
	
	/**
	 * 
	* @Title: updateScreeningroom 
	* @Description: 更新影厅，不能修改永久关闭的影厅
	* @param screeningroom
	* @return
	 */
	public int updateScreeningroom(Screeningroom screeningroom) {
		Screeningroom temp = queryById(screeningroom.getScreeningroomId());
		if(temp.getScreeningroomStatus()==3) {
			return 2;
		}
		else  {
			screeningroomDao.save(screeningroom);
			return 1;
		}
	}
	
	
	/**
	 * 
	* @Title: deleteById 
	* @Description: 删除影厅，若不能删除，自动设为永久关闭
	* @param screeningroomId
	* @return
	 */
	public int deleteById(int screeningroomId) {
		if(!screeningroomDao.existsById(screeningroomId)) {
			return 2;
		}
		else {
			Screeningroom screeningroom = new Screeningroom();
			screeningroom.setScreeningroomId(screeningroomId);
			screeningroom.setScreeningroomStatus(3);
			updateScreeningroom(screeningroom);
			screeningroomDao.deleteById(screeningroomId);
			return 1;
		}
	}
}
