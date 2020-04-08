package cn.sher6j.transaction;

import cn.sher6j.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;

/**
 * @author sher6j
 * @create 2020-04-08-11:32
 */
public class ConnectionTest {
    @Test
    public void testGetConnection() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        System.out.println(conn);
    }
}
