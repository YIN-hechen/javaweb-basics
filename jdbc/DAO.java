package jdbc;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.Test;

public class DAO {

	/**
	 * 对INSERT UPDATE DELECT 操作进行事务
	 */
	public static void updatTransaction(String sql, Object... args) {

		Connection connection = null;
		// 用于执行Sql语句的对象
		PreparedStatement preparedStatement = null;

		try {
			connection = getConnection2();
			// 开启事务：取消默认提交
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(sql);

			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}

			preparedStatement.executeUpdate();

			// 提交事务
			connection.commit();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				// 回滚事务
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			release(preparedStatement, null, null, connection, null);
		}

	}

	/**
	 * INSERT UPDATE DELECT 操作
	 * 
	 * @param sql
	 * @param args
	 */
	public static void update(String sql, Object... args) {

		Connection connection = null;
		// 用于执行Sql语句的对象
		PreparedStatement preparedStatement = null;

		try {
			connection = getConnection2();
			preparedStatement = connection.prepareStatement(sql);

			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			release(preparedStatement, null, null, connection, null);
		}
	}

	/**
	 * 返回某条记录的第一个字段
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public static <E> E getObject(String sql, Object... args) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = getConnection2();

			preparedStatement = (com.mysql.jdbc.PreparedStatement) connection.prepareStatement(sql);

			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}

			java.sql.ResultSet resultSet = preparedStatement.executeQuery();

			// ResultSet resultSet=preparedStatement.getGeneratedKeys();
			// 该ResultSet中只有一列GENERATED_KEY,用于存放新生成的主键值（前提是
			// connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)）

			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

			if (resultSet.next()) {
				return (E) resultSet.getObject(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			release(preparedStatement, null, null, connection, null);
		}

		return null;
	}

	/**
	 * 查询一条数据，返回对应的对象
	 * 
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	public static <T> T getObject(Class<T> clazz, String sql, Object... args) {

		List<T> mList = getListObject(clazz, sql, args);
		return mList.size() > 0 ? mList.get(0) : null;
	}

	/**
	 * 查询多条数据，返回对应的集合
	 * 
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	public static <T> List<T> getList(Class<T> clazz, String sql, Object... args) {

		return getListObject(clazz, sql, args);
	}

	/**
	 * 查询多条数据
	 * 
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	public static <T> List<T> getListObject(Class<T> clazz, String sql, Object... args) {
		List<T> mList = new ArrayList<>();

		T entity = null;

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = getConnection2();

			preparedStatement = (com.mysql.jdbc.PreparedStatement) connection.prepareStatement(sql);

			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}

			java.sql.ResultSet resultSet = preparedStatement.executeQuery();

			// ResutSet结果集元数据
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

			while (resultSet.next()) {
				entity = clazz.newInstance();
				for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
					String columnLabel = resultSetMetaData.getColumnLabel(i + 1); // 列名
					Object columnValue = resultSet.getObject(i + 1); // 列名对应的值
					ReflectionUtils.setFieldValue(entity, columnLabel, columnValue);
				}

				mList.add(entity);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			release(preparedStatement, null, null, connection, null);
		}

		return mList;
	}

	/**
	 * 了解下DatabaseMetaData元数据(描述数据库信息:版本号，用户名，连接字符串...)
	 */
	public static void KnowDatabaseMetaData() {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = getConnection();

			DatabaseMetaData data = connection.getMetaData();

			String version = data.getDatabaseProductVersion(); // 得到数据库版本号
			System.out.println(version);

			String user = data.getUserName(); // 得到用户名
			System.out.println(user);

			String url = data.getURL(); // 连接字符串
			System.out.println(url);

			ResultSet set = data.getCatalogs();// 有哪些数据库
			while (set.next()) {
				System.out.println(set.getObject(1));

			}

		} catch (Exception e) {
			e.printStackTrace();
			release(preparedStatement, null, null, connection, null);
		}

	}

	/**
	 * 了解下ResultSetMetaData 结果集元数据 可以得到有哪些列，列名，列的别名...
	 */
	public static void KnowResultSetMetaData(String sql, Object... args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = getConnection2();

			preparedStatement = (com.mysql.jdbc.PreparedStatement) connection.prepareStatement(sql);

			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}

			java.sql.ResultSet resultSet = preparedStatement.executeQuery();

			// ResutSet结果集元数据
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

			// 得到列的个数
			int columns = resultSetMetaData.getColumnCount();

			while (resultSet.next()) {
				for (int i = 0; i < columns; i++) {
					String columnName = resultSetMetaData.getColumnName(i + 1); // 列名
					String columnLabel = resultSetMetaData.getColumnLabel(i + 1); // 别名
					Object columnValue = resultSet.getObject(i + 1); // 列名对应的值
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			release(preparedStatement, null, null, connection, null);
		}

	}

	/**
	 * 通过Driver 反射配置文件 得到Connection对象
	 * 
	 * @throws Exception
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@Test
	public static Connection getConnection() throws Exception {
		String driverClass = null;
		String jdbcUrl = null;
		String user = null;
		String password = null;

		InputStream in = DAO.class.getClassLoader().getResourceAsStream("jdbc/jdbc.properties");
		Properties config = new Properties();
		config.load(in);
		driverClass = config.getProperty("driverClass");
		jdbcUrl = config.getProperty("jdbcUrl");
		user = config.getProperty("user");
		password = config.getProperty("password");

		Driver driver = (Driver) Class.forName(driverClass).newInstance();

		Properties info = new Properties();
		info.put("user", "root");
		info.put("password", "123456");

		Connection connection = driver.connect(jdbcUrl, info);
		return connection;

	}

	/**
	 * 通过DrivverManager 反射配置文件 得到Connection对象
	 * 
	 * @throws Exception
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@Test
	public static Connection getConnection2() throws InstantiationException, IllegalAccessException, Exception {
		String driverClass = null;
		String jdbcUrl = null;
		String user = null;
		String password = null;

		InputStream in = DAO.class.getClassLoader().getResourceAsStream("jdbc/jdbc.properties");
		Properties config = new Properties();
		config.load(in);
		driverClass = config.getProperty("driverClass");
		jdbcUrl = config.getProperty("jdbcUrl");
		user = config.getProperty("user");
		password = config.getProperty("password");

		Class.forName(driverClass);
		Connection connection = DriverManager.getConnection(jdbcUrl, user, password);

		// 可以设置隔离级别，MySql默认的隔离级别是REPEATABLE_READ
		// connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

		return connection;
	}

	/**
	 * 释放资源
	 * 
	 * @param preparedStatement
	 * @param resultSet
	 * @param statement
	 * @param connection
	 */
	public static void release(java.sql.PreparedStatement preparedStatement, ResultSet resultSet, Statement statement,
			Connection connection, CallableStatement callableStatement) {
		if (preparedStatement != null) {

			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (callableStatement != null) {
			try {
				callableStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 使用Statement
	 * 
	 * @param sql
	 */
	@Test
	public static void Query(String sql) {

		Connection connection = null;

		Statement statement = null;

		ResultSet resultSet = null;

		try {
			connection = getConnection2();

			statement = connection.createStatement();

			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) { // 查看结果集的下一条记录是否有效，若有效则下移指针。是Interator的hasNext和Next结合体
				int id = resultSet.getInt(1); // resulSet.getInt("id")起始值是从1开始的
				String username = resultSet.getString("username");
				Date bithday = resultSet.getDate(3);
				String sex = resultSet.getString("sex");
				String address = resultSet.getString("address");

				System.out.println(id);
				System.out.println(username);
				System.out.println(bithday);
				System.out.println(sex);
				System.out.println(address);
				System.out.println("-------------------");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			release(null, resultSet, statement, connection, null);

		}

	}

	/**
	 * 使用jdbc调用存储在数据库中的函数和存储过程
	 * 有四种  参考：http://sjsky.iteye.com/blog/1246657
	 * 
	 */
    public static void CallableStatment() {
    	Connection connection=null;
    	CallableStatement callableStatement=null;
    	try {
    		connection=getConnection2(); 
    		callableStatement=connection.prepareCall("{call TEST_MICHAEL(?,?,?)}");
    		callableStatement.setString(1, "");
    		callableStatement.registerOutParameter(2, java.sql.Types.REF_CURSOR);
    		callableStatement.execute();
    		
    		ResultSet rs = (ResultSet) callableStatement.getObject(2);
    		
    		//..............................
    		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			release(null, null, null, connection,callableStatement);
			
		}
    	
    	
    }
}
