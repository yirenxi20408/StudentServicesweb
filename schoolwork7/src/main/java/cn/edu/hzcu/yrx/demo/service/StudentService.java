package cn.edu.hzcu.yrx.demo.service;

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

    public List<Student> findAll() {
        return new ArrayList<>(students);
    }

    public Student save(Student s) {
        if (s.getId() == null) {
            s.setId(idGenerator.getAndIncrement());
        }
        students.add(s);
        return s;
    }

    public void clearAll() {
        students.clear();
        idGenerator.set(1);
    }
}