# lealone-javascript

在 Lealone 中使用 JavaScript 语言开发微服务应用或编写用户自定义函数


## 编译需要

* jdk 1.8+
* maven 3.8+


## 打包插件

运行 `mvn clean package -Dmaven.test.skip=true`

生成 jar 包 `target/lealone-javascript-plugin-6.0.1.jar`

假设 jar 包的绝对路径是 `E:/lealone/lealone-plugins/javascript/target/lealone-javascript-plugin-6.0.1.jar`


## 创建插件

先参考[ lealone 快速入门](https://github.com/lealone/Lealone-Docs/blob/master/应用文档/Lealone数据库快速入门.md) 启动 lealone 数据库并打开一个命令行客户端

然后执行以下命令创建插件：

```sql
create plugin js
  implement by 'com.lealone.plugins.js.JavaScriptServiceExecutorFactory' 
  class path 'E:/lealone/lealone-plugins/javascript/target/lealone-javascript-plugin-6.0.1.jar';
```

要 drop 插件可以执行以下命令：

```sql
drop plugin js;
```

执行 drop plugin 会把插件占用的内存资源都释放掉



## 使用 JavaScript 开发微服务应用

E:/lealone/lealone-plugins/javascript/src/test/resources/js/hello_service.js

```JavaScript
function hello(name) {
    return "hello " + name;
}
```


## 创建服务

执行以下 SQL 创建 js_hello_service

```sql
create service if not exists js_hello_service (
  hello(name varchar) varchar
)
language 'js' implement by 'E:/lealone/lealone-plugins/javascript/src/test/resources/js/hello_service.js';
```

也可以用以下简化版本，无需声明服务的方法，会自动调用 js 文件里定义的方法

```sql
create service if not exists js_hello_service
language 'js' implement by 'E:/lealone/lealone-plugins/javascript/src/test/resources/js/hello_service.js';
```


## 调用服务

执行以下 SQL 就可以直接调用 js_hello_service 了

```sql
sql> execute service js_hello_service hello('test');
+-------------------------+
| 'JS_HELLO_SERVICE.HELLO()' |
+-------------------------+
| hello test              |
+-------------------------+
(1 row, 2 ms)

sql>
```

