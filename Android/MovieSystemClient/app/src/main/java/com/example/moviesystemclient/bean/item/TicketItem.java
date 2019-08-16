package com.example.moviesystemclient.bean.item;


import com.example.moviesystemclient.bean.TicketView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketItem {

    public static List<TicketItem> getItemList(List<TicketView> list){
        Iterator<TicketView> iterator = list.iterator();
        List<TicketItem> result = new ArrayList<TicketItem>();
        while(iterator.hasNext()){
            TicketView temp = iterator.next();
            String se = "";
            String status ="";
            if(temp.getScreeningSpecialeffect()==1){
                se="2D";
            }
            else{
                se="3D";
            }
            if(temp.getTicketStatus()==1){
                status="未取票";
            }
            else if(temp.getTicketStatus()==2){
                status="已取票";
            }
            else{
                status="已取消";
            }
            String DATE_PATTERN = "HH:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
            String time1str = sdf.format(temp.getScreeningStarttime());
            String time2str = sdf.format(new Date(temp.getScreeningStarttime().getTime()+temp.getMovieDuration()*60000))+"散场";
            result.add(new TicketItem(temp.getMovieName(),time1str,time2str,se,temp.getScreeningroomName(),temp.getSeatName(),String.valueOf(temp.getScreeningPrice()),status));
        }
        return result;
    }

    public TicketItem(String name, String startime, String endtime, String se, String sr, String seat, String price, String status) {
        this.name = name;
        this.startime = startime;
        this.endtime = endtime;
        this.se = se;
        this.sr = sr;
        this.seat = seat;
        this.price = price;
        this.status = status;
    }
    public TicketItem(){

    }

    private String name;
    private String startime;
    private String endtime;
    private String se;
    private String sr;
    private String seat;
    private String price;
    private String status;
}
