package util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.*;

public class Log
{
	final static Logger logger = LoggerFactory.getLogger(Log.class);
	
	
	  private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	  public static void log(String msg)
	  {
		  System.out.println("LOG]"+ format.format(new Date()) +">"+ msg);
		  //logger.info(msg);
	  }

	  public static void main(String[] args) throws Exception
	  {
		 // System.out.println("=== lang3 # "+ org.apache.commons.lang3.StringUtils.endsWith("kwak", "xxx"));
	  }
}
