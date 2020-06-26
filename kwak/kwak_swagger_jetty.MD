
# ---------------------------- jetty + swagger
# ------  pom.xml
```
<dependency> 
	<groupId>io.springfox</groupId> 
	<artifactId>springfox-swagger-ui</artifactId> 
	<version>2.9.2</version> 
</dependency> 
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
<dependency>
    <groupId>org.webjars</groupId>
    <artifactId>swagger-ui</artifactId>
    <version>3.27.0</version>
</dependency>
<dependency>
    <groupId>org.webjars</groupId>
    <artifactId>webjars-locator</artifactId>
    <version>0.40</version>
</dependency>
```

# ------  swagger.json
src/main/resources/static/swagger.json

# ------ SwaggerJetty.java
```
///// http://localhost:8080/swagger.json
        resourceHandler.setResourceBase("./src/main/resources/static");
///// http://localhost:8080/swagger/?url=http://localhost:8080/swagger.json
        swagger.setResourceBase(SwaggerJetty.class.getClassLoader().getResource("META-INF/resources/webjars/swagger-ui/3.27.0").toURI().toString());
        contextswagger.setContextPath("/swagger/");
```

# ------ web
= http://localhost:8080/swagger/?url=http://localhost:8080/swagger.json
```
hello 테스트
json 테스트
```

