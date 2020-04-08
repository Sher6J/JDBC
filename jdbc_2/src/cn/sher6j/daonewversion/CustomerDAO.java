package cn.sher6j.daonewversion;

import cn.sher6j.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * 此接口用于规范针对于Customers表的常用操作
 * @author sher6j
 * @create 2020-04-08-17:28
 */
public interface CustomerDAO {

    /**
     * @Description 将cust对象添加到数据库中
     * @param conn
     * @param cust
     */
    void insert(Connection conn, Customer cust);

    /**
     * 根据指定ID，删除表中的一条记录
     * @param conn
     * @param id
     */
    void deleteById(Connection conn, int id);

    /**
     * 针对内存中的cust对象，修改数据表中指定的记录
     * @param conn
     * @param cust
     */
    void update(Connection conn, Customer cust);

    /**
     * 针对指定的ID查询对应的Customer对象
     * @param conn
     * @param id
     */
    Customer getCustomerByID(Connection conn, int id);

    /**
     * 查询表中的所有记录构成的集合
     * @param conn
     * @return
     */
    List<Customer> getAll(Connection conn);

    /**
     * 返回数据表中数据的条目数
     * @param conn
     * @return
     */
    Long getCount(Connection conn);

    /**
     * 返回数据表中最大的生日
     * @param conn
     * @return
     */
    Date getMaxBirth(Connection conn);
}
