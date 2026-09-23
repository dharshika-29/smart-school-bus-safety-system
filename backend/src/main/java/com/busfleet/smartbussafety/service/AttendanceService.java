package com.busfleet.smartbussafety.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.busfleet.smartbussafety.entity.Attendance;
import com.busfleet.smartbussafety.entity.Student;
import com.busfleet.smartbussafety.exception.ApiException;
import com.busfleet.smartbussafety.repository.AttendanceRepository;
import com.busfleet.smartbussafety.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public Attendance markAttendance(Long studentId, Attendance.AttendanceStatus status) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Student not found"));

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setDate(LocalDate.now());
        attendance.setStatus(status);
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceForStudent(Long studentId, LocalDate date) {
        return attendanceRepository.findByStudentIdAndDate(studentId, date);
    }
}