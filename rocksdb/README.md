# lealone-rocksdb

RocksDB 存储引擎插件


## 编译需要

* jdk 1.8+
* maven 3.8+


## 打包插件

运行 `mvn clean package -Dmaven.test.skip=true`

生成 jar 包 `target\lealone-rocksdb-plugin-6.0.1.jar`

假设 jar 包的绝对路径是 `E:\lealone\lealone-plugins\rocksdb\target\lealone-rocksdb-plugin-6.0.1.jar`


## 创建插件

先参考[ lealone 快速入门](https://github.com/lealone/Lealone-Docs/blob/master/应用文档/Lealone数据库快速入门.md) 启动 lealone 数据库并打开一个命令行客户端

然后执行以下命令创建插件：

```sql
create plugin rocksdb
  implement by 'com.lealone.plugins.rocksdb.RocksdbStorageEngine' 
  class path 'E:\lealone\lealone-plugins\rocksdb\target\lealone-rocksdb-plugin-6.0.1.jar';
```

要 drop 插件可以执行以下命令：

```sql
drop plugin rocksdb;
```

执行 drop plugin 会把插件占用的内存资源都释放掉


## 使用插件

在执行 create table 语句创建新的表时，可以为表指定专用的存储引擎：

```sql
--为表 t1 指定专用的 rocksdb 存储引擎
create table t1(f1 int, f2 int) engine = rocksdb;

insert into t1 values(1, 2);

select * from t1;
