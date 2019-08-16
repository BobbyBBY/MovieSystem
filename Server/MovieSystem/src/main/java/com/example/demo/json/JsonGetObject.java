package com.example.demo.json;

import java.lang.reflect.Field;

/**
 * 
* @ClassName: JsonGetObject 
* @Description: TODO 封装部分内部复杂的json     //!部分!//
* @author xuanpengyu@faxmail.com
* @date 2019年6月12日 上午11:03:40 
*
 */
public class JsonGetObject {
	@SuppressWarnings("rawtypes")
	public static String toString(Object o) throws IllegalAccessException {
        if(o!=null) {//判断传过来的对象是否为空
            StringBuilder sb = new StringBuilder("{");//定义一个保存数据的变量
            Class cs = o.getClass();//获取对象的类
            Field[] fields = cs.getDeclaredFields();//反射获取该对象里面的所有变量
            for (Field f : fields) {//遍历变量
                f.setAccessible(true);//强制允许访问私有变量
                Object value = f.get(o);//获取传递过来的对象 对应 f 的类型
                value = value == null ? "null" : value;//判断获取到的变量是否为空，如果为空就赋值字符串，否则下面代码会异常
                sb.append(f.getName() + ":\"" + value.toString() + "\" ");// f.getName()：获取变量名；value.toString()：变量值装String
            }
            sb.append("}");
            return sb.toString();
        }else{
            return "null";
        }
    }
}
