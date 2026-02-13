/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.service.test.orm;

import com.lealone.db.service.ServiceSetting;
import com.lealone.test.TestBase.MainTest;
import com.lealone.test.TestBase.SqlExecutor;

import com.lealone.plugins.service.test.service.ExecuteServiceTest;
import com.lealone.plugins.service.test.service.impl.AllTypeServiceImpl;
import com.lealone.plugins.service.test.service.impl.CollectionTypeServiceImpl;
import com.lealone.plugins.service.test.service.impl.HelloWorldServiceImpl;
import com.lealone.plugins.service.test.service.impl.UserServiceImpl;

public class SqlScript implements MainTest {

    private static final String MODEL_PACKAGE_NAME = AllModelPropertyTest.class.getPackage().getName()
            + ".generated";
    private static final String SERVICE_PACKAGE_NAME = ExecuteServiceTest.class.getPackage().getName()
            + ".generated";
    private static String GENERATED_CODE_PATH = "./src/test/java";

    public static void setCodePath(String path) {
        GENERATED_CODE_PATH = path;
    }

    public static void createUserTable(SqlExecutor executor) {
        System.out.println("create table: user");
        executor.execute("drop table if exists user");
        // 创建表: user
        executor.execute(
                "create table if not exists user(name char(10) primary key, notes varchar, phone int, id long, phones ARRAY)" //
                        + " package '" + MODEL_PACKAGE_NAME + "'" //
                        + " generate code '" + GENERATED_CODE_PATH + "'");
    }

    // 21种模型属性类型，目前不支持GEOMETRY类型
    // INT
    // BOOLEAN
    // TINYINT
    // SMALLINT
    // BIGINT
    // IDENTITY
    // DECIMAL
    // DOUBLE
    // REAL
    // TIME
    // DATE
    // TIMESTAMP
    // BINARY
    // OTHER
    // VARCHAR
    // VARCHAR_IGNORECASE
    // CHAR
    // BLOB
    // CLOB
    // UUID
    // ARRAY
    public static String TEST_TYPES = "" //
            + " f1  INT," //
            + " f2  BOOLEAN," //
            + " f3  TINYINT," //
            + " f4  SMALLINT," //
            + " f5  BIGINT," //
            + " f6  IDENTITY," //
            + " f7  DECIMAL," //
            + " f8  DOUBLE," //
            + " f9  REAL," //
            + " f10 TIME," //
            + " f11 DATE," //
            + " f12 TIMESTAMP," //
            + " f13 BINARY," //
            + " f14 OTHER," //
            + " f15 VARCHAR," //
            + " f16 VARCHAR_IGNORECASE," //
            + " f17 CHAR," //
            + " f18 BLOB," //
            + " f19 CLOB," //
            + " f20 UUID," //
            + " f21 ARRAY" //
    ;

    public static void createAllModelPropertyTable(SqlExecutor executor) {

        executor.execute("drop table if exists all_model_property");
        executor.execute("CREATE TABLE if not exists all_model_property (" //
                + TEST_TYPES + ")" //
                + " PACKAGE '" + MODEL_PACKAGE_NAME + "'" //
                + " GENERATE CODE '" + GENERATED_CODE_PATH + "'");

        System.out.println("create table: all_model_property");
    }

    public static void createAllTypeService(SqlExecutor executor) {
        System.out.println("create service: all_type_service");
        executor.execute("drop service if exists all_type_service");
        // 创建服务: all_type_service
        executor.execute("create service if not exists all_type_service (" //
                + " test_type(" + TEST_TYPES + ") user," //
                + " test_uuid(f1 uuid) uuid" //
                + ")" //
                + " package '" + SERVICE_PACKAGE_NAME + "'" //
                // 如果是内部类，不能用getClassName()，会包含$字符
                + " implement by '" + AllTypeServiceImpl.class.getCanonicalName() + "'" //
                + " generate code '" + GENERATED_CODE_PATH + "'");
    }

    public static void createUserService(SqlExecutor executor) {
        System.out.println("create service: user_service");
        executor.execute("drop service if exists user_service");
        // 创建服务: user_service
        executor.execute("create service if not exists user_service (" //
                + " add(user user) long," // 第一个user是参数名，第二个user是参数类型
                + " find(name varchar) user," //
                + " update(user user) int," //
                + " get_list() array," //
                + " delete(name varchar) int)" //
                + " package '" + SERVICE_PACKAGE_NAME + "'" //
                // 如果是内部类，不能用getClassName()，会包含$字符
                + " implement by '" + UserServiceImpl.class.getCanonicalName() + "'" //
                + " generate code '" + GENERATED_CODE_PATH + "'");
    }

    public static void createHelloWorldService(SqlExecutor executor) {
        System.out.println("create service: hello_world_service");
        executor.execute("drop service if exists hello_world_service");
        // 创建服务: hello_world_service
        executor.execute("create service hello_world_service (" //
                + "             say_hello() void," //
                + "             get_date() date," //
                + "             get_int() int," //
                + "             get_two(name varchar, age int) int," //
                + "             say_goodbye_to(name varchar) varchar" //
                + "         ) package '" + SERVICE_PACKAGE_NAME + "'" //
                + "           implement by '" + HelloWorldServiceImpl.class.getName() + "'" //
                + "           generate code '" + GENERATED_CODE_PATH + "'" //
                + "           parameters(" + ServiceSetting.CREATE_METHOD_NAME + "='_create')");
    }

    public static void createCollectionTypeService(SqlExecutor executor) {
        System.out.println("create service: collection_type_service");
        executor.execute("drop service if exists collection_type_service");
        executor.execute("create service collection_type_service (" //
                + "             m1() list," //
                + "             m2() list<int>," //
                + "             m3() set," //
                + "             m4() set<varchar>," //
                + "             m5() map," //
                + "             m6() map<int, varchar>," //
                + "             m7(p1 list<int>, p2 set<varchar>, p3 map<int, varchar>, p4 int)"
                + "                  map<int, varchar>" //
                + "         ) package '" + SERVICE_PACKAGE_NAME + "'" //
                + "           implement by '" + CollectionTypeServiceImpl.class.getName() + "'" //
                + " generate code '" + GENERATED_CODE_PATH + "'");
    }
}
