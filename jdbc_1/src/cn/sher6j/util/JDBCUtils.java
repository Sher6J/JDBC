package cn.sher6j.util;

import cn.sher6j.connection.ConnectionTest;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @Description 操作数据库的工具类
 * @author sher6j
 * @create 2020-04-06-21:27
 */
public class JDBCUtils {
    /**
     * 获取数据库的链接
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception{
        //1. 读取配置文件中的4个基本信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        //2. 加载驱动
        Class.forName(driverClass);

        //3. 获取链接
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    /**
     * 关闭链接和Statement的操作
     * @param conn
     * @param ps
     */
    public static void closeResource(Connection conn, PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeResource(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
