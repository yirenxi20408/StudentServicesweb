package cn.edu.hzcu.yrx.demo;

import cn.edu.hzcu.yrx.demo.exception.InvalidStudentDataException;
import cn.edu.hzcu.yrx.demo.exception.StudentNotFoundException;
import cn.edu.hzcu.yrx.demo.model.Student;
import cn.edu.hzcu.yrx.demo.service.StudentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 学生服务演示测试类
 * 展示IoC容器注入和AOP日志记录功能
 */
@SpringBootTest
class StudentServiceDemoTest {

    @Autowired  // 使用IoC容器自动注入StudentService
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        System.out.println("\n========== 开始测试 ==========");
        studentService.clearAll();
    }

    @AfterEach
    void tearDown() {
        studentService.clearAll();
        System.out.println("========== 测试结束 ==========\n");
    }

    @Test
    void testIoCAndAOP_AddStudent() {
        System.out.println("\n--- 测试1: 添加学生 (addStudent) ---");
        System.out.println("IoC说明: StudentService已通过@Autowired自动注入");
        System.out.println("AOP说明: 下面将看到方法调用的日志输出");
        
        Student student = new Student(null, "张三", "13800138000");
        Student result = studentService.addStudent(student);
        
        System.out.println("业务结果: 成功添加学生 - " + result);
    }

    @Test
    void testIoCAndAOP_DeleteStudent() {
        System.out.println("\n--- 测试2: 删除学生 (deleteStudent) ---");
        
        // 先添加一个学生
        Student student = studentService.addStudent(new Student(null, "李四", "13900139000"));
        System.out.println("已添加学生: " + student);
        
        // 删除学生
        System.out.println("\n开始删除学生...");
        studentService.deleteStudent(student.getSid());
        System.out.println("业务结果: 成功删除学生");
    }

    @Test
    void testIoCAndAOP_ModifyStudent() {
        System.out.println("\n--- 测试3: 修改学生 (modifyStudent) ---");
        
        // 先添加一个学生
        Student student = studentService.addStudent(new Student(null, "王五", "13700137000"));
        System.out.println("原始学生: " + student);
        
        // 修改学生信息
        System.out.println("\n开始修改学生信息...");
        student.setName("王五五");
        student.setTele("13700137001");
        Student modified = studentService.modifyStudent(student);
        
        System.out.println("业务结果: 成功修改学生 - " + modified);
    }

    @Test
    void testIoCAndAOP_QueryStudent() {
        System.out.println("\n--- 测试4: 查询学生 (findStudent) ---");
        
        // 先添加一个学生
        Student student = studentService.addStudent(new Student(null, "赵六", "13600136000"));
        System.out.println("已添加学生: " + student);
        
        // 查询学生
        System.out.println("\n开始查询学生...");
        Student found = studentService.findStudent(student.getSid());
        System.out.println("业务结果: 查询到学生 - " + found);
    }

    @Test
    void testIoCAndAOP_InvalidOperation() {
        System.out.println("\n--- 测试5: 无效操作异常处理 ---");
        System.out.println("AOP说明: 异常也会被AOP拦截并记录日志");
        
        try {
            System.out.println("\n尝试添加空姓名的学生...");
            studentService.addStudent(new Student(null, "", "13800138000"));
        } catch (InvalidStudentDataException e) {
            System.out.println("捕获到自定义异常: " + e.getMessage());
        }
        
        try {
            System.out.println("\n尝试查询不存在的学生...");
            studentService.findStudent(999L);
        } catch (StudentNotFoundException e) {
            System.out.println("捕获到自定义异常: " + e.getMessage());
        }
    }

    @Test
    void testIoCAndAOP_CompleteWorkflow() {
        System.out.println("\n--- 测试6: 完整工作流程 (增删改查) ---");
        System.out.println("IoC说明: 使用Spring容器管理的StudentService实例");
        System.out.println("AOP说明: 所有方法调用都会被日志切面拦截\n");
        
        // 1. 添加学生
        System.out.println("步骤1: 添加3个学生");
        Student s1 = studentService.addStudent(new Student(null, "学生A", "11111111111"));
        Student s2 = studentService.addStudent(new Student(null, "学生B", "22222222222"));
        Student s3 = studentService.addStudent(new Student(null, "学生C", "33333333333"));
        
        // 2. 查询所有学生
        System.out.println("\n步骤2: 查询所有学生");
        System.out.println("当前学生数量: " + studentService.findAll().size());
        
        // 3. 修改学生
        System.out.println("\n步骤3: 修改学生B的信息");
        s2.setName("学生B_修改版");
        s2.setTele("22222222220");
        studentService.modifyStudent(s2);
        
        // 4. 查询单个学生
        System.out.println("\n步骤4: 查询修改后的学生B");
        Student found = studentService.findStudent(s2.getSid());
        System.out.println("修改后的学生: " + found);
        
        // 5. 删除学生
        System.out.println("\n步骤5: 删除学生A");
        studentService.deleteStudent(s1.getSid());
        System.out.println("删除后学生数量: " + studentService.findAll().size());
        
        System.out.println("\n工作流程完成！请查看上方的AOP日志输出。");
    }
}
