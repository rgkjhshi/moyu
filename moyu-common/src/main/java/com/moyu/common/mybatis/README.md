## mybatis-plus组件包

### 功能
* 定义数据库表通用基础实体
* 定义通用字段自动填充处理类
* 配置类中添加分页插件配置

### 依赖
本模块需要如下依赖
```xml
    <dependencies>
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