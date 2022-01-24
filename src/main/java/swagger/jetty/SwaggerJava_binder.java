package swagger.jetty;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.concurrent.*;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.impl.bootstrap.*;
import org.apache.hc.core5.http.io.*;
import org.apache.hc.core5.http.io.entity.*;
import org.apache.hc.core5.http.protocol.*;
import org.apache.hc.core5.util.TimeValue;

import util.Log;

public class SwaggerJava_binder {
	public static void main(String[] args) throws Exception
	{
		startServer();
		
		
	}
	
	public static void startServer() throws Exception
	{
    	Log.log("################## startServer START ");
		new Thread(()->{
			while(true)
			{
				try{ Thread.sleep(1000); }catch(Exception e) { e.printStackTrace(); }
			}
		}).start();;
	}
}