/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.spring;

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
