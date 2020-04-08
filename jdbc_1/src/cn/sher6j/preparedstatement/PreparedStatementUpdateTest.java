package cn.sher6j.preparedstatement;

import cn.sher6j.connection.ConnectionTest;
import cn.sher6j.util.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 使用PreParedStatement替换Statement，实现对数据表的增删改查操作
 *
 * 增删改，查
 * 有无返回集
 *
 * @author sher6j
 * @create 2020-04-06-21:12
 */
public class PreparedStatementUpdateTest {

    //向customer表中添加一条记录
    @Test
    public void testInsert() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
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
            conn = DriverManager.getConnection(url, user, password);
//        System.out.println(conn);

            //4. 预编译SQL语句，返回PreparedStatement实例
            String sql = "insert into customers(name,email,birth)values(?,?,?)";//?占位符
            ps = conn.prepareStatement(sql);
            //5. 填充占位符
            ps.setString(1,"王鸥");
            ps.setString(2,"wo@163.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse("2002-02-23");
            ps.setDate(3, new java.sql.Date(date.getTime()));

            //6. 执行操作
            ps.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            //7. 资源的关闭
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


    }

    //修改customers表的一条记录
    @Test
    public void testUpdate() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1. 获取数据库的链接
            conn = JDBCUtils.getConnection();
            //2. 预编译SQL语句，返回PreparedStatement的实例
            String sql = "update customers set name = ? where id = ?";
            ps = conn.prepareStatement(sql);
            //3. 填充占位符
            ps.setObject(1, "莫扎特");
            ps.setObject(2, 18);
            //4. 执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5. 关闭资源
            JDBCUtils.closeResource(conn, ps);
        }

    }


    //通用的增删改操作
    //SQL中占位符的个数应该与可变形参的长度一致
    public void update(String sql, Object ...args) { //占位符数量未知--可变形参
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1. 获取数据库链接
            conn = JDBCUtils.getConnection();
            //2. 预编译SQL语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //3. 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);//参数index注意
            }
            //4. 执行操作
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5. 关闭资源
            JDBCUtils.closeResource(conn, ps);
        }
    }

    @Test
    public void testCommonUpdate() {
//        String sql = "delete from customers where id = ?";
//        update(sql, 19);

        //order是关键字，所以表明如果是关键字可能会报错，所以写`order`
        String sql = "update `order` set order_name = ? where order_id = ?";
        update(sql, "DD",2);
    }

}
