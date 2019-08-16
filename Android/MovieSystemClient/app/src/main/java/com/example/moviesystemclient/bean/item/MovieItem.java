package com.example.moviesystemclient.bean.item;


import com.example.moviesystemclient.bean.Movie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieItem {

    private String name;
    private String time;
    private String introduction;

    public String getName() {
        return name;
    }

    public static List<MovieItem> getItemList(List<Movie> list){
        Iterator<Movie> iterator = list.iterator();
        List<MovieItem> result = new ArrayList<MovieItem>();
        while(iterator.hasNext()){
            Movie temp = iterator.next();
            result.add(new MovieItem(temp.getMovieName(),"",temp.getMovieIntroduction()));
        }
        return result;
    }

    public MovieItem(String name, String time, String introduction) {
        this.name = name;
        this.time = time;
        this.introduction = introduction;
    }

}
