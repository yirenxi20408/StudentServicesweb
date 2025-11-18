package cn.edu.hzcu.yrx.demo.model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import cn.edu.hzcu.yrx.demo.model.Student;
import cn.edu.hzcu.yrx.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class RunController {

    @Autowired
    private StudentService studentService;

    // 根路径重定向到录入页面或列表页，避免 Whitelabel 404
    @GetMapping("/")
    public String root() {
        return "redirect:/students/new";
    }

    // 显示学生录入表单
    @GetMapping("/students/new")
    public String showForm(Model model) {
        model.addAttribute("student", new Student());
        return "form";
    }

    // 处理表单提交（POST），使用表单字段自动绑定到 Student
    @PostMapping("/students")
    public String submitForm(@ModelAttribute Student student) {
        studentService.addStudent(student);
        return "redirect:/students"; // 提交后重定向到列表页（PRG 模式）
    }

    // 展示学生列表
    @GetMapping("/students")
    public String list(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "list";
    }
}