package cn.edu.hzcu.yrx.demo.service;

import cn.edu.hzcu.yrx.demo.exception.InvalidStudentDataException;
import cn.edu.hzcu.yrx.demo.exception.StudentNotFoundException;
import cn.edu.hzcu.yrx.demo.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class StudentService {

    private final List<Student> students = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * 添加学生
     */
    public Student addStudent(Student student) {
        if (student == null) {
            throw new InvalidStudentDataException("学生信息不能为空");
        }
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new InvalidStudentDataException("学生姓名不能为空");
        }
        if (student.getTele() == null || student.getTele().trim().isEmpty()) {
            throw new InvalidStudentDataException("学生电话不能为空");
        }
        
        if (student.getSid() == null) {
            student.setSid(idGenerator.getAndIncrement());
        }
        students.add(student);
        return student;
    }

    /**
     * 删除学生
     */
    public void deleteStudent(Long sid) {
        if (sid == null) {
            throw new InvalidStudentDataException("学号不能为空");
        }
        
        boolean removed = students.removeIf(s -> s.getSid().equals(sid));
        if (!removed) {
            throw new StudentNotFoundException(sid);
        }
    }

    /**
     * 修改学生信息
     */
    public Student modifyStudent(Student student) {
        if (student == null || student.getSid() == null) {
            throw new InvalidStudentDataException("学生信息或学号不能为空");
        }
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new InvalidStudentDataException("学生姓名不能为空");
        }
        if (student.getTele() == null || student.getTele().trim().isEmpty()) {
            throw new InvalidStudentDataException("学生电话不能为空");
        }
        
        Student existingStudent = findStudent(student.getSid());
        existingStudent.setName(student.getName());
        existingStudent.setTele(student.getTele());
        return existingStudent;
    }

    /**
     * 查询学生
     */
    public Student findStudent(Long sid) {
        if (sid == null) {
            throw new InvalidStudentDataException("学号不能为空");
        }
        
        return students.stream()
                .filter(s -> s.getSid().equals(sid))
                .findFirst()
                .orElseThrow(() -> new StudentNotFoundException(sid));
    }

    /**
     * 查询所有学生
     */
    public List<Student> findAll() {
        return new ArrayList<>(students);
    }

    /**
     * 清空所有学生（用于测试）
     */
    public void clearAll() {
        students.clear();
        idGenerator.set(1);
    }
}