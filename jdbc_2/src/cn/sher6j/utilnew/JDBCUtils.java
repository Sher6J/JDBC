package cn.sher6j.utilnew;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author sher6j
 * @create 2020-04-08-20:05
 */
public class JDBCUtils {

    /**
     * 使用C3P0的数据库连接池技术
     * @return
     * @throws SQLException
     */
    //数据库连接池只需提供一个即可
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("intergalactoApp");
    public static Connection getConnection1() throws SQLException {
        Connection conn = cpds.getConnection();
        return conn;
    }

    /**
     * 使用DBCP数据库连接池技术获取数据库连接
     * @return
     * @throws Exception
     */
    private static DataSource source;
    static {
        try {
            Properties pros = new Properties();
            //方式2：
            FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
            pros.load(is);
            //创建一个DBCP数据库连接池
            source = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection2() throws Exception {
        Connection conn = source.getConnection();
        return conn;
    }


    /**
     * 使用Druid数据库连接池
     */
    private static DataSource dataSource;
    static {
        try {
            Properties pros = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            pros.load(is);
            dataSource = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection3() throws Exception {
        Connection conn = dataSource.getConnection();
        return conn;
    }

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


    /**
     * 使用dbutils.jar中提供的DbUtils工具类，实现资源的关闭
     * @param conn
     * @param ps
     * @param rs
     */
    public static void closeResource1(Connection conn, PreparedStatement ps, ResultSet rs) {
//        try {
//            DbUtils.close(conn);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        try {
//            DbUtils.close(ps);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        try {
//            DbUtils.close(rs);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        DbUtils.closeQuietly(conn);
        DbUtils.closeQuietly(ps);
        DbUtils.closeQuietly(rs);

    }



}
