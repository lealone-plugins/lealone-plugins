# Lealone-Platform

用于开发高并发 AI 应用的技术平台


### 1. 在 pom.xml 中增加依赖

```xml
    <dependencies>
        <dependency>
            <groupId>com.lealone.plugins</groupId>
            <artifactId>lealone-boot</artifactId>
            <version>8.0.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
```

### 2. 在 sql/tables.sql 文件中创建表

```sql
-- 创建表: user，会生成一个名为 User 的模型类
create table if not exists user (
  id long auto_increment primary key,
  name varchar,
  age int
)
package 'com.lealone.examples.fullstack.model' -- User 类所在的包名
generate code './src/main/java' -- User 类的源文件所在的根目录
```


### 3. 在 sql/services.sql 文件中创建服务

```sql
create service if not exists user_service implement by 'com.lealone.examples.fullstack.UserService'
```

### 4. 实现服务

```java
package com.lealone.examples.fullstack;

import com.lealone.examples.fullstack.model.User;

public class UserService {

    public Long addUser(String name, Integer age) {
        // 如果 name = 'zhh', age = 18
        // 对应的sql是: insert into user(name, age) values('zhh', 18);
        return new User().name.set(name).age.set(age).insert(); // 链式调用，insert()返回新增记录的 rowId
    }

    public User findByName(String name) {
        // 如果 name = 'zhh'
        // 对应的 sql 是: select * from user where name = 'zhh' limit 1
        return User.dao.where().name.eq(name).findOne();
    }
}
```

### 5. 启动应用

```java
package com.lealone.examples.fullstack;

import com.lealone.plugins.boot.LealoneApplication;

public class FullStackDemo {

    public static void main(String[] args) {
        LealoneApplication.start("test", "./sql/tables.sql", "./sql/services.sql");
    }
}

在浏览器中打开下面的 URL 进行测试:
http://localhost:8080/service/user_service/addUser?name=zhh&age=18
http://localhost:8080/service/user_service/findByName?name=zhh

