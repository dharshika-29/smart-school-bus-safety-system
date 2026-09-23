package com.busfleet.smartbussafety.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busfleet.smartbussafety.dto.StudentCreateRequest;
import com.busfleet.smartbussafety.dto.StudentDTO;
import com.busfleet.smartbussafety.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<StudentDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/parent/{parentId}")
    public List<StudentDTO> getStudentsByParent(@PathVariable Long parentId) {
        return studentService.getStudentsByParent(parentId);
    }

    @PostMapping
    public StudentDTO createStudent(@Valid @RequestBody StudentCreateRequest req) {
        return studentService.createStudent(req);
    }

    @PutMapping("/{id}")
    public StudentDTO updateStudent(@PathVariable Long id, @Valid @RequestBody StudentCreateRequest req) {
        return studentService.updateStudent(id, req);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}