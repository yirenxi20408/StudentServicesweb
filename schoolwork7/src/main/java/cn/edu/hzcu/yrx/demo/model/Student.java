package cn.edu.hzcu.yrx.demo.model;

public class Student {
    private Long sid;
    private String name;
    private String tele;

    public Student() {}

    public Student(Long sid, String name, String tele) {
        this.sid = sid;
        this.name = name;
        this.tele = tele;
    }

    public Long getSid() { 
        return sid; 
    }
    
    public void setSid(Long sid) { 
        this.sid = sid; 
    }

    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }

    public String getTele() { 
        return tele; 
    }
    
    public void setTele(String tele) { 
        this.tele = tele; 
    }

    @Override
    public String toString() {
        return "Student{" +
                "sid=" + sid +
                ", name='" + name + '\'' +
                ", tele='" + tele + '\'' +
                '}';
    }
}