# 学生信息管理系统 - IoC和AOP实现

## 项目说明

本项目使用Spring Boot框架，实现了基于IoC（控制反转）和AOP（面向切面编程）的学生信息管理系统，包含完整的增删改查功能。

## 核心技术

### 1. IoC (Inversion of Control) - 控制反转

- **实现方式**: 使用Spring的依赖注入（DI）
- **核心注解**:
  - `@Service`: 标记StudentService为Spring管理的服务组件
  - `@Autowired`: 在测试类和控制器中自动注入StudentService
  - `@Component`: 标记LoggingAspect为Spring管理的组件

### 2. AOP (Aspect Oriented Programming) - 面向切面编程

- **实现方式**: 使用Spring AOP和AspectJ
- **依赖配置**: `spring-boot-starter-aop`
- **核心注解**:
  - `@Aspect`: 标记LoggingAspect为切面类
  - `@Pointcut`: 定义切点表达式
  - `@Around`: 环绕通知，记录方法执行日志
  - `@Before`: 前置通知
  - `@AfterReturning`: 后置通知
  - `@AfterThrowing`: 异常通知

## 项目结构

```
src/main/java/cn/edu/hzcu/yrx/demo/
├── aspect/
│   └── LoggingAspect.java          # AOP日志切面
├── exception/
│   ├── StudentNotFoundException.java      # 学生未找到异常
│   └── InvalidStudentDataException.java   # 无效学生数据异常
├── model/
│   ├── Student.java                # 学生实体类
│   └── RunController.java          # Web控制器
├── service/
│   └── StudentService.java         # 学生服务类（使用IoC）
└── Schoolwork7Application.java     # Spring Boot主类
```

## 核心类说明

### 1. Student 实体类

```java
public class Student {
    private Long sid;      // 学号
    private String name;   // 姓名
    private String tele;   // 电话
}
```

### 2. StudentService 服务类

**IoC特性**: 使用`@Service`注解，由Spring容器管理

**CRUD方法**:
- `addStudent(Student)` - 添加学生
- `deleteStudent(Long sid)` - 删除学生
- `modifyStudent(Student)` - 修改学生信息
- `findStudent(Long sid)` - 查询单个学生
- `findAll()` - 查询所有学生

**数据验证**:
- 学生信息不能为空
- 姓名不能为空
- 电话不能为空
- 学号不能为空（查询/删除/修改时）

### 3. LoggingAspect AOP切面

**功能**: 自动记录所有StudentService方法的调用日志

**日志内容**:
- 方法名称
- 方法参数
- 执行时间（毫秒）
- 返回值
- 异常信息（如果有）

**日志示例**:
```
==> 调用方法: addStudent, 参数: [Student{sid=null, name='张三', tele='13800138000'}]
<== 方法 addStudent 执行成功, 耗时: 0ms, 返回值: Student{sid=1, name='张三', tele='13800138000'}
```

### 4. 自定义异常

**StudentNotFoundException**: 当学生不存在时抛出
```
学生未找到，学号: 999
```

**InvalidStudentDataException**: 当学生数据无效时抛出
```
学生姓名不能为空
学号不能为空
```

## 运行测试

### 运行所有测试
```bash
mvn test
```

### 运行演示测试
```bash
mvn test -Dtest=StudentServiceDemoTest
```

## 测试说明

### 测试类1: Schoolwork7ApplicationTests
- 包含13个单元测试
- 测试所有CRUD操作
- 测试异常处理
- 测试数据验证

### 测试类2: StudentServiceDemoTest
- 包含6个演示测试
- 清晰展示IoC和AOP的工作原理
- 包含完整的工作流程演示

## AOP日志示例

运行测试时，控制台会输出详细的AOP日志：

```
========== 开始测试 ==========
==> 调用方法: addStudent, 参数: [Student{sid=null, name='学生A', tele='11111111111'}]
<== 方法 addStudent 执行成功, 耗时: 0ms, 返回值: Student{sid=1, name='学生A', tele='11111111111'}
==> 调用方法: findStudent, 参数: [1]
<== 方法 findStudent 执行成功, 耗时: 0ms, 返回值: Student{sid=1, name='学生A', tele='11111111111'}
==> 调用方法: deleteStudent, 参数: [1]
<== 方法 deleteStudent 执行成功, 耗时: 0ms, 返回值: null
========== 测试结束 ==========
```

## 技术亮点

1. **IoC容器管理**: StudentService由Spring容器创建和管理，实现松耦合
2. **AOP日志切面**: 自动拦截所有服务方法，记录详细日志，无需修改业务代码
3. **异常处理**: 自定义异常类，提供清晰的错误提示
4. **数据验证**: 在服务层进行完整的数据验证
5. **完整测试**: 19个测试用例，覆盖所有功能点

## 依赖配置

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

## 测试结果

```
Tests run: 19, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## 总结

本项目成功实现了：
✅ IoC依赖注入管理StudentService
✅ AOP切面记录所有操作日志
✅ 完整的学生信息增删改查功能
✅ 自定义异常处理和错误提示
✅ 全面的单元测试覆盖
