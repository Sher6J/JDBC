package cn.sher6j.preparedstatement;

import cn.sher6j.bean.Order;
import cn.sher6j.util.JDBCUtils;
import com.mysql.jdbc.integration.jboss.ExtendedMysqlExceptionSorter;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * 针对于Order表的通用查询操作
 * @author sher6j
 * @create 2020-04-07-9:13
 */
public class OrderForQuery {

    @Test
    public void testQuery1() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select order_id,order_name,order_date from `order` where order_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 1);

            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                int id = (int) resultSet.getObject(1);
                String name = (String) resultSet.getObject(2);
                Date date = resultSet.getDate(3);
                Order order = new Order(id, name, date);
                System.out.println(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, resultSet);
        }
    }

    //通用的针对于order表的查询操作
    public Order orderForQuery(String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //执行获取结果集
            rs = ps.executeQuery();
            //获取结果集元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值：通过ResultSet
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名：通过ResultSet ---- 不推荐使用
                    //获取列的列名：getColumnName()
//                    String columnName = rsmd.getColumnName(i + 1);
                    //获取列的别名：getColumnLabel()，没起别名就返回列名
                    String columnName = rsmd.getColumnLabel(i + 1);

                    //通过反射将对象指定名的属性columnName赋值为指定的值columnValue
                    Field field = Order.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(order, columnValue);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }

        return null;
    }

    /*
    针对于表的字段名与类的属性名不相同的情况
    1. 必须声明SQL时，使用类的属性名来命名字段名的别名，如下测试类
    2. 使用ResultSetMetaData时，需要使用getColumnLabel()替换getColumnName()
       获取列的别名。如上通用方法
    说明：若SQL中没有给字段起别名，则getColumnLabel()获取的就是列名
     */
    @Test
    public void tetsOrderForQuery() {
        //起别名，否则由于JavaBean和数据库表中属性名不一致，出现异常java.lang.NoSuchFieldException: order_id
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Order order = orderForQuery(sql, 1);
        System.out.println(order);
    }
}
