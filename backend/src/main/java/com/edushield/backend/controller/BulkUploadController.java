package com.edushield.backend.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edushield.backend.entity.AttendanceRecord;
import com.edushield.backend.entity.Category;
import com.edushield.backend.entity.FeeStatus;
import com.edushield.backend.entity.Marks;
import com.edushield.backend.entity.Mentor;
import com.edushield.backend.entity.Role;
import com.edushield.backend.entity.Scholarship;
import com.edushield.backend.entity.Student;
import com.edushield.backend.entity.User;
import com.edushield.backend.repository.AttendanceRecordRepository;
import com.edushield.backend.repository.FeeStatusRepository;
import com.edushield.backend.repository.MarksRepository;
import com.edushield.backend.repository.MentorRepository;
import com.edushield.backend.repository.ScholarshipRepository;
import com.edushield.backend.repository.StudentRepository;
import com.edushield.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/admin/students")
public class BulkUploadController {



    @Autowired
private AttendanceRecordRepository attendanceRepository;

@Autowired
private MarksRepository marksRepository;

@Autowired
private FeeStatusRepository feeStatusRepository;

@PostMapping("/bulk-upload-attendance")
public ResponseEntity<BulkUploadResponse> bulkUploadAttendance(@RequestParam("file") MultipartFile file) {
    int successCount = 0;
    List<String> errors = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
        String line;
        boolean isFirstLine = true;
        int lineNumber = 0;

        while ((line = reader.readLine()) != null) {
            lineNumber++;
            if (line.trim().isEmpty()) continue;

            List<String> columns = parseCsvLine(line);
            if (isFirstLine) {
                isFirstLine = false;
                if (columns.get(0).equalsIgnoreCase("rollNo")) continue;
            }

            if (columns.size() < 5) {
                errors.add("Line " + lineNumber + ": Insufficient columns. Expected 5, got " + columns.size());
                continue;
            }

            try {
                String rollNo = columns.get(0);
                Integer month = Integer.parseInt(columns.get(1));
                Integer year = Integer.parseInt(columns.get(2));
                Integer totalDays = Integer.parseInt(columns.get(3));
                Integer presentDays = Integer.parseInt(columns.get(4));

                Student student = studentRepository.findByRollNo(rollNo).orElse(null);
                if (student == null) {
                    errors.add("Line " + lineNumber + ": student with rollNo '" + rollNo + "' not found");
                    continue;
                }

                AttendanceRecord record = new AttendanceRecord();
                record.setStudent(student);
                record.setMonth(month);
                record.setYear(year);
                record.setTotalDays(totalDays);
                record.setPresentDays(presentDays);
                attendanceRepository.save(record);

                successCount++;
            } catch (Exception e) {
                errors.add("Line " + lineNumber + ": " + e.getMessage());
            }
        }
    } catch (Exception e) {
        errors.add("Failed to process file: " + e.getMessage());
    }

    return ResponseEntity.ok(new BulkUploadResponse(successCount, errors));
}

@PostMapping("/bulk-upload-marks")
public ResponseEntity<BulkUploadResponse> bulkUploadMarks(@RequestParam("file") MultipartFile file) {
    int successCount = 0;
    List<String> errors = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
        String line;
        boolean isFirstLine = true;
        int lineNumber = 0;

        while ((line = reader.readLine()) != null) {
            lineNumber++;
            if (line.trim().isEmpty()) continue;

            List<String> columns = parseCsvLine(line);
            if (isFirstLine) {
                isFirstLine = false;
                if (columns.get(0).equalsIgnoreCase("rollNo")) continue;
            }

            if (columns.size() < 6) {
                errors.add("Line " + lineNumber + ": Insufficient columns. Expected 6, got " + columns.size());
                continue;
            }

            try {
                String rollNo = columns.get(0);
                String subject = columns.get(1);
                String assessmentType = columns.get(2);
                Double score = Double.parseDouble(columns.get(3));
                Double maxScore = Double.parseDouble(columns.get(4));
                LocalDate assessmentDate = LocalDate.parse(columns.get(5));

                Student student = studentRepository.findByRollNo(rollNo).orElse(null);
                if (student == null) {
                    errors.add("Line " + lineNumber + ": student with rollNo '" + rollNo + "' not found");
                    continue;
                }

                Marks marks = new Marks();
                marks.setStudent(student);
                marks.setSubject(subject);
                marks.setAssessmentType(assessmentType);
                marks.setScore(score);
                marks.setMaxScore(maxScore);
                marks.setAssessmentDate(assessmentDate);
                marksRepository.save(marks);

                successCount++;
            } catch (Exception e) {
                errors.add("Line " + lineNumber + ": " + e.getMessage());
            }
        }
    } catch (Exception e) {
        errors.add("Failed to process file: " + e.getMessage());
    }

    return ResponseEntity.ok(new BulkUploadResponse(successCount, errors));
}

