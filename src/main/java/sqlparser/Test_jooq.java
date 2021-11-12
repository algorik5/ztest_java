package sqlparser;

import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import org.jooq.DSLContext;
import org.jooq.Meta;
import org.jooq.SQLDialect;
import org.jooq.Source;
import org.jooq.impl.DSL;

import net.sf.jsqlparser.*;
import net.sf.jsqlparser.parser.*;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

public class Test_jooq {

	 public static void main(String[] args) throws Exception {
	        
		 
		 String url = "jdbc:postgresql://localhost/DEMO_Basic1_SysCfg";
	        Properties props = new Properties();
	        props.setProperty("user", "postgres");
	        props.setProperty("password", "swx");

	        java.sql.Connection conn = DriverManager.getConnection(url, props);
	            final DSLContext dsl = DSL.using(conn, SQLDialect.POSTGRES);
	            final Meta specialMeta = dsl.meta(Source.of("CREATE TABLE tutorials_tbl ( " +
	                    "   id INT NOT NULL, " +
	                    "   title VARCHAR(50) NOT NULL, " +
	                    "   author VARCHAR(20) NOT NULL, " +
	                    "   submission_date DATE, " +
	            ");"));
	            
	            
	            DSL.using(props)
	            .parser()
	            .parseResultQuery("SELECT * FROM (VALUES (1, 'a'), (2, 'b')) t(a, b)");
	            
		 if(1==1) return;
		 
		 
		 
		 
		 
		 
		 
	        String sql = "select a,b from tbl_city";
	        getColumns(sql).stream().forEach(c-> System.out.println(c)); // get a b

	        System.out.println("----");

	        String sql2 = "select a from (select b from table1 left join table2 on table2.c=table1.d)"; // get a
	        getColumns(sql2).stream().forEach(c-> System.out.println(c));

	        System.out.println("----");

	        ///////////// * 리턴 ---- 직접쿼리해야함
	        String sql3 = "select * from tbl_city";
	        getColumns(sql3).stream().forEach(c-> System.out.println(c)); // get *

	        //String sql3 = "select * from tbl_city";
	        //getColumns(sql).stream().forEach(c-> System.out.println(c)); // get *
	        //System.out.println("----");

	    }
	
	public static List<String> getColumns(String sql) throws JSQLParserException {
	    CCJSqlParserManager parserRealSql = new CCJSqlParserManager();

	    Statement stmt = parserRealSql.parse(new StringReader(sql)); // create a jSqlParser Statement from the sql

	    List<String> list=new ArrayList<String>(); // contains the columns result

	    if (stmt instanceof Select) { // only parse select sql
	        Select selectStatement = (Select) stmt; //convert to Select Statement
	        PlainSelect ps = (PlainSelect)selectStatement.getSelectBody();

	        List<SelectItem> selectitems = ps.getSelectItems();
	        selectitems.stream().forEach(selectItem -> list.add(selectItem.toString()));//add the selected items to result
	    }
	    return list;
	}	
}

