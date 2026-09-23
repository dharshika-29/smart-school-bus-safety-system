package com.busfleet.smartbussafety.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.busfleet.smartbussafety.entity.Attendance;
import com.busfleet.smartbussafety.service.AttendanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public Attendance markAttendance(@RequestBody Map<String, String> body) {
        Long studentId = Long.valueOf(body.get("studentId"));
        Attendance.AttendanceStatus status = Attendance.AttendanceStatus.valueOf(body.get("status"));
        return attendanceService.markAttendance(studentId, status);
    }

    @GetMapping("/student/{studentId}")
    public List<Attendance> getAttendance(@PathVariable Long studentId,
                                            @RequestParam(required = false) String date) {
        LocalDate d = date != null ? LocalDate.parse(date) : LocalDate.now();
        return attendanceService.getAttendanceForStudent(studentId, d);
    }
}