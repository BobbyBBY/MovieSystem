package com.example.demo.service;

import java.util.Date;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dao.HarddiskDao;
import com.example.demo.pojo.Harddisk;

/**
 * 
* @ClassName: HarddiskService 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午6:59:14 
*
 */
@Service
public class HarddiskService {

	@Resource
	private HarddiskDao harddiskDao;
	
	/**
	 * 
	* @Title: queryByFilmstudio 
	* @Description:  根据电影公司名模糊查询
	* @param name
	* @return
	 */
	public Page<Harddisk> queryByFilmstudio(String name ,Pageable pageable){
		return harddiskDao.findByHarddiskFilmstudioLike("%"+name+"%", pageable);
	}
//	public List<Harddisk> queryByFilmstudio(String name ){
//		return harddiskDao.findByHarddiskFilmstudioLike("%"+name+"%");
//	}
	
	/**
	 * 
	* @Title: queryDecrytion 
	* @Description:  当前还不可以解密的硬盘 
 	* @param time 当前时间
	* @return
	 */
	public Page<Harddisk> queryDecrytion(long time ,Pageable pageable){
		return harddiskDao.findByHarddiskDecryptiontimeGreaterThan(new Date(time), pageable);
	}
	
	/**
	 * 
	* @Title: queryDecrytion 
	* @Description:  查询在有效期的硬盘 
 	* @param time 当前时间
	* @return
	 */
	public Page<Harddisk> queryValidity(long time1,long time2 ,Pageable pageable){
		return harddiskDao.findByHarddiskExpirationtimeGreaterThanAndHarddiskDecryptiontimeLessThan(new Date(time1), new Date(time2), pageable);
	}
	
	
	/**
	 * 
	* @Title: queryExpiration 
	* @Description:  查询已经到期的硬盘
	* @param time
	* @param pageable
	* @return
	 */
	public Page<Harddisk> queryExpiration(long time ,Pageable pageable){
		return harddiskDao.findByHarddiskExpirationtimeLessThan(new Date(time), pageable);
	}
	
	/**
	 * 
	* @Title: updateHarddisk 
	* @Description: 更新/插入
	* @param harddisk
	* @return
	 */
	public int updateHarddisk(Harddisk harddisk) {
		harddiskDao.save(harddisk);
		return 1;
	}
	
	/**
	 * 
	* @Title: qureyById 
	* @Description: 根据id精确查询
	* @param harddiskId
	* @return
	 */
	public Harddisk qureyById(String harddiskId) {
		Optional<Harddisk> optional = harddiskDao.findById(harddiskId);
		if(optional.isPresent()) {
			return optional.get();
		}
		else {
			return null;
		}
	}
	
	
}

