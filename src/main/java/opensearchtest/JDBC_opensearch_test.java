package opensearchtest;

import java.sql.*;

import util.Log;

public class JDBC_opensearch_test {
	public static void main(String[] args) throws Exception
	{

		String url = "jdbc:opensearch://http://192.168.10.9:31000";
		String user = "";
		String pass = "";
		
		Connection con = DriverManager.getConnection(url,user,pass);
		
		String sql = "select * from alias100 where age = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setObject(1, 1);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next())
		{
			Log.log("\t rs # "+ "#id="+rs.getObject("id")+ "#name="+rs.getObject("name")+ "#age="+rs.getObject("age"));
		}
		
	}
}
