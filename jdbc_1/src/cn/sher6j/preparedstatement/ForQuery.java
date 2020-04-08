package cn.sher6j.preparedstatement;

import cn.sher6j.bean.Customer;
import cn.sher6j.bean.Order;
import cn.sher6j.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用PreparedStatement实现针对于不同表的通用的查询操作
 * @author sher6j
 * @create 2020-04-07-10:01
 */
public class ForQuery {

    /**
     * @Description 针对不同的表的通用查询操作，返回表中的一条记录
     * @param aClass
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> T getInstance(Class<T> aClass, String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();

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
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    @Test
    public void test1() {
        String sql1 = "select id, name, email from customers where id = ?";
        Customer customer = getInstance(Customer.class, sql1, 12);
        System.out.println(customer);

        String sql2 = "select order_id orderId, order_name orderName from `order` where order_id = ?";
        Order order = getInstance(Order.class, sql2, 2);
        System.out.println(order);
    }


    public <T> List<T> getForList(Class<T> aClass, String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();

            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i +1, args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据：ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            //创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while (rs.next()) {
                T t = aClass.newInstance();
                //处理结果集一行数据中的每一个列:给t对象指定的属性赋值
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
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }


    @Test
    public void testGetForList() {
        String sql1 = "select id, name, email from customers where id < ?";
        List<Customer> list = getForList(Customer.class, sql1, 12);
        list.forEach(System.out::println);
    }
}
