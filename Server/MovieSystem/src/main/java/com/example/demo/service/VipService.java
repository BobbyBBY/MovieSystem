package com.example.demo.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.demo.dao.VipDao;
import com.example.demo.pojo.Vip;

/**
 * 
* @ClassName: VipService 
* @Description: TODO
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:01:11 
*
 */
@Service
public class VipService {
	@Resource
	private VipDao	vipDao;
	
	/**
	* save,update ,delete 方法需要绑定事务. 使用@Transactional进行事务的绑定.
	*
	*/
	
	/**
	 * 
	* @Title: ExistsById 
	* @Description: 是否存在
	* @param id
	* @return
	 */
	public boolean  existsById(Integer id)
	{
		 return  vipDao.existsById(id);
	}
	
	/**
	 * 
	* @Title: queryById 
	* @Description: TODO通过id获取实体
	* @param id
	* @return
	 */
	public Vip queryById(Integer id)
	{
		 return vipDao.findById(id).get();
	}
	
	/**
	 * 
	* @Title: queryByName 
	* @Description: TODO 根据名字查询会员
	* @param name
	* @return
	 */
	public List<Vip> queryByName(String name)
	{
		 return vipDao.findByVipNameLike("%"+name+"%");
	}
	
	
	
 	 @Transactional
 	public void save(Vip model) {
 		vipDao.save(model);
 	}
 	
 	/**
 	 * 
 	* @Title: deleteById 
 	* @Description: 单个删除
 	* @param id
 	 */
 	@Transactional
 	public void deleteById(Integer id) {
 		vipDao.deleteById(id);
 	}

}
