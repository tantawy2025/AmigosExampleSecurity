package com.example.amigosExampe.Student;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1,"mohamed"),
            new Student(2,"mamdouh")
    );

    @GetMapping(path = "/{studentId}")
    public Student getStudents(@PathVariable("studentId") Integer studentId ){
        return STUDENTS.stream()
                .filter(student -> student.getStudentId().equals(studentId))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("Student "+studentId+" does not exist"));
    }




}
