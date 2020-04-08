package cn.sher6j.exer;

import java.util.Scanner;

/**
 * 课后练习2
 * @author sher6j
 * @create 2020-04-07-17:40
 */
public class Exer2Test {
    //问题1：向examstudent表中添加一条记录
    /*
    Type:
    IDCard:
    ExamCard:
    StudentName:
    Location:
    Grade:
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("四级/六级:");
        int type = scanner.nextInt();
        System.out.println("身份证号：");
        String IDCard = scanner.next();
        System.out.println("准考证号：");
        String examCard = scanner.next();
        System.out.println("学生姓名：");
        String studentName = scanner.next();
        System.out.println("所在城市：");
        String location = scanner.next();
        System.out.println("考试成绩：");
        int grade = scanner.nextInt();

        String sql = "insert into examstudent (type,IDCard,examCard,studentName,location,grade)values(?,?,?,?,?,?)";

        int insertcount = Exer1Test.update(sql, type, IDCard, examCard, studentName, location, grade);

        if (insertcount > 0) {
            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }

    }
}
