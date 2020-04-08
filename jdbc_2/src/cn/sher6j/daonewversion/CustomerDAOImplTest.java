package cn.sher6j.daonewversion;

import cn.sher6j.bean.Customer;
import cn.sher6j.daonewversion.CustomerDAOImpl;
import cn.sher6j.utilnew.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author sher6j
 * @create 2020-04-08-19:31
 */
public class CustomerDAOImplTest {

    private CustomerDAOImpl dao = new CustomerDAOImpl();

    @Test
    public void insert() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            //id是啥不影响，数据库中自增长
            Customer cust1 = new Customer(1, "朴智妍", "pzy@gmail.com", new Date(34535624562456L));
            dao.insert(conn, cust1);
            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void deleteById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            dao.deleteById(conn, 6);
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void update() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer cust = new Customer(22, "朴孝敏", "xm@163.com", new Date(4523455426L));
            dao.update(conn, cust);
            System.out.println("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void getCustomerByID() {
        Connection conn = null;
        try {
//            conn = JDBCUtils.getConnection1(); //使用C3P0连接池
//            conn = JDBCUtils.getConnection2(); //使用DBCP连接池
            conn = JDBCUtils.getConnection3(); //使用Druid连接池
            Customer customer = dao.getCustomerByID(conn, 20);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void getAll() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            List<Customer> list = dao.getAll(conn);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void getCount() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Long count = dao.getCount(conn);
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void getMaxBirth() {Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Date maxBirth = dao.getMaxBirth(conn);
            System.out.println(maxBirth);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }
}