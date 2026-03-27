# lealone-spring

使用 lealone-spring 插件可以用 tomcat + spring + lealone 这样的一体化方案开发 web 应用或微服务应用


### 在 pom.xml 中增加依赖

```xml
    <dependencies>
        <dependency>
            <groupId>com.lealone.plugins</groupId>
            <artifactId>lealone-spring</artifactId>
            <version>6.0.1</version>
        </dependency>
    </dependencies>
```


### 在 @SpringBootApplication 中增加 scanBasePackages 扫描 com.lealone.plugins.spring 的组件

```java
@SpringBootApplication(scanBasePackages = {
        "com.lealone.plugins.spring"})
public class SpringDemo {
    public static void main(String[] args) { 
        SpringApplication.run(SpringDemo.class, args);
    }
}
```

### 创建 CrudController

```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrudController {

    private static final ThreadLocal<CrudService> crudServiceThreadLocal = ThreadLocal
            .withInitial(() -> new CrudService());

    private static CrudService getCrudService() {
        return crudServiceThreadLocal.get();
    }

    // 打开url: http://localhost:8080/insert?name=zhh&age=18
    @GetMapping("/insert")
    public String insert(@RequestParam(value = "name", defaultValue = "zhh") String name,
            @RequestParam("age") int age) {
        return getCrudService().insert(name, age);
    }

    // 打开url: http://localhost:8080/find?name=zhh
    @GetMapping("/find")
    public String find(@RequestParam("name") String name) {
        return getCrudService().find(name);
    }
}
```

### 通过嵌入模式访问 lealone 数据库

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CrudService {

    private static volatile boolean created;

    private static synchronized void createTable(Connection conn) throws SQLException {
        if (created)
            return;
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS user");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS user (name varchar primary key, age int)");
        stmt.close();
        created = true;
    }

    private static Connection getConnection() throws SQLException {
        // 通过嵌入模式访问lealone数据库
        return DriverManager.getConnection("jdbc:lealone:embed:lealone", "root", "");
    }

    private final Connection conn;
    private final PreparedStatement insert;
    private final PreparedStatement find;

    public CrudService() {
        try {
            conn = getConnection();
            createTable(conn);
            insert = conn.prepareStatement("INSERT INTO user(name, age) VALUES(?, ?)");
            find = conn.prepareStatement("SELECT age FROM user WHERE name = ?");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String insert(String name, int age) {
        try {
            insert.setString(1, name);
            insert.setInt(2, age);
            insert.executeUpdate();
            return "insert ok";
        } catch (SQLException e) {
            return "insert exception: " + e.getMessage();
        }
    }

    public String find(String name) {
        try {
            find.setString(1, name);
            ResultSet rs = find.executeQuery();
            try {
                if (rs.next()) {
                    return "user(name: " + name + ", age: " + rs.getInt(1) + ")";
                } else {
                    return "user not found: " + name;
                }
            } finally {
                rs.close();
            }
        } catch (SQLException e) {
            return "exception: " + e.getMessage();
        }
    }
}
```

### 运行

mvn spring-boot:run
