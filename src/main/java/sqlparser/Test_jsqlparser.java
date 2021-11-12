package sqlparser;

import java.io.*;
import java.util.*;

import net.sf.jsqlparser.*;
import net.sf.jsqlparser.parser.*;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

public class Test_jsqlparser {

	 public static void main(String[] args) throws JSQLParserException {
	        
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

