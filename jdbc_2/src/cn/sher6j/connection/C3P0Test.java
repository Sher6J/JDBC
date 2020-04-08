package cn.sher6j.connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author sher6j
 * @create 2020-04-08-19:42
 */
public class C3P0Test {
    //方式一：配置信息写到代码中（硬编码），不提倡
    @Test
    public void testGetConnection() throws Exception {
        /*
        查看官方文档，下面5行代码获取C3P0连接池
         */
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test" );
        cpds.setUser("root");
        cpds.setPassword("root");

        /*
        通过设置相关参数对数据库连接池进行管理
         */
        //设置初始时数据库连接池中的连接数
        cpds.setInitialPoolSize(10);

        Connection conn = cpds.getConnection();
        System.out.println(conn);

        //销毁连接池，一般不会执行此操作
//        DataSources.destroy(cpds);
    }

    //方式二：使用配置文件
    @Test
    public void testGetConnection1() throws SQLException {
        ComboPooledDataSource cpds = new ComboPooledDataSource("intergalactoApp");
        Connection conn = cpds.getConnection();
        System.out.println(conn);

    }
}
