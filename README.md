user-agent-parser Spring boot 3 Library
======================

This is a library that can be used to parse the User Agent header in a Spring Boot 3 application.

Import Dependency:
------
Maven
```xml
<dependency>
  <groupId>org.useragent.parse</groupId>
  <artifactId>resolver-spring-boot-3</artifactId>
  <version>1.0.0</version>
</dependency>
```

Gradle (Kotlin)
```gradle
implementation("org.useragent.parse:resolver-spring-boot-3:1.0.0")
```


To build the project, execute
```
mvn package
```

Configuration:
--------

```java
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.useragent.parse.DeviceResolverHandlerInterceptor;

class ResolverHandlerInterceptor implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(new DeviceResolverHandlerInterceptor()).addPathPatterns(ALL);
  }
}
```
Usage: 
--------
```java
import org.useragent.parse.Device;
import org.useragent.parse.DevicePlatform;
import org.useragent.parse.DeviceUtils;

Device device = DeviceUtils.getCurrentDevice(httpServletRequest);

device.isMobile() // true or false
device.isNormal() // true or false
device.isTablet() // true or false

DevicePlatform platform = device.getDevicePlatform();
