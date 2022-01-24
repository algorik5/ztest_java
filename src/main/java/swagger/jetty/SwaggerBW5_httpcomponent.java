package swagger.jetty;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.*;

import org.apache.http.*;
import org.apache.http.config.*;
import org.apache.http.entity.*;
import org.apache.http.impl.nio.*;
import org.apache.http.impl.nio.reactor.*;
import org.apache.http.nio.*;
import org.apache.http.nio.entity.*;
import org.apache.http.nio.protocol.*;
import org.apache.http.nio.reactor.*;
import org.apache.http.protocol.*;

import util.Log;

public class SwaggerBW5_httpcomponent {
	public static void main(String[] args) throws Exception
	{
		startServer();
	}
	
	public static void startServer() throws Exception
	{
		///////////////////// http://localhost:20000/swagger-ui/index.html
		
		String docRoot = "";//"test";
//      docRoot = "D:\\project\\kwakEclipse\\ztest_java\\src\\main\\resources";
//      docRoot = "D:\\project\\kwakEclipse\\ztest_java\\src\\main\\resources\\swagger-ui";
//		docRoot = "C:\\Users\\epozen\\.TIBCO\\working\\ahello_http\\tomcat\\webapps\\servlet\\swagger-ui";
		docRoot = "C:\\Users\\epozen\\.TIBCO\\working\\ahello_http\\tomcat\\webapps\\servlet";
		int port = 20000;
		
		HttpProcessor httpproc = HttpProcessorBuilder.create().add(new ResponseContent()).add(new ResponseDate())
                .add(new ResponseServer("Test/1.1")).add(new ResponseConnControl()).build();

        UriHttpAsyncRequestHandlerMapper registry = new UriHttpAsyncRequestHandlerMapper();
        registry.register("*", new HttpFileHander(new File(docRoot)));
//        registry.register("/swagger-ui/*", new HttpFileHander(new File(docRoot)));
        HttpAsyncService protocolHandler = new HttpAsyncService(httpproc, registry) {
            @Override
            public void connected(final NHttpServerConnection conn) {
                System.out.println(conn + ": connection open");
                super.connected(conn);
            }

            @Override
            public void closed(final NHttpServerConnection conn) {
                System.out.println(conn + ": connection closed");
                super.connected(conn);
            }
        };
        
        
        
        NHttpConnectionFactory<DefaultNHttpServerConnection> connFacotry;

        connFacotry = new DefaultNHttpServerConnectionFactory(ConnectionConfig.DEFAULT);

        IOEventDispatch ioEventDispatch = new DefaultHttpServerIODispatch(protocolHandler, connFacotry);
        IOReactorConfig config = IOReactorConfig.custom().setIoThreadCount(1).setSoTimeout(3000)
                .setConnectTimeout(3000).build();
        ListeningIOReactor ioReactor = new DefaultListeningIOReactor(config);
    	Log.log("################## listen ");
        ioReactor.listen(new InetSocketAddress(port));
    	Log.log("################## execute ");
        ioReactor.execute(ioEventDispatch);
        
    	Log.log("################## END ");
	}
	
	
	static class HttpFileHander implements HttpAsyncRequestHandler<HttpRequest> {
        private final File docRoot;

        public HttpFileHander(final File docRoot) {
            super();
            this.docRoot = docRoot;
        }

        public HttpAsyncRequestConsumer<HttpRequest> processRequest(final HttpRequest request,
                final HttpContext context) {
        	Log.log("################## processRequest ");
            return new BasicAsyncRequestConsumer();
        }

        public void handle(final HttpRequest request, final HttpAsyncExchange httpexchange,
                final HttpContext context) throws UnsupportedEncodingException, MethodNotSupportedException {
        	Log.log("################## handle ");
            HttpResponse response = httpexchange.getResponse();
            handleInternal(request, response, context);
            httpexchange.submitResponse(new BasicAsyncResponseProducer(response));
        }

        private void handleInternal(final HttpRequest request, final HttpResponse response,
                final HttpContext context) throws MethodNotSupportedException, UnsupportedEncodingException {
        	Log.log("################## handleInternal ");
            HttpCoreContext coreContext = HttpCoreContext.adapt(context);

            String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
            if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
                throw new MethodNotSupportedException(method + " method not supported");
            }

