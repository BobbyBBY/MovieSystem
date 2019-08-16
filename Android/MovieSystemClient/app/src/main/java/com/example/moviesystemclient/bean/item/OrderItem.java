package com.example.moviesystemclient.bean.item;


import com.example.moviesystemclient.bean.OrderView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItem {
    private String phone;
    private String name;
    private String time;
    private String price;
    private String count;
    private String status;

    public String getPhone() {
        return phone;
    }

    public static List<OrderItem> getItemList(List<OrderView> list){
        Iterator<OrderView> iterator = list.iterator();
        List<OrderItem> result = new ArrayList<OrderItem>();
        while(iterator.hasNext()){
            OrderView temp = iterator.next();
            String statusStr = "";
            int status = temp.getOrderStatus();
            switch(status){
                case 1:{
                    statusStr = "未付款";
                    break;
                }
                case 2:{
                    statusStr = "未取票";
                    break;
                }
                case 3:{
                    statusStr = "已取票";
                    break;
                }
                case 4:{
                    statusStr = "已取消";
                    break;
                }
                case 5:{
                    statusStr = "其他异常";
                    break;
                }
            }
            result.add(new OrderItem(temp.getAudiencePhone(),"包含："+temp.getMovieName(),(new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm")).format(temp.getOrderGeneratedtime()),String.valueOf(temp.getOrderTotalprice()),"共有"+String.valueOf(temp.getOrderTicketcount())+"张票",statusStr));
        }
        return result;
    }

    public OrderItem(String phone, String name, String time, String price, String count, String status) {
        this.phone = phone;
        this.name = name;
        this.time = time;
        this.price = price;
        this.count = count;
        this.status = status;
    }


}
