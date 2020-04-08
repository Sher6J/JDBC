package cn.sher6j.blob;

import cn.sher6j.bean.Customer;
import cn.sher6j.util.JDBCUtils;
import org.junit.Test;

import java.io.*;
import java.sql.*;

/**
 * 测试使用PreparedStatement操作Blob类型的数据
 * @author sher6j
 * @create 2020-04-08-7:50
 */
public class BlobTest {
    //向customers表中插入blob类型的字段
    @Test
    public void testInsert() throws Exception {

        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into customers(name, email, birth, photo)values(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, "允儿");
        ps.setObject(2, "yoona@gmail.com");
        ps.setObject(3,"1989-09-08");
        FileInputStream fis = new FileInputStream(new File("D:\\Pictures\\yoona05.jpg"));
        ps.setBlob(4, fis);
        ps.execute();

        JDBCUtils.closeResource(conn, ps);
    }

    //查询数据表customers中Blob类型的字段
    @Test
    public void testQuery() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id, name, email, birth, photo from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 20);
            rs = ps.executeQuery();
            is = null;
            fos = null;
            if (rs.next()) {
                //方式一：用索引
    //            int id = rs.getInt(1);
    //            String name = rs.getString(2);
    //            String email = rs.getString(3);
    //            Date date = rs.getDate(4);
                //方式二：用列的别名 --- 推荐
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");

                Customer cust = new Customer(id, name, email, birth);
                System.out.println(cust);

                //将Blob类型的字段下载下来，以文件的方式保存到本地
                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("yoona8.jpg");
                byte[] buff = new byte[1024];
                int len;
                while ((len = is.read(buff)) != -1) {
                    fos.write(buff, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            JDBCUtils.closeResource(conn, ps, rs);
        }
    }
}