            String target = request.getRequestLine().getUri();
            final File file = new File(this.docRoot, URLDecoder.decode(target, "UTF-8"));
            if (!file.exists()) {
                response.setStatusCode(HttpStatus.SC_NOT_FOUND);
                NStringEntity entity = new NStringEntity(
                        "<html><body><h1>File " + file.getPath() + " not found</h1></body></html>",
                        ContentType.create("text/html", "UTF-8"));
                response.setEntity(entity);
                System.out.println("File " + file.getPath() + " not found");
            } else if (!file.canRead() || file.isDirectory()) {
                response.setStatusCode(HttpStatus.SC_FORBIDDEN);
                NStringEntity entity = new NStringEntity(
                        "<html><body><h1>File " + file.getPath() + " access denied</h1></body></html>",
                        ContentType.create("text/html", "UTF-8"));
                response.setEntity(entity);
                System.out.println("File " + file.getPath() + " access denied");
            } else {
                NHttpConnection conn = coreContext.getConnection(NHttpConnection.class);
                response.setStatusCode(HttpStatus.SC_OK);
                NFileEntity body = new NFileEntity(file, ContentType.create("text/html"));
                response.setEntity(body);
                System.out.println(conn + ": serving file " + file.getPath());
            }
        }
    }
}
		
		
		
		
		
		
		
		
		
		
//		
//		
//		//////// 1. dependency - httpcore5 5.1.3
//		//////// 2. start
//		//////// 3. http://localhost:30004/swagger-ui/index.html
//		//////// css 등이 제대로 표현안됨
//		String docRoot = "";//"test";
//        docRoot = "D:\\project\\kwakEclipse\\ztest_java\\src\\main\\resources";
//        //docRoot = "D:\\project\\kwakEclipse\\ztest_java\\src\\main\\resources\\swagger-ui";
//		int port = 30004;
//		SocketConfig socketConfig = SocketConfig.custom()
//                .setSoTimeout(15, TimeUnit.SECONDS)
//                .setTcpNoDelay(true)
//                .build();
//		
//		HttpServer server = ServerBootstrap.bootstrap()
//                .setListenerPort(port)
//                .setSocketConfig(socketConfig)
//                //.setSslContext(sslContext)
//                .setExceptionListener(new ExceptionListener() {
//
//                    @Override
//                    public void onError(final Exception ex) {
//                        ex.printStackTrace();
//                    }
//
//                    @Override
//                    public void onError(final HttpConnection conn, final Exception ex) {
//                        if (ex instanceof SocketTimeoutException) {
//                            System.err.println("Connection timed out");
//                        } else if (ex instanceof ConnectionClosedException) {
//                            System.err.println(ex.getMessage());
//                        } else {
//                            ex.printStackTrace();
//                        }
//                    }
//
//                })
//                .register("*", new HttpFileHandler(docRoot))// /echo *
//                //.register("/swagger-ui", new HttpFileHandler(docRoot))// /echo *
//                //.registerHandler("/css/*", new CssHandler(fileSystem.getPath("www/css"), "/css"))
//                
//                .create();
//
//        server.start();
//    	Log.log("################## server.start ");
//        server.awaitTermination(TimeValue.MAX_VALUE);
//    	Log.log("################## server.awaitTermination ");
//	}
//	
//	static class HttpFileHandler implements HttpRequestHandler  {
//
//        private String docRoot;
//
//        public HttpFileHandler(final String docRoot) {
//            super();
//            this.docRoot = docRoot;
//        }
//
//        @Override
//        public void handle(
//                final ClassicHttpRequest request,
//                final ClassicHttpResponse response,
//                final HttpContext context) throws HttpException, IOException {
//
//        	Log.log("---------- handle 1 ");
//            final String method = request.getMethod();
//            if (!Method.GET.isSame(method) && !Method.HEAD.isSame(method) && !Method.POST.isSame(method)) {
//                throw new MethodNotSupportedException(method + " method not supported");
//            }
//            final String path = request.getPath();
//        	Log.log("---------- handle "+"#path="+path);
//
//            final HttpEntity incomingEntity = request.getEntity();
//            if (incomingEntity != null) {
//                final byte[] entityContent = EntityUtils.toByteArray(incomingEntity);
//                System.out.println("Incoming incomingEntity content (bytes): " + entityContent.length);
//            }
//
//            
//            ////////////////// string reply
////            final StringEntity str = new StringEntity(
////                    "<html><body><h1>OK</h1></body></html>",
////                    ContentType.create("text/html", "UTF-8"));
////            response.setEntity(str);
////            if(1==1) return;
//
//            
//            
//            ////////////////// file reply
//            final File file1 = new File(this.docRoot, URLDecoder.decode(path, "UTF-8"));
//        	Log.log("---------- file1 "+"#file1="+file1.getAbsolutePath());//D:\static\index1.html
//            final HttpCoreContext coreContext1 = HttpCoreContext.adapt(context);
//            final EndpointDetails endpoint1 = coreContext1.getEndpointDetails();
//            response.setCode(HttpStatus.SC_OK);
//            final FileEntity body1 = new FileEntity(file1, ContentType.create("text/html", (Charset) null));
//            response.setEntity(body1);
//            System.out.println(endpoint1 + ": serving file " + file1.getPath());
//            if(1==1) return;
//
//
//                
//                
//                
//            final File file = new File(this.docRoot, URLDecoder.decode(path, "UTF-8"));
//            if (!file.exists()) {
//
//                response.setCode(HttpStatus.SC_NOT_FOUND);
//                final String msg = "File " + file.getPath() + " not found";
//                final StringEntity outgoingEntity = new StringEntity(
//                        "<html><body><h1>" + msg + "</h1></body></html>",
//                        ContentType.create("text/html", "UTF-8"));
//                response.setEntity(outgoingEntity);
//                System.out.println(msg);
//
//            } else if (!file.canRead() || file.isDirectory()) {
//
//                response.setCode(HttpStatus.SC_FORBIDDEN);
//                final String msg = "Cannot read file " + file.getPath();
//                final StringEntity outgoingEntity = new StringEntity(
//                        "<html><body><h1>" + msg + "</h1></body></html>",
//                        ContentType.create("text/html", "UTF-8"));
//                response.setEntity(outgoingEntity);
//                System.out.println(msg);
//
//            } else {
//                final HttpCoreContext coreContext = HttpCoreContext.adapt(context);
//                final EndpointDetails endpoint = coreContext.getEndpointDetails();
//                response.setCode(HttpStatus.SC_OK);
//                final FileEntity body = new FileEntity(file, ContentType.create("text/html", (Charset) null));
//                response.setEntity(body);
//                System.out.println(endpoint + ": serving file " + file.getPath());
//            }
//        }
//
//    }
//}
