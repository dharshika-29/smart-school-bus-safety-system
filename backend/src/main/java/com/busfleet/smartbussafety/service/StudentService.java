package com.busfleet.smartbussafety.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.busfleet.smartbussafety.dto.StudentCreateRequest;
import com.busfleet.smartbussafety.dto.StudentDTO;
import com.busfleet.smartbussafety.entity.Bus;
import com.busfleet.smartbussafety.entity.Parent;
import com.busfleet.smartbussafety.entity.Student;
import com.busfleet.smartbussafety.exception.ApiException;
import com.busfleet.smartbussafety.repository.BusRepository;
import com.busfleet.smartbussafety.repository.ParentRepository;
import com.busfleet.smartbussafety.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final BusRepository busRepository;

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<StudentDTO> getStudentsByParent(Long parentId) {
        return studentRepository.findByParentId(parentId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public StudentDTO createStudent(StudentCreateRequest req) {
        Student student = new Student();
        student.setName(req.getName());
        student.setClassName(req.getClassName());
        student.setRollNumber(req.getRollNumber());

        if (req.getParentId() != null) {
            Parent parent = parentRepository.findById(req.getParentId())
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Parent not found"));
            student.setParent(parent);
        }
        if (req.getBusId() != null) {
            Bus bus = busRepository.findById(req.getBusId())
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Bus not found"));
            student.setBus(bus);
        }
        return toDTO(studentRepository.save(student));
    }

    public StudentDTO updateStudent(Long id, StudentCreateRequest req) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Student not found"));
        student.setName(req.getName());
        student.setClassName(req.getClassName());
        student.setRollNumber(req.getRollNumber());

        if (req.getParentId() != null) {
            Parent parent = parentRepository.findById(req.getParentId())
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Parent not found"));
            student.setParent(parent);
        }
        if (req.getBusId() != null) {
            Bus bus = busRepository.findById(req.getBusId())
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Bus not found"));
            student.setBus(bus);
        }
        return toDTO(studentRepository.save(student));
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Student not found");
        }
        studentRepository.deleteById(id);
    }

    private StudentDTO toDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setClassName(student.getClassName());
        dto.setRollNumber(student.getRollNumber());
        if (student.getParent() != null) {
            dto.setParentId(student.getParent().getId());
            dto.setParentName(student.getParent().getName());
        }
        if (student.getBus() != null) {
            dto.setBusId(student.getBus().getId());
            dto.setBusNumber(student.getBus().getBusNumber());
        }
        return dto;
    }
}