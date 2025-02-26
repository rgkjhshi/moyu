## security组件包

### 功能
* SpringSecurity自动配置类
* 认证异常、授权异常统一处理
* JwtToken认证过滤器(用于获取令牌及用户信息存入上下文)
* 登陆用户信息对象及上下文工具类
* Jwt、Token等令牌处理

### 依赖
本模块需要如下依赖

```xml
<dependencies>
    <!-- spring-security  -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <!-- spring web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```