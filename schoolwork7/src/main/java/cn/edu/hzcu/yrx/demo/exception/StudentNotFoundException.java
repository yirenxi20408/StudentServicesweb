package cn.edu.hzcu.yrx.demo.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }
    
    public StudentNotFoundException(Long sid) {
        super("学生未找到，学号: " + sid);
    }
}
