package cn.sher6j.blob;

import cn.sher6j.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 使用PreparedStatement实现批量数据的操作
 *
 * update、delete本身就有批量操作的效果
 *
 * 此时批量操作主要指的是批量插入。使用PreparedStatement实现更高效的批量插入
 *
 * @author sher6j
 * @create 2020-04-08-8:58
 */
public class BatchInsertTest {
    //批量插入方式一
    @Test
    public void testInsert() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods (name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= 20000; i++) {
                ps.setObject(1, "name_" + i);
                ps.execute();
            }
            long end = System.currentTimeMillis();
            System.out.println("花费时间为：" + (end - start)); //花费时间为：31155
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

    //批量插入方式二
    /*
    1.addBatch()   executeBatch()   clearBatch()
    2.mysql服务器默认是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持。
      ?rewriteBatchedStatements=true 写在配置文件的url后面
    3.使用更新的mysql 驱动：mysql-connector-java-5.1.37-bin.jar
     */
    @Test
    public void testInsert2() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods (name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= 1000000; i++) {
                ps.setObject(1, "name_" + i);
                //1. "攒"sql
                ps.addBatch();
                if (i % 500 == 0) {
                    //2. 执行Batch
                    ps.executeBatch();
                    //3. 清空batch
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("花费时间为：" + (end - start)); //20000花费时间为：576
            //1000000 花费时间为：7420
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

    //批量插入的方式三:设置连接不允许自动提交数据
    @Test
    public void testInsert3() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            //设置不允许自动提交数据
            conn.setAutoCommit(false);
            String sql = "insert into goods (name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= 1000000; i++) {
                ps.setObject(1, "name_" + i);
                //1. "攒"sql
                ps.addBatch();
                if (i % 500 == 0) {
                    //2. 执行Batch
                    ps.executeBatch();
                    //3. 清空batch
                    ps.clearBatch();
                }
            }
            //提交数据
            conn.commit();
            long end = System.currentTimeMillis();
            System.out.println("花费时间为：" + (end - start)); //1000000花费时间为：4411
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

}
