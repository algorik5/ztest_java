package httptest;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpClient.*;
import java.net.http.HttpRequest.*;
import java.net.http.HttpResponse.*;
import java.util.*;
import java.util.concurrent.*;

import com.alibaba.fastjson.JSON;

import util.Log;


public class java11_httpclient {
	public static void main(String[] args) throws Exception
	{
		test_sync();
	}
	
	private static void test_sync() throws Exception
	{
		String api = "http://localhost:20000/api/v1/hello1";
		
		HttpClient client = HttpClient.newBuilder()
				.version(Version.HTTP_1_1)
				.build();

		Map<String,String> requestmap = Map.of("id","id-1","name","name-1");
		String requestBody = JSON.toJSONString(requestmap);
		//BodyPublisher body = BodyPublishers.ofString(requestBody);
		
		HttpRequest request = HttpRequest.newBuilder()
				  .uri(new URI(api))
				  .headers("Content-Type", "application/json;charset=UTF-8")
				  //.POST(BodyPublishers.noBody())
				  .POST(BodyPublishers.ofString(requestBody))
				  .build();
		
		//////// sync
		HttpResponse<String> response = HttpClient.newBuilder()
				  .build()
				  .send(request, BodyHandlers.ofString());
		Log.log("-------- 1 sync response body # "+ response +"#body="+response.body());
		
		//////// async
		String responsebody = client.sendAsync(request,BodyHandlers.ofString()).thenApply(HttpResponse::body)  //thenApply메소드로 응답body값만 받기
	            .get();
		Log.log("-------- 2 async response body # "+ responsebody);
		
		
		//////// async promise
//		client.sendAsync(request, BodyHandlers.ofString(), pushPromiseHandler())
//	    .thenAccept(pageResponse -> {
//	        System.out.println("Page response status code: " + pageResponse.statusCode());
//	        System.out.println("Page response headers: " + pageResponse.headers());
//	        String responseBody = pageResponse.body();
//	        System.out.println(responseBody);
//	    })
//	    .join();
		
		//////// async thread
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		CompletableFuture<HttpResponse<String>> response1 = HttpClient.newBuilder()
				  .executor(executorService)
				  .build()
				  .sendAsync(request, HttpResponse.BodyHandlers.ofString());
		Log.log("-------- 3 async thread response body # "+ response1);//jdk.internal.net.http.common.MinimalFuture@146044d7[Not completed] (id=99)
		String responsebody2 = response1.get().body();
		Log.log("-------- 3 async thread response body # "+ responsebody2);
	}
	
	
//	private static PushPromiseHandler<String> pushPromiseHandler() {
//	    return (HttpRequest initiatingRequest, 
//	        HttpRequest pushPromiseRequest, 
//	        Function<HttpResponse.BodyHandler<String>, 
//	        CompletableFuture<HttpResponse<String>>> acceptor) -> {
//	        acceptor.apply(BodyHandlers.ofString())
//	            .thenAccept(resp -> {
//	                System.out.println(" Pushed response: " + resp.uri() + ", headers: " + resp.headers());
//	            });
//	        System.out.println("Promise request: " + pushPromiseRequest.uri());
//	        System.out.println("Promise request: " + pushPromiseRequest.headers());
//	    };
//	}
}
