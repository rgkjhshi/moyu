## mybatis-plus组件包

### 功能

* 定义数据库表通用基础实体
* 定义通用字段自动填充处理类
* 数据权限拦截器
* 配置类中添加分页插件配置

### 依赖
本组件对于 common-security的以来主要用于自动填充处理类及数据权限拦截器，若不使用可删除相关代码

本模块需要如下依赖

```xml

<dependencies>
    <!-- common-security -->
    <dependency>
        <groupId>com.moyu</groupId>
        <artifactId>common-security</artifactId>
        <version>1.0.1-SNAPSHOT</version>
    </dependency>
    <!-- mybatis-plus 增强CRUD 参考：https://baomidou.com/introduce/ -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
    </dependency>
    <!-- mybatis-plus分页插件 参考：https://baomidou.com/plugins/pagination/ -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-jsqlparser-4.9</artifactId>
    </dependency>
</dependencies>
```

### 使用
在SpringBoot项目的配置文件，正常添加数据源配置、mybatis-plus配置即可

```yaml
# spring配置
spring:
  # 数据源配置
  datasource:
    url: jdbc:mysql://127.0.0.1/yourDB?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=utf8
    username: xxx
    password: xxx
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource

# MyBatis Plus配置 参考：https://baomidou.com/reference/
mybatis-plus:
  # 别名包扫描路径，用于给包中的类注册别名。注册后可以直接使用类名，无需使用全限定类名。
  type-aliases-package: com.moyu.system.sys.entity
  # 配置mapper的扫描，如果在 Mapper 中有自定义方法，需要配置此项。 默认值：["classpath*:/mapper/**/*.xml"]
  mapper-locations: classpath*:mapper/**/*.xml
  # 加载全局的配置文件
#  config-location: classpath:mybatis-config.xml
```