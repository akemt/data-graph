package com.hiekn.plantdata.util;

import com.hiekn.plantdata.Entity.SqlConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtils {

    /**
     * jdbc方式获取Connection
     * @param sqlConfig
     * @return
     */
    public static Connection getConn(SqlConfig sqlConfig, String driver) {
        String url = sqlConfig.getJdbcurl();
        String username = sqlConfig.getUsername();
        String password = sqlConfig.getPassword();
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
