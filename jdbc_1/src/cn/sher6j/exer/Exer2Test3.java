package cn.sher6j.exer;

import cn.sher6j.bean.Student;

import java.util.Scanner;

/**
 * 练习二问题3
 * @author sher6j
 * @create 2020-04-07-18:15
 */
public class Exer2Test3 {
    //完成学习信息的删除功能
    /*
    可以不用查询，直接Update
    if update > 0  ----  删除成功
    else --- 查无此人
     */
    public static void main(String[] args) {
        System.out.println("请输入学生的考号：");
        Scanner scanner = new Scanner(System.in);
        String examCard = scanner.next();
        //查询指定准考证号的学生
        String sql1 = "select FlowID flowID, Type type, IDCard, ExamCard examCard, StudentName name, Location location, Grade grade from examstudent where examCard = ? ";
        Student student = Exer2Test2.getInstance(Student.class, sql1, examCard);
        if (student == null) {
            System.out.println("查无此人，请重新输入");
        } else {
            String sql2 = "delete from examstudent where examCard = ?";
            int deleteCount = Exer1Test.update(sql2, examCard);
            if (deleteCount > 0) {
                System.out.println("删除成功");
            }
        }
    }
}
