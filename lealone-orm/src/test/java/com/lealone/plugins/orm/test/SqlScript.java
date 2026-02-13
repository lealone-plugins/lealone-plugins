/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.orm.test;

import com.lealone.test.TestBase.MainTest;
import com.lealone.test.TestBase.SqlExecutor;

public class SqlScript implements MainTest {

    public static void main(String[] args) {
        new SqlScriptTest().runTest();
    }

    private static class SqlScriptTest extends OrmTestBase {
        @Override
        public void test() {
            createTables(this);
        }
    }

    private static final String MODEL_PACKAGE_NAME = OrmTest.class.getPackage().getName() + ".generated";
    private static String GENERATED_CODE_PATH = "./src/test/java";

    public static void setCodePath(String path) {
        GENERATED_CODE_PATH = path;
    }

    public static void createTables(SqlExecutor executor) {
        createUserTable(executor);
        createCustomerTable(executor);
        createCustomerAddressTable(executor);
        createProductTable(executor);
        createOrderTable(executor);
        createOrderItemTable(executor);
        createAllModelPropertyTable(executor);
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

    public static void createCustomerTable(SqlExecutor executor) {
        System.out.println("create table: customer");

        executor.execute("drop table if exists customer");
        executor.execute(
                "create table if not exists customer(id long primary key, name char(10), notes varchar, phone int)" //
                        + " package '" + MODEL_PACKAGE_NAME + "'" //
                        + " generate code '" + GENERATED_CODE_PATH + "'" // 生成领域模型类和查询器类的代码
        );
    }

    public static void createCustomerAddressTable(SqlExecutor executor) {
        System.out.println("create table: customer_address");

        executor.execute("drop table if exists customer_address");
        executor.execute(
                "create table if not exists customer_address(customer_id long, city varchar, street varchar, "
                        + " FOREIGN KEY(customer_id) REFERENCES customer(id))" //
                        + " package '" + MODEL_PACKAGE_NAME + "'" //
                        + " generate code '" + GENERATED_CODE_PATH + "'" //
        );
    }

    public static void createProductTable(SqlExecutor executor) {
        System.out.println("create table: product");

        executor.execute("drop table if exists product");
        executor.execute(
                "create table if not exists product(product_id long primary key, product_name varchar, "
                        + " category varchar, unit_price double)" //
                        + " package '" + MODEL_PACKAGE_NAME + "'" //
                        + " generate code '" + GENERATED_CODE_PATH + "'" // 生成领域模型类和查询器类的代码
        );
    }

    public static void createOrderTable(SqlExecutor executor) {
        System.out.println("create table: order");

        executor.execute("drop table if exists `order`");
        // order是关键字，所以要用特殊方式表式
        executor.execute(
                "create table if not exists `order`(customer_id long, order_id int primary key, order_date date, total double,"
                        + " FOREIGN KEY(customer_id) REFERENCES customer(id))" //
                        + " package '" + MODEL_PACKAGE_NAME + "'" //
                        + " generate code '" + GENERATED_CODE_PATH + "'" // 生成领域模型类和查询器类的代码
        );
    }

    public static void createOrderItemTable(SqlExecutor executor) {
        System.out.println("create table: order_item");

        executor.execute("drop table if exists order_item");
        executor.execute(
                "create table if not exists order_item(order_id int, product_id long, product_count int, "
                        + " FOREIGN KEY(order_id) REFERENCES `order`(order_id)," //
                        + " FOREIGN KEY(product_id) REFERENCES product(product_id))" //
                        + " package '" + MODEL_PACKAGE_NAME + "'" //
                        + " generate code '" + GENERATED_CODE_PATH + "'" // 生成领域模型类和查询器类的代码
        );
    }

    public static void createJsonTestTable(SqlExecutor executor) {
        System.out.println("create table: json_test_table");

        executor.execute("drop table if exists json_test_table");
        executor.execute(
                "create table if not exists json_test_table(property_name1 int, property_name2 long, b boolean)" //
                        + " parameters(json_format='lower_underscore_format')" //
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

    public static void createCollectionPropertyTable(SqlExecutor executor) {

        executor.execute("drop table if exists collection_property");
        executor.execute("CREATE TABLE if not exists collection_property (" //
                + " f1  list," //
                + " f2  list<int>," //
                + " f3  set," //
                + " f4  set<varchar>," //
                + " f5  map," //
                + " f6  map<int, varchar>" //
                + ")" //
                + " PACKAGE '" + MODEL_PACKAGE_NAME + "'" //
                + " GENERATE CODE '" + GENERATED_CODE_PATH + "'");

        System.out.println("create table: collection_property");
    }
}
