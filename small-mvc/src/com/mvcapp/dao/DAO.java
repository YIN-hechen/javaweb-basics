package com.mvcapp.dao;

import java.util.List;

/**
 * 封装基本的CRUD的方法，以供子类继承使用
 * @author yinhechen
 *
 */
public class DAO<T> {

	private Class<T>clazz;
	
	
	
	
	
	/**
	 * 返回某个字段的值
	 * @param sql
	 * @param args
	 * @return
	 */
	public <E> E getForValue(String sql,Object...args) {
		
		
		return null;
	}
	
	
	/**
	 * 返回T所对应的List
	 * @param sql
	 * @param args
	 * @return
	 */
	public List<T> getForList(String sql,Object...args){
		
		return null;
	}
	
	/**
	 * 返回 对应的T的一个实例类的对象
	 * @param sql
	 * @param args
	 * @return
	 */
	public T get(String sql, Object...args) {
		
		return null;
		
	}
	
	
	/**
	 * 该方法封装了insert  delete  update操作 
	 * @param sql
	 * @param args 占位符
	 */
	public void update(String sql,Object...args) {
		
		
		
	}
	
	
}
