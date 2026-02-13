# lealone-tomcat

使用 Tomcat 插件部署应用


## 编译需要

* jdk 1.8+
* maven 3.8+


## 打包插件

运行 `mvn clean package -Dmaven.test.skip=true`

生成 jar 包 `target\lealone-tomcat-plugin-6.0.1.jar`

假设 jar 包的绝对路径是 `E:\lealone\lealone-plugins\tomcat\target\lealone-tomcat-plugin-6.0.1.jar`


## 运行插件

先参考[ lealone 快速入门](https://github.com/lealone/Lealone-Docs/blob/master/应用文档/Lealone数据库快速入门.md) 启动 lealone 数据库并打开一个命令行客户端

然后执行以下命令创建并启动插件：

```sql
create plugin tomcat
  implement by 'com.lealone.plugins.tomcat.TomcatServerEngine' 
  class path 'E:\lealone\lealone-plugins\tomcat\target\lealone-tomcat-plugin-6.0.1.jar'
  parameters (port=8080,web_root='E:\lealone\lealone-plugins\tomcat\src\test\resources\web');
 
start plugin tomcat;
```

要 stop 和 drop 插件可以执行以下命令：

```sql
stop plugin tomcat;

drop plugin tomcat;
```

执行 stop plugin 只是把插件对应的服务停掉，可以再次通过执行 start plugin 启动插件

执行 drop plugin 会把插件占用的内存资源都释放掉，需要再次执行 create plugin 才能重新启动插件


## 打开以下 URL 测试

http://localhost:8080/


