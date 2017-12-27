package com.mvcapp.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.mvcapp.db.JdbcUtils;

/**
 * 封装基本的CRUD的方法，以供子类继承使用
 * @author yinhechen
 *
 */
public class DAO<T> {

	private Class<T>clazz;
	
	private QueryRunner queryRunner=new QueryRunner();
	
	public DAO() {
		Type superClass=getClass().getGenericSuperclass();
		if (superClass instanceof ParameterizedType) {
			
			ParameterizedType parameterizedType=(ParameterizedType) superClass;
			
			Type[] typeArgs=parameterizedType.getActualTypeArguments();
			
			if (typeArgs!=null&&typeArgs.length>0) {
				
				if (typeArgs[0] instanceof Class) {
					clazz=(Class<T>) typeArgs[0];
				}
			}
			
		}
		
		
	}
	
	
	
	/**
	 * 返回某个字段的值
	 * @param sql
	 * @param args
	 * @return
	 */
	public <E> E getForValue(String sql,Object...args) {
		
		Connection connection=null;
		try {
			connection=JdbcUtils.getConnection();
			return (E) queryRunner.query(connection, sql,new ScalarHandler(),args);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.releaseConnection(connection);
		}
		return null;
	}
	
	
	/**
	 * 返回T所对应的List
	 * @param sql
	 * @param args
	 * @return
	 */
	public List<T> getForList(String sql,Object...args){
		
		Connection connection=null;
		try {
			connection=JdbcUtils.getConnection();
			return queryRunner.query(connection, sql,new BeanListHandler<>(clazz),args);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.releaseConnection(connection);
		}
		return null;
	}
	
	/**
	 * 返回 对应的T的一个实例类的对象
	 * @param sql
	 * @param args
	 * @return
	 */
	public T get(String sql, Object...args) {
		Connection connection=null;
		try {
			connection=JdbcUtils.getConnection();
			return queryRunner.query(connection, sql,new BeanHandler<>(clazz),args);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.releaseConnection(connection);
		}
		
		return null;
		
		
	}
	
	
	/**
	 * 该方法封装了insert  delete  update操作 
	 * @param sql
	 * @param args 占位符
	 */
	public void update(String sql,Object...args) {
		Connection connection=null;
		try {
			connection=JdbcUtils.getConnection();
			queryRunner.update(connection,sql,args);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.releaseConnection(connection);
		}
		
	}
	
	
}
