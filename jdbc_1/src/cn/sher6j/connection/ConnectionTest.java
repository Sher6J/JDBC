package cn.sher6j.connection;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author sher6j
 * @create 2020-04-06-19:50
 */
public class ConnectionTest {

    //方式一
    @Test
    public void testConnection1() throws SQLException {
        //获取Driver的实现类对象
        Driver driver = new com.mysql.jdbc.Driver(); //出现第三方API，影响程序移植性

        //jdbc:mysql--协议
        //localhost--IP地址
        //3306--默认MySQL端口号
        //test--test数据库
        String url = "jdbc:mysql://localhost:3306/test";
        //将用户名和密码封装在Properties中
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "root");

        Connection conn = driver.connect(url, info);

        System.out.println(conn);
    }

    //方式二：对方式一的迭代
    //目的：在如下程序中不出现第三方的API，使得程序具有更好的可移植性
    @Test
    public void testConenction2() throws Exception{
        //1. 获取Driver的实现类对象，使用反射
        Class<?> aClass = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();

        //2. 提供要链接的数据库
        String url = "jdbc:mysql://localhost:3306/test";

        //3. 提供链接需要的用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "root");

        //4. 获取链接
        Connection conn = driver.connect(url, info);
        System.out.println(conn);
    }

    //方式三：使用DriverManager替换Driver
    @Test
    public void testConnection3() throws Exception {

        //1. 获取Driver实现类的对象
        Class<?> aClass = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();

        //2. 提供另外三个链接的基本信息
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "root";

        //注册驱动
        DriverManager.registerDriver(driver);

        //3. 获取链接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }

    //方式四：可以只是加载驱动，不用显示的注册驱动
    @Test
    public void testConnection4() throws Exception {

        //1. 提供另外三个链接的基本信息
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "root";

        //2. 加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        //相较于方式三，可以省略如下操作：
//        Driver driver = (Driver) aClass.newInstance();
//        //注册驱动
//        DriverManager.registerDriver(driver);
        //省略上述操作原因 -- com.mysql.jdbc.Driver源码
        /*
        static {
            try {
                DriverManager.registerDriver(new Driver());
            } catch (SQLException var1) {
                throw new RuntimeException("Can't register driver!");
            }
        }
         */

        //3. 获取链接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }

    //方式五：将数据库链接需要的4个基本信息声明在配置文件中，通过读取配置文件的方式，获取链接（最终版）
    /*
    此种方式好处？
    1. 实现了数据与代码的分离，实现了解耦
    2. 如果需要修改配置文件信息，可以避免程序重新打包
     */
    @Test
    public void testConnection5() throws Exception {
        //1. 读取配置文件中的4个基本信息
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

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
        System.out.println(conn);


    }
}
