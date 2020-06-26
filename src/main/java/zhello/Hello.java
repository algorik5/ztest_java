package zhello;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Hello {

	public static void main(String[] args) throws Exception {

		log("=================== START # ");

		while(true)
		{
			Thread.sleep(1000);
			log("=================== END # ");
		}
		
	}
	
	  private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	  public static void log(String msg)
	  {
		  System.out.println("LOG]"+ format.format(new Date()) +">"+ msg);
	  }

}
