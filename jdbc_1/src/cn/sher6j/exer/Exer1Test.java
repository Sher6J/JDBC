package cn.sher6j.exer;

import cn.sher6j.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * 课后练习1
 * @author sher6j
 * @create 2020-04-07-17:25
 */
public class Exer1Test {

    //通用的增删改操作
    //SQL中占位符的个数应该与可变形参的长度一致
    public static int update(String sql, Object ...args) { //占位符数量未知--可变形参
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
            /*
            ps.execute():
            如果执行的是查询操作，有返回结果，则此方法返回true；
            如果执行的是增删改操作，没有返回结果，则此方法返回false
             */
            //方式一
//            ps.execute();
            //方式二
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5. 关闭资源
            JDBCUtils.closeResource(conn, ps);
        }
        return 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名： ");
        String name = scanner.next();
        System.out.println("请输入邮箱： ");
        String email = scanner.next();
        System.out.println("请输入生日： ");
        String birth = scanner.next();//只要是'1992-09-08'格式，数据库可以隐式转换

        String sql = "insert into customers(name,email,birth)values(?,?,?)";

        int insertCount = update(sql, name, email, birth);

        if (insertCount > 0) {
            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }
    }
}
