package cn.sher6j.connection;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author sher6j
 * @create 2020-04-08-20:55
 */
public class DruidTest {
    @Test
    public void getConnection() throws Exception {
//        DataSource source = null;//Ctrl + Alt + B 查看该接口的实现类
        Properties pros = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        pros.load(is);

        DataSource source = DruidDataSourceFactory.createDataSource(pros);
        Connection conn = source.getConnection();

        System.out.println(conn);


    }
}
