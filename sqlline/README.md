# lealone-sqlline

基于 SQLLine 开发的带彩色的 Lealone 命令行客户端


## 编译需要

* jdk 1.8+
* maven 3.8+


## 打包客户端

运行 `mvn clean package`

生成 jar 包 `target/lealone-sqlline-client-6.0.1.jar`


## 运行客户端

打开一个命令行客户端，运行 `java -jar lealone-sqlline-client-6.0.1.jar`

```sql
Lealone version: 5.2.0
lealone> CREATE TABLE IF NOT EXISTS test (f1 int primary key, f2 long);
No rows affected (0.004 seconds)
lealone> INSERT INTO test(f1, f2) VALUES(1, 2);
1 row affected (0.003 seconds)
lealone> SELECT * FROM test;
+----+----+
| F1 | F2 |
+----+----+
| 1  | 2  |
+----+----+
1 row selected (0.014 seconds)
lealone> !quit
Closing: com.lealone.client.jdbc.JdbcConnection
```


