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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Schoolwork7ApplicationTests {

	@Autowired
	private StudentService studentService;

	@BeforeEach
	void setUp() {
		studentService.clearAll();
	}

	@AfterEach
	void tearDown() {
		studentService.clearAll();
	}

	@Test
	void contextLoads() {
		assertNotNull(studentService);
	}

	@Test
	void testAddStudent() {
		// 测试添加学生
		Student student = new Student(null, "张三", "13800138000");
		Student savedStudent = studentService.addStudent(student);
		
		assertNotNull(savedStudent.getSid());
		assertEquals("张三", savedStudent.getName());
		assertEquals("13800138000", savedStudent.getTele());
		
		List<Student> students = studentService.findAll();
		assertEquals(1, students.size());
	}

	@Test
	void testAddStudentWithInvalidData() {
		// 测试添加空学生
		assertThrows(InvalidStudentDataException.class, () -> {
			studentService.addStudent(null);
		});
		
		// 测试添加姓名为空的学生
		assertThrows(InvalidStudentDataException.class, () -> {
			studentService.addStudent(new Student(null, "", "13800138000"));
		});
		
		// 测试添加电话为空的学生
		assertThrows(InvalidStudentDataException.class, () -> {
			studentService.addStudent(new Student(null, "张三", ""));
		});
	}

	@Test
	void testFindStudent() {
		// 先添加学生
		Student student = new Student(null, "李四", "13900139000");
		Student savedStudent = studentService.addStudent(student);
		
		// 测试查询学生
		Student foundStudent = studentService.findStudent(savedStudent.getSid());
		assertNotNull(foundStudent);
		assertEquals("李四", foundStudent.getName());
		assertEquals("13900139000", foundStudent.getTele());
	}

	@Test
	void testFindStudentNotFound() {
		// 测试查询不存在的学生
		assertThrows(StudentNotFoundException.class, () -> {
			studentService.findStudent(999L);
		});
	}

	@Test
	void testModifyStudent() {
		// 先添加学生
		Student student = new Student(null, "王五", "13700137000");
		Student savedStudent = studentService.addStudent(student);
		
		// 测试修改学生信息
		savedStudent.setName("王五五");
		savedStudent.setTele("13700137001");
		Student modifiedStudent = studentService.modifyStudent(savedStudent);
		
		assertEquals("王五五", modifiedStudent.getName());
		assertEquals("13700137001", modifiedStudent.getTele());
		
		// 验证修改是否持久化
		Student foundStudent = studentService.findStudent(savedStudent.getSid());
		assertEquals("王五五", foundStudent.getName());
		assertEquals("13700137001", foundStudent.getTele());
	}

	@Test
	void testModifyStudentNotFound() {
		// 测试修改不存在的学生
		Student student = new Student(999L, "赵六", "13600136000");
		assertThrows(StudentNotFoundException.class, () -> {
			studentService.modifyStudent(student);
		});
	}

	@Test
	void testModifyStudentWithInvalidData() {
		// 先添加学生
		Student student = new Student(null, "孙七", "13500135000");
		Student savedStudent = studentService.addStudent(student);
		
		// 测试修改为空姓名
		savedStudent.setName("");
		assertThrows(InvalidStudentDataException.class, () -> {
			studentService.modifyStudent(savedStudent);
		});
	}

	@Test
	void testDeleteStudent() {
		// 先添加学生
		Student student = new Student(null, "周八", "13400134000");
		Student savedStudent = studentService.addStudent(student);
		
		// 测试删除学生
		studentService.deleteStudent(savedStudent.getSid());
		
		// 验证学生已被删除
		List<Student> students = studentService.findAll();
		assertEquals(0, students.size());
		
		// 验证查询已删除的学生会抛出异常
		assertThrows(StudentNotFoundException.class, () -> {
			studentService.findStudent(savedStudent.getSid());
		});
	}

	@Test
	void testDeleteStudentNotFound() {
		// 测试删除不存在的学生
		assertThrows(StudentNotFoundException.class, () -> {
			studentService.deleteStudent(999L);
		});
	}

	@Test
	void testDeleteStudentWithNullId() {
		// 测试删除空学号的学生
		assertThrows(InvalidStudentDataException.class, () -> {
			studentService.deleteStudent(null);
		});
	}

	@Test
	void testFindAll() {
		// 添加多个学生
		studentService.addStudent(new Student(null, "学生1", "13100131000"));
		studentService.addStudent(new Student(null, "学生2", "13200132000"));
		studentService.addStudent(new Student(null, "学生3", "13300133000"));
		
		// 测试查询所有学生
		List<Student> students = studentService.findAll();
		assertEquals(3, students.size());
	}

	@Test
	void testCRUDOperations() {
		// 综合测试增删改查操作
		
		// 1. 添加学生
		Student student1 = studentService.addStudent(new Student(null, "测试1", "11111111111"));
		Student student2 = studentService.addStudent(new Student(null, "测试2", "22222222222"));
		assertEquals(2, studentService.findAll().size());
		
		// 2. 查询学生
		Student found = studentService.findStudent(student1.getSid());
		assertEquals("测试1", found.getName());
		
		// 3. 修改学生
		student1.setName("测试1修改");
		student1.setTele("11111111110");
		studentService.modifyStudent(student1);
		Student modified = studentService.findStudent(student1.getSid());
		assertEquals("测试1修改", modified.getName());
		assertEquals("11111111110", modified.getTele());
		
		// 4. 删除学生
		studentService.deleteStudent(student2.getSid());
		assertEquals(1, studentService.findAll().size());
		assertThrows(StudentNotFoundException.class, () -> {
			studentService.findStudent(student2.getSid());
		});
	}
}

