package cn.sher6j.exer;

import cn.sher6j.bean.Student;
import cn.sher6j.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * 课后练习二问题2
 * @author sher6j
 * @create 2020-04-07-17:52
 */
public class Exer2Test2 {
    //问题2：根据身份证号或准考证号查询学生成绩信息
    public static void main(String[] args) {
        System.out.println("请选择您要输入的类型：");
        System.out.println("a.准考证号");
        System.out.println("b.身份证号");
        Scanner scanner = new Scanner(System.in);
        String selection = scanner.next();
        if ("a".equalsIgnoreCase(selection)) {
            System.out.println("请输入准考证号：");
            String examCard = scanner.next();
            String sql = "select FlowID flowID, Type type, IDCard, ExamCard examCard, StudentName name, Location location, Grade grade from examstudent where examCard = ? ";
            Student student = getInstance(Student.class, sql, examCard);
            if (student != null) {
                System.out.println(student);
            } else {
                System.out.println("输入准考证号有误");
            }
        } else if ("b".equalsIgnoreCase(selection)) {
            System.out.println("请输入身份证号：");
            String IDCard = scanner.next();
            String sql = "select FlowID flowID, Type type, IDCard, ExamCard examCard, StudentName name, Location location, Grade grade from examstudent where IDCard = ? ";
            Student student = getInstance(Student.class, sql, IDCard);
            if (student != null) {
                System.out.println(student);
            } else {
                System.out.println("输入身份证号有误");
            }
        } else {
            System.out.println("您的输入有误，请重新进入程序");
        }
    }

    /**
     * @Description 针对不同的表的通用查询操作，返回表中的一条记录
     * @param aClass
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<T> aClass, String sql, Object ...args) {
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
}
