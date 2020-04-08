package cn.sher6j.daonewversion;

import cn.sher6j.util.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO：data(base) access object
 * 封装了针对于数据表的通用的操作
 * @author sher6j
 * @create 2020-04-08-17:14
 */
public abstract class BaseDAO<T> {

    private Class<T> aClass = null;

//    public BaseDAO() {
//
//    }

    {
        //this指的是BaseDAO的子类对象
        //获取当前BaseDAO的子类继承的父类中的泛型
        Type genericSuperclass = this.getClass().getGenericSuperclass();//获取带泛型的父类
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] typeArguments = parameterizedType.getActualTypeArguments();//获取父类的泛型参数
        aClass = (Class<T>) typeArguments[0];//泛型的第一个参数
    }

    /**
     * @param conn
     * @param sql
     * @param args
     * @return
     */
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

    /**
     *
     * @param conn
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    //考虑事务的通用查询操作,用于返回数据表中的一条记录
    public T getInstance(Connection conn, String sql, Object ...args) {
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


    /**
     *
     * @param conn
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    //考虑事务的通用查询操作,用于返回数据表中的多条记录构成的集合
    public List<T> getForList(Connection conn, String sql, Object ...args) {
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
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }

    /**
     * 用于查询特殊值的通用方法
     * @param conn 数据库链接
     * @param sql SQL语句
     * @param args 占位符的参数列表
     * @param <T>
     * @return
     */
    public <T> T getValue(Connection conn, String sql, Object ...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();

            if (rs.next()) {
                return (T) rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }
}
