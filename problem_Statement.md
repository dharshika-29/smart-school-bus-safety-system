# Problem Statement

## 1. Title

Smart School Bus Safety & Tracking System

## 2. Domain
Education / Transportation

## 3. Who is the User?
1. School Admin
2. Bus Driver
3. Parent

## 4. What Problem are We Solving?
Many schools face difficulties in tracking the real-time location of school buses. Parents often do not know when the bus will arrive or whether their child has safely boarded or reached school. Schools also face challenges in monitoring bus routes and handling emergency situations quickly. This project provides a smart solution to improve student safety, communication, and bus management through live tracking and instant notifications.

## 5. Proposed Solution
The proposed system is a Smart School Bus Safety & Tracking System that allows parents to track the live location of the school bus. It provides QR-based student attendance, instant notifications when students board or leave the bus, route monitoring, emergency SOS alerts, and offline data synchronization. The system helps schools improve student safety and transportation management.

## 6. Core Entities / Database Tables
1. Admin
2. Driver
3. Parent
4. Student
5. Bus
6. Route
7. Attendance
8. Notification

## 7. User Roles & Permissions
Admin
- Manage buses
- Manage drivers
- Manage students
- Monitor live location
- View attendance reports

Driver
- Login
- Start/Stop trip
- Share live location
- Send SOS alert

Parent
- Login
- View live bus location
- Receive notifications
- View student attendance

## 8. Success Criteria
1. 1. Parents should be able to view the live location of the school bus.
2. Parents should receive notifications when the student boards and leaves the bus.
3. School admin should monitor all buses in real time.
4. Driver should be able to send emergency SOS alerts.
5. Student attendance should be recorded successfully using QR code.

## 9. Out of Scope
1. Online fee payment
2. CCTV live video streaming
3. Vehicle maintenance management
4. Bus ticket booking
5. Driver salary management

## 10. Chosen Track
Java Track

Frontend : React.js
Backend : Spring Boot
Database : MySQL