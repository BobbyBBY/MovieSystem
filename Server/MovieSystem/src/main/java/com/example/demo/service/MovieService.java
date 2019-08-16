package com.example.demo.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dao.MovieDao;
import com.example.demo.pojo.Movie;

/**
 * 
* @ClassName: MovieService 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午6:59:27 
*
 */
@Service
public class MovieService {
	@Resource
	private MovieDao movieDao;
	
	
	/**
	 * 
	* @Title: queryMoviesByName 
	* @Description:  根据电影名称模糊查询
	* @param name
	* @return
	 */
	public Page<Movie> queryByName(String name,int movieStatus,Pageable pageable){
		return movieDao.findByMovieNameLikeAndMovieStatusNot("%"+name+"%",movieStatus, pageable);
	}
	
//	/**
//	 * 已弃用
//	* @Title: updateMoviesOnline 
//	* @Description:  更改所有还没有上线但是达到上线日期的电影
//	* @param time
//	* @return 修改条数
//	 */
//	public int updateOnline(Timestamp timestamp) {
//		return 0;
//	}
	
//	/**
//	 * 已弃用
//	* @Title: updateMoviesOffline 
//	* @Description: 更改所有已上线但是达到下线日期的电影
//	* @param time
//	* @return 修改条数
//	 */
//	public int updateOffline(Timestamp timestamp) {
//		return 0;
//	}
	
	/**
	 * 
	* @Title: queryMovieOnline 
	* @Description: 查询即将上映的电影
	* @param timestamp
	* @param pageable
	* @return
	 */
	public Page<Movie> queryMovieOnline(Timestamp timestamp,Pageable pageable){
		return movieDao.findByMovieOnlinetimeGreaterThanAndMovieStatusNot(new Date(timestamp.getTime()),4, pageable);
	}
	
	
	/**
	 * 
	* @Title: queryByTime 
	* @Description:  查询当天所有电影。时间戳加24*60*60秒
	* @param 
	* @param pageable
	* @return
	 */
	public Page<Movie> queryMovieByTime(Timestamp timestamp,int movieStatus,Pageable pageable){
		return movieDao.findByMovieOnlinetimeLessThanAndMovieOfflinetimeGreaterThanAndMovieStatusNot(new Date(timestamp.getTime()), new Date(timestamp.getTime()+86400000),movieStatus, pageable);
	}
	
	/**
	 * 
	* @Title: updateMovie 
	* @Description: 添加或修改电影 
	* @param movie
	* @return
	 */
	public int updateMovie(Movie movie) {
		Optional<Movie> optional = movieDao.findById(movie.getMovieId());
		if(optional.isPresent()) {
			if(optional.get().getMovieStatus()!=4) {
				movieDao.save(movie);
				return 1;
			}
			else {
				return 2;
			}
		}
		else {
			movieDao.save(movie);
			return 1;
		}
	}
	
	/**
	 * 
	* @Title: existsById 
	* @Description: 判断该电影是否存在
	* @param movieId
	* @return
	 */
	public boolean existsById(String movieId) {
		return movieDao.existsById(movieId);
	}
	
	/**
	 * 
	* @Title: queryById 
	* @Description: 根据id精确查询
	* @param movieId
	* @return
	 */
	public Movie queryById(String movieId) {
		Optional<Movie> optional = movieDao.findById(movieId);
		if(optional.isPresent()) {
			return optional.get();
		}
		else {
			return null;
		}
	}
	
	/**
	 * 
	* @Title: deleteById 
	* @Description: 删除电影
	* @return
	 */
	public int deleteById(String movieId) {
		if(!existsById(movieId)) {
			return 2;
		}
		else {
			Movie movie = new Movie();
			movie.setMovieId(movieId);
			movie.setMovieStatus(4);
			updateMovie(movie);
			movieDao.deleteById(movieId);
			return 1;
		}
	}
}
