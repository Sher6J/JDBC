package cn.sher6j.transaction;

import cn.sher6j.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * 1. 什么叫数据库事务？
 * 事务：一组逻辑操作单元,使数据从一种状态变换到另一种状态。
 *   一组逻辑操作单元：一个或多个DML操作
 * 2. 事务处理的原则：要么都做，要么都不做
 * 3. 数据一旦提交，就不可回滚
 * 4. 哪些操作会导致数据的自动提交？
 *      ①DDL操作一旦执行，都会自动提交
 *          set autocommit = false对DDL没有用
 *      ②DML默认情况下，一旦执行，就会提交
 *          但是可以通过set autocommit = false的方式取消DML操作的自动提交
 *      ③默认在关闭连接时，会自动的提交数据
 *
 * @author sher6j
 * @create 2020-04-08-11:35
 */
public class TransactionTest {

    //未考虑数据库事务情况下的转账操作
    /**
     * 针对于数据表user_table来说：
     * AA用户给BB用户转账100
     *
     * update user_table set balance = balance - 100 where user = 'AA';
     * update user_table set balance = balance + 100 where user = 'BB';
     */
    @Test
    public void testUpdate() {
        String sql1 = "update user_table set balance = balance - 100 where user = ?";
        update(sql1, "AA");

        //模拟网络异常
        System.out.println(10/0);

        String sql2 = "update user_table set balance = balance + 100 where user = ?";
        update(sql2, "BB");

        System.out.println("转账成功");
    }

    //考虑数据库事务的转账操作
    @Test
    public void tetUpdateWithTransaction() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            //取消数据的自动提交功能
            conn.setAutoCommit(false); //默认情况下为true
            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            update(conn, sql1, "AA");

            //模拟网络异常
        System.out.println(10/0);

            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            update(conn, sql2, "BB");

            System.out.println("转账成功");
            //提交数据
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常回滚数据
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            //如果使用数据库连接池，记得放回连接池时修改回默认自动提交数据
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void testTransactionSelect() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        //获取当前连接的隔离级别
        System.out.println(conn.getTransactionIsolation());
        //设置数据库的隔离级别
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        //取消自动提交数据
        conn.setAutoCommit(false);
        String sql = "select user, password, balance from user_table where user = ?";
        User user = getInstance(conn, User.class, sql, "CC");

        System.out.println(user);
    }

    @Test
    public void testTransactionUpdate() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        //取消自动提交数据
        conn.setAutoCommit(false);
        String sql = "update user_table set balance = ? where user = ?";
        update(conn, sql, 5000, "CC");

        Thread.sleep(15000);
        System.out.println("修改结束");
    }


    //通用的增删改操作 --- version2.0考虑事务
    //SQL中占位符的个数应该与可变形参的长度一致
    public int update(Connection conn, String sql, Object ...args) { //占位符数量未知--可变形参
        PreparedStatement ps = null;
        try {
            //1. 没有1了
            //2. 预编译SQL语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //3. 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);//参数index注意
            }
            //4. 执行操作
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5. 关闭资源
            JDBCUtils.closeResource(null, ps);
        }
        return 0;
    }

    //考虑事务的查询操作,用于返回数据表中的一条记录
    public <T> T getInstance(Connection conn, Class<T> aClass, String sql, Object ...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i +1, args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据：ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                T t = aClass.newInstance();
                //处理结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的值
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的别名
                    String columnName = rsmd.getColumnLabel(i + 1);

                    //给t对象的指定的columnName属性，赋值为columnValue，通过反射
                    Field field = aClass.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }

    //通用的增删改操作 --- version1.0没有事务
    //SQL中占位符的个数应该与可变形参的长度一致
    public int update(String sql, Object ...args) { //占位符数量未知--可变形参
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
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5. 关闭资源
            JDBCUtils.closeResource(conn, ps);
        }
        return 0;
    }
}
