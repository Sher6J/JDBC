package cn.sher6j.connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author sher6j
 * @create 2020-04-08-20:21
 */
public class DBCPTest {
    /**
     * 测试DBCP的数据库连接池技术
     */

    //方式一：硬编码，依旧不推荐
    @Test
    public void testGetConnection() throws SQLException {
        //创建DBCP的数据库连接池
        BasicDataSource source = new BasicDataSource();
        //设置基本信息
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql:///test");
        source.setUsername("root");
        source.setPassword("root");
        //还可以设置其他涉及数据库连接池相关管理的属性
        source.setInitialSize(10);
        source.setMaxActive(10);
        //......
        Connection conn = source.getConnection();
        System.out.println(conn);
    }

    //方式二：配置文件
    @Test
    public void testGetConnection1() throws Exception {
        Properties pros = new Properties();
        //方式1：
//        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        //方式2：
        FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
        pros.load(is);

        DataSource source = BasicDataSourceFactory.createDataSource(pros);
        Connection conn = source.getConnection();
        System.out.println(conn);
    }
}
