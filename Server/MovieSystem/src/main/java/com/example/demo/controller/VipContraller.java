package com.example.demo.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.VipService;

/**
 * 
* @ClassName: VipContraller 
* @Description: TODO
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:03:18 
*
 */
@RequestMapping(value = "/movieorder/vip")
@RestController
public class VipContraller {

		@Resource
		private VipService vipService;
	 
//		@GetMapping(value = "/getId")
//		public String getByName() {
//			List<Vip> b = vipService.queryByName("%"+"r"+"%");
//			
//			return String.valueOf(b.size());
//		}
		
//		@ResponseBody
//		@RequestMapping(value = "/vip", method = RequestMethod.POST)
//		public String getByJSON(@RequestBody JSONObject jsonParam) {
//		    // 直接将json信息打印出来
//		    System.out.println(jsonParam.toString());
//
//		    jsonParam.get
//		    
//		    // 将获取的json数据封装一层，然后在给返回
//		    JSONObject result = new JSONObject();
//		    result.put("msg", "ok");
//		    result.put("method", "json");
//		    result.put("data", jsonParam);
//
//		    return result.toString();
//		}
	
}
