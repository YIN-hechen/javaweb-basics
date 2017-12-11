package jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Demo {

	public static void main(String[] args) throws Exception {

//		 String sql = "INSERT INTO `user`(username,birthday,sex,address) VALUES('大臣','1993-05-12','0','济宁');";
//		 String sql2 = "UPDATE user SET username='小臣' WHERE id=8 ;";
//		 DAO.update(sql);
//		
//		String sql3 = "SELECT * FROM user ; ";
//		Student student=DAO.getObject(Student.class,sql3);
//		System.out.println(student);
		
		DAO.KnowDatabaseMetaData();
		
		
		

	}

	
	
}
