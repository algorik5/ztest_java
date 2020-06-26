package swagger.jetty;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

public class SwaggerJetty {

	public static void main(String[] args) throws Exception {
		int port = 8080;
		Server server = new Server(port);
		log("=============== server="+ port);
		
		/////////////////// ok - http://localhost:8080
		ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        //resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        resourceHandler.setResourceBase("./src/main/resources/static");
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, new DefaultHandler()});
        ContextHandler context = new ContextHandler();
        context.setContextPath("/");
        context.setHandler(resourceHandler);
		log("=============== resourceHandler="+ context.getContextPath() +"#base="+ resourceHandler.getResourceBase());

        
		///////////////// OK - http://localhost:8080/hello
        ContextHandler contexthello = new ContextHandler();
        contexthello.setContextPath("/hello");
        contexthello.setHandler(new AbstractHandler(){
			@Override
			public void handle(String target, Request baseRequest, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		        response.setContentType("text/html; charset=utf-8");
		        response.setStatus(HttpServletResponse.SC_OK);
		        PrintWriter out = response.getWriter();
		        out.println("<h1>hello</h1>");
		        baseRequest.setHandled(true);
			}});//new HelloHandler());
		log("=============== contexthello="+ contexthello.getContextPath());
        
		///////////////// OK - http://localhost:8080/json
        ContextHandler contextjson = new ContextHandler();
        contextjson.setContextPath("/json");
        contextjson.setHandler(new AbstractHandler(){
        	long no = 0;
        	Gson gson = new Gson();
			@Override
			public void handle(String target, Request baseRequest, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
				no++;
				Map map = ImmutableMap.of("version","v1","no",""+ no);
				String json = gson.toJson(map);
		        response.setContentType("application/json; charset=utf-8");//**** json
		        response.setStatus(HttpServletResponse.SC_OK);
		        PrintWriter out = response.getWriter();
		        out.println(json);
		        baseRequest.setHandled(true);
			}});//new HelloHandler());
		log("=============== contextjson="+ contextjson.getContextPath());

        
		/////////////////// ok - http://localhost:8080/swagger/
        ResourceHandler swagger = new ResourceHandler();
        swagger.setResourceBase(SwaggerJetty.class.getClassLoader()
            .getResource("META-INF/resources/webjars/swagger-ui/3.27.0")
            .toURI().toString());
        ContextHandler contextswagger = new ContextHandler();
        contextswagger.setContextPath("/swagger/");
        contextswagger.setHandler(swagger);
		log("=============== contextswagger="+ contextswagger.getContextPath());
        
        ContextHandlerCollection contexts = new ContextHandlerCollection( context, contextswagger,contexthello,contextjson );
        server.setHandler(contexts);
        

		log("=============== server.start()="+ server);
		server.start();
		server.join();
	}


	
	
	
	
	
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public static void log(String msg) {
		System.out.println("LOG]" + format.format(new Date()) + ">" + msg);
	}

}



class HelloServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println("<h1>Hello from HelloServlet</h1>");
    }
}

class HelloHandler extends AbstractHandler
{
    final String greeting;
    final String body;

    public HelloHandler() { this("Hello World"); }
    public HelloHandler(String greeting) { this(greeting, null); }
    public HelloHandler(String greeting, String body) { this.greeting = greeting; this.body = body; }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();

        out.println("<h1>" + greeting + "</h1>");
        if (body != null)
        {
            out.println(body);
        }

        baseRequest.setHandled(true);
    }
}
