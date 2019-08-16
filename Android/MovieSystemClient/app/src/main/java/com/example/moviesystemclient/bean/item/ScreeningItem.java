package com.example.moviesystemclient.bean.item;


import com.example.moviesystemclient.bean.ScreeningView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScreeningItem {
    private String name;
    private String startime;
    private String endtime;
    private String se;
    private String sr;
    private String price;

    public static List<ScreeningItem> getItemList(List<ScreeningView> list){
        Iterator<ScreeningView> iterator = list.iterator();
        List<ScreeningItem> result = new ArrayList<ScreeningItem>();
        while(iterator.hasNext()){
            ScreeningView temp = iterator.next();
            String starttime;
            String endtime;
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
            starttime = sdf.format(temp.getScreeningStarttime());
            endtime = sdf.format(temp.getScreeningStarttime().getTime()+temp.getMovieDuration()*60000);
            String se = "";
            if(temp.getScreeningSpecialeffect()==1){
                se="2D";
            }
            else{
                se="3D";
            }
            result.add(new ScreeningItem(temp.getMovieName(),starttime,endtime+"散场",se,temp.getScreeningroomName(),String.valueOf(temp.getScreeningPrice())+"元"));
        }
        return result;
    }

    public ScreeningItem(String name, String startime, String endtime, String se, String sr, String price) {
        this.name = name;
        this.startime = startime;
        this.endtime = endtime;
        this.se = se;
        this.sr = sr;
        this.price = price;
    }

}