@PostMapping("/bulk-upload-fees")
public ResponseEntity<BulkUploadResponse> bulkUploadFees(@RequestParam("file") MultipartFile file) {
    int successCount = 0;
    List<String> errors = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
        String line;
        boolean isFirstLine = true;
        int lineNumber = 0;

        while ((line = reader.readLine()) != null) {
            lineNumber++;
            if (line.trim().isEmpty()) continue;

            List<String> columns = parseCsvLine(line);
            if (isFirstLine) {
                isFirstLine = false;
                if (columns.get(0).equalsIgnoreCase("rollNo")) continue;
            }

            if (columns.size() < 5) {
                errors.add("Line " + lineNumber + ": Insufficient columns. Expected 5, got " + columns.size());
                continue;
            }

            try {
                String rollNo = columns.get(0);
                Integer semester = Integer.parseInt(columns.get(1));
                Double amountDue = Double.parseDouble(columns.get(2));
                LocalDate dueDate = LocalDate.parse(columns.get(3));
                Boolean paid = Boolean.parseBoolean(columns.get(4));

                Student student = studentRepository.findByRollNo(rollNo).orElse(null);
                if (student == null) {
                    errors.add("Line " + lineNumber + ": student with rollNo '" + rollNo + "' not found");
                    continue;
                }

                FeeStatus fee = new FeeStatus();
                fee.setStudent(student);
                fee.setSemester(semester);
                fee.setAmountDue(amountDue);
                fee.setDueDate(dueDate);
                fee.setPaid(paid);
                feeStatusRepository.save(fee);

                successCount++;
            } catch (Exception e) {
                errors.add("Line " + lineNumber + ": " + e.getMessage());
            }
        }
    } catch (Exception e) {
        errors.add("Failed to process file: " + e.getMessage());
    }

    return ResponseEntity.ok(new BulkUploadResponse(successCount, errors));
}

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private ScholarshipRepository scholarshipRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static class BulkUploadResponse {
        private int successCount;
        private List<String> errors;

        public BulkUploadResponse(int successCount, List<String> errors) {
            this.successCount = successCount;
            this.errors = errors;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public List<String> getErrors() {
            return errors;
        }
    }

    private List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder curVal = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (inQuotes) {
                if (ch == '\"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                        curVal.append('\"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    curVal.append(ch);
                }
            } else {
                if (ch == '\"') {
                    inQuotes = true;
                } else if (ch == ',') {
                    result.add(curVal.toString().trim());
                    curVal.setLength(0);
                } else {
                    curVal.append(ch);
                }
            }
        }
        result.add(curVal.toString().trim());
        return result;
    }

    @PostMapping("/bulk-upload")
    public ResponseEntity<BulkUploadResponse> bulkUploadStudents(@RequestParam("file") MultipartFile file) {
        int successCount = 0;
        List<String> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> columns = parseCsvLine(line);

                if (isFirstLine) {
                    isFirstLine = false;
                    if (columns.size() > 0 && columns.get(0).equalsIgnoreCase("username")) {
                        continue;
                    }
                }

                if (columns.size() < 9) {
                    errors.add("Line " + lineNumber + ": Insufficient columns. Expected at least 9, got " + columns.size());
                    continue;
                }

                String username = columns.get(0);
                String password = columns.get(1);
                String name = columns.get(2);
                String rollNo = columns.get(3);
                String department = columns.get(4);
                String yearStr = columns.get(5);
                String categoryStr = columns.get(6);
                String annualIncomeStr = columns.get(7);
                String state = columns.get(8);
                String mentorUsername = columns.size() > 9 ? columns.get(9) : "";

                if (username.isEmpty() || password.isEmpty() || name.isEmpty() || rollNo.isEmpty() || department.isEmpty() || yearStr.isEmpty() || categoryStr.isEmpty()) {
                    errors.add("Line " + lineNumber + ": Username, password, name, rollNo, department, year, and category are required fields");
                    continue;
                }

                if (userRepository.existsByUsername(username)) {
                    errors.add("Line " + lineNumber + ": Username '" + username + "' already exists");
                    continue;
                }

                if (studentRepository.existsByRollNo(rollNo)) {
                    errors.add("Line " + lineNumber + ": Student with roll number '" + rollNo + "' already exists");
                    continue;
                }

                Integer year;
                try {
                    year = Integer.parseInt(yearStr);
                } catch (NumberFormatException e) {
                    errors.add("Line " + lineNumber + ": Invalid year '" + yearStr + "'");
                    continue;
                }

                Double annualIncome = null;
                if (!annualIncomeStr.isEmpty()) {
                    try {
                        annualIncome = Double.parseDouble(annualIncomeStr);
                    } catch (NumberFormatException e) {
                        errors.add("Line " + lineNumber + ": Invalid annual income '" + annualIncomeStr + "'");
                        continue;
                    }
                }

                try {
                    Category categoryEnum;
                    try {
                        categoryEnum = Category.valueOf(categoryStr.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        errors.add("Line " + lineNumber + ": Invalid category '" + categoryStr + "'");
                        continue;
                    }

                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(passwordEncoder.encode(password));
                    user.setRole(Role.STUDENT);
                    User savedUser = userRepository.save(user);

                    Student student = new Student();
                    student.setUser(savedUser);
                    student.setName(name);
                    student.setRollNo(rollNo);
                    student.setDepartment(department);
                    student.setYear(year);
                    student.setCategory(categoryEnum.name());
                    student.setAnnualIncome(annualIncome);
                    student.setState(state.isEmpty() ? null : state);

                    if (!mentorUsername.isEmpty()) {
                        User mentorUser = userRepository.findByUsername(mentorUsername).orElse(null);
                        if (mentorUser == null) {
                            errors.add("Line " + lineNumber + ": mentor username '" + mentorUsername + "' not found — student created without mentor");
                        } else {
                            Mentor mentor = mentorRepository.findByUserId(mentorUser.getId()).orElse(null);
                            if (mentor == null) {
                                errors.add("Line " + lineNumber + ": '" + mentorUsername + "' is not a mentor account — student created without mentor");
                            } else {
                                student.setMentor(mentor);
                            }
                        }
                    }

                    studentRepository.save(student);
                    successCount++;
                } catch (Exception e) {
                    errors.add("Line " + lineNumber + ": Error saving to database: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            errors.add("Failed to process file: " + e.getMessage());
        }

        return ResponseEntity.ok(new BulkUploadResponse(successCount, errors));
    }

    @PostMapping("/bulk-upload-mentors")
    public ResponseEntity<BulkUploadResponse> bulkUploadMentors(@RequestParam("file") MultipartFile file) {
        int successCount = 0;
        List<String> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> columns = parseCsvLine(line);

                if (isFirstLine) {
                    isFirstLine = false;
                    if (columns.size() > 0 && columns.get(0).equalsIgnoreCase("username")) {
                        continue;
                    }
                }

                if (columns.size() < 4) {
                    errors.add("Line " + lineNumber + ": Insufficient columns. Expected 4, got " + columns.size());
                    continue;
                }

                String username = columns.get(0);
                String password = columns.get(1);
                String name = columns.get(2);
                String department = columns.get(3);

                if (username.isEmpty() || password.isEmpty() || name.isEmpty() || department.isEmpty()) {
                    errors.add("Line " + lineNumber + ": Username, password, name, and department are required fields");
                    continue;
                }

                if (userRepository.existsByUsername(username)) {
                    errors.add("Line " + lineNumber + ": Username '" + username + "' already exists");
                    continue;
                }

                try {
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(passwordEncoder.encode(password));
                    user.setRole(Role.MENTOR);
                    User savedUser = userRepository.save(user);

                    Mentor mentor = new Mentor();
                    mentor.setUser(savedUser);
                    mentor.setName(name);
                    mentor.setDepartment(department);
                    mentorRepository.save(mentor);

                    successCount++;
                } catch (Exception e) {
                    errors.add("Line " + lineNumber + ": Error saving to database: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            errors.add("Failed to process file: " + e.getMessage());
        }

        return ResponseEntity.ok(new BulkUploadResponse(successCount, errors));
    }

    @PostMapping("/bulk-upload-scholarships")
    public ResponseEntity<BulkUploadResponse> bulkUploadScholarships(@RequestParam("file") MultipartFile file) {
        int successCount = 0;
        List<String> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> columns = parseCsvLine(line);

                if (isFirstLine) {
                    isFirstLine = false;
                    if (columns.size() > 0 && columns.get(0).equalsIgnoreCase("name")) {
                        continue;
                    }
                }

                if (columns.size() < 8) {
                    errors.add("Line " + lineNumber + ": Insufficient columns. Expected 8, got " + columns.size());
                    continue;
                }

                String name = columns.get(0);
                String minIncomeStr = columns.get(1);
                String maxIncomeStr = columns.get(2);
                String categoryStr = columns.get(3);
                String minMarksStr = columns.get(4);
                String state = columns.get(5);
                String deadlineStr = columns.get(6);
                String description = columns.get(7);

                if (name.isEmpty() || deadlineStr.isEmpty()) {
                    errors.add("Line " + lineNumber + ": Name and deadline are required fields");
                    continue;
                }

                Double minIncome = null;
                if (!minIncomeStr.isEmpty()) {
                    try {
                        minIncome = Double.parseDouble(minIncomeStr);
                    } catch (NumberFormatException e) {
                        errors.add("Line " + lineNumber + ": Invalid min income '" + minIncomeStr + "'");
                        continue;
                    }
                }

                Double maxIncome = null;
                if (!maxIncomeStr.isEmpty()) {
                    try {
                        maxIncome = Double.parseDouble(maxIncomeStr);
                    } catch (NumberFormatException e) {
                        errors.add("Line " + lineNumber + ": Invalid max income '" + maxIncomeStr + "'");
                        continue;
                    }
                }

                Double minMarks = null;
                if (!minMarksStr.isEmpty()) {
                    try {
                        minMarks = Double.parseDouble(minMarksStr);
                    } catch (NumberFormatException e) {
                        errors.add("Line " + lineNumber + ": Invalid min marks '" + minMarksStr + "'");
                        continue;
                    }
                }

                Category categoryEnum = null;
                if (!categoryStr.isEmpty()) {
                    try {
                        categoryEnum = Category.valueOf(categoryStr.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        errors.add("Line " + lineNumber + ": Invalid category '" + categoryStr + "'");
                        continue;
                    }
                }

                LocalDate deadline;
                try {
                    deadline = LocalDate.parse(deadlineStr);
                } catch (Exception e) {
                    errors.add("Line " + lineNumber + ": Invalid deadline format '" + deadlineStr + "'. Expected yyyy-MM-dd");
                    continue;
                }

                try {
                    Scholarship scholarship = new Scholarship();
                    scholarship.setName(name);
                    scholarship.setMinIncome(minIncome);
                    scholarship.setMaxIncome(maxIncome);
                    scholarship.setCategory(categoryEnum);
                    scholarship.setMinMarks(minMarks);
                    scholarship.setState(state.isEmpty() ? null : state);
                    scholarship.setDeadline(deadline);
                    scholarship.setDescription(description);

                    scholarshipRepository.save(scholarship);
                    successCount++;
                } catch (Exception e) {
                    errors.add("Line " + lineNumber + ": Error saving to database: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            errors.add("Failed to process file: " + e.getMessage());
        }

        return ResponseEntity.ok(new BulkUploadResponse(successCount, errors));
    }
}