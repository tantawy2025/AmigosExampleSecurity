package com.example.amigosExampe.Student;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("managment/api/v1/students")
public class ManagmentContoller {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1,"mohamed"),
            new Student(2,"mamdouh")
    );

    @GetMapping
    public List<Student> getAllStudents(){
        System.out.println("all student returned");
        return STUDENTS;
    }

    @PostMapping
    public void registerNewStudent(@RequestBody Student student){
        System.out.println("registerNewStudent");
        System.out.println(student.toString());
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Integer studentId){
        System.out.println("deleteStudent");
        System.out.println(studentId);
    }

    @PutMapping(path = "{studentId}")
    public void updateStudent(@PathVariable("studentId") Integer studentId,@RequestBody Student student){
        System.out.println("updateStudent");
        System.out.println(student.toString());
    }


}
