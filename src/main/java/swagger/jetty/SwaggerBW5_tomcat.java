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

public class SwaggerBW5_tomcat {
	public static void main(String[] args) throws Exception
	{
		startServer();
	}
	
	public static void startServer() throws Exception
	{
		//////// 1. dependency - httpcore5 5.1.3
		//////// 2. start
		//////// 3. http://localhost:30004/swagger-ui/index.html
		//////// css 등이 제대로 표현안됨
		//String docRoot = "";//"test";
        //docRoot = "D:\\project\\kwakEclipse\\ztest_java\\src\\main\\resources";
        //docRoot = "D:\\project\\kwakEclipse\\ztest_java\\src\\main\\resources\\swagger-ui";
		int webPort = 30004;
		
		
        Tomcat tomcat = new Tomcat();

        tomcat.setPort(webPort);

		//String webappDirLocation = "src/main/webapp";
		String webappDirLocation = "src/main/resources";
        StandardContext ctx = (StandardContext) tomcat.addWebapp("/",new File(webappDirLocation).getAbsolutePath());
        System.out.println("### addWebapp = "+new File("./" + webappDirLocation).getAbsolutePath());

//        WebResourceRoot resources = new StandardRoot(ctx);
//        //File additionWebInfClass = new File("target/classes");
//        File additionWebInfClass = new File("src\\main\\resources\\swagger-ui");
//        //resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClass.getAbsolutePath(), "/"));
//        resources.addPreResources(new DirResourceSet(resources, "/", additionWebInfClass.getAbsolutePath(), "/"));
//        //tomcat.addWebapp("/", new File(docBase).getAbsolutePath());
//        
//        ctx.setResources(resources);

        tomcat.start();
    	Log.log("################## tomcat.start ");
        tomcat.getServer().await();		
    	Log.log("################## tomcat.awaitTermination ");
	}
}