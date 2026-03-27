# Lealone FullText Search

既可以使用内置的全文检索功能，也可以使用 Apache Lucene 实现全文检索。


## 编译需要

* jdk 1.8+
* maven 3.8+


## 打包插件

运行 `mvn clean package -Dmaven.test.skip=true`

生成 jar 包 `target\lealone-fulltext-plugin-6.0.1.jar`


## 使用插件

把 lealone-fulltext-plugin-6.0.1.jar 加到类路径

全文检索功能的用法跟 h2 数据库一样，可以参考文档: https://www.h2database.com/html/tutorial.html#fulltext

把文档中的 org.h2.fulltext.FullText 替换成 com.lealone.plugins.fulltext.FullText 即可
