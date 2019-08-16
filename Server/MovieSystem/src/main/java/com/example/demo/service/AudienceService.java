package com.example.demo.service;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.demo.dao.AudienceDao;
import com.example.demo.pojo.Audience;

/**
 * 
* @ClassName: AudienceService 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午6:59:01 
*
 */
@Service
public class AudienceService {

	@Resource
	private AudienceDao audienceDao;
	
	/**
	 * 
	* @Title: queryById 
	* @Description: 根据手机号查询观众信息
	* @param phone
	* @return
	 */
	public Audience queryById(String phone) {
		Optional<Audience> optional = audienceDao.findById(phone);
		if(optional.isPresent()) {
			return optional.get();
		}
		else {
			return null;
		}
	}
	
	/**
	 * 
	* @Title: updateAudience 
	* @Description:  添加/修改
	* @param audience
	* @return
	 */
	public int updateAudience(Audience audience) {
		try {
			audienceDao.save(audience);
			return 1;
		}
		catch (Exception e) {
			return 100;
		}
	}
}
