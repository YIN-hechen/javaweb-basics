package jdbc;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;



/**
 * 关于数据库连接池 C3P0连接池 DBCP连接池 Proxool连接池
 * http://blog.csdn.net/code_du/article/details/24419003
 * 
 * @author yinhechen
 *
 */
public class ConnectionPool {

	/**
	 * 使用dbcp 配置文件
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDBCP() throws Exception {
		Properties properties = new Properties();
		InputStream in = ConnectionPool.class.getClassLoader().getResourceAsStream("jdbc/dbcp.properites");
		properties.load(in);

		DataSource datasource = BasicDataSourceFactory.createDataSource(properties);

		System.out.println("testDBCP"+datasource.getConnection());

	}

	/**
	 * 使用c3p0
	 * @throws PropertyVetoException
	 * @throws SQLException
	 */
	@Test
	public void testhhh() throws PropertyVetoException, SQLException {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass("com.mysql.jdbc.Driver");
		cpds.setJdbcUrl("jdbc:mysql://localhost:3306/demo?useSSL=false");
		cpds.setUser("test");
		cpds.setPassword("123456");

		System.out.println("testC3P0 "+cpds.getConnection());
	}

}
