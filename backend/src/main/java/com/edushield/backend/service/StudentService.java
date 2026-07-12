package com.edushield.backend.service;

import com.edushield.backend.dto.StudentRequest;
import com.edushield.backend.dto.StudentResponse;
import com.edushield.backend.entity.Mentor;
import com.edushield.backend.entity.Student;
import com.edushield.backend.repository.MentorRepository;
import com.edushield.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MentorRepository mentorRepository;

    public StudentResponse createStudent(StudentRequest request) {
        Student student = new Student();
        mapRequestToEntity(request, student);
        Student saved = studentRepository.save(student);
        return mapToResponse(saved);
    }

    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        return mapToResponse(student);
    }

    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        mapRequestToEntity(request, student);
        Student updated = studentRepository.save(student);
        return mapToResponse(updated);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    public List<StudentResponse> getStudentsByMentor(Long mentorId) {
        return studentRepository.findByMentorId(mentorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private void mapRequestToEntity(StudentRequest request, Student student) {
        student.setName(request.getName());
        student.setRollNo(request.getRollNo());
        student.setDepartment(request.getDepartment());
        student.setYear(request.getYear());
        student.setCategory(request.getCategory());
        student.setAnnualIncome(request.getAnnualIncome());
        student.setState(request.getState());

        if (request.getMentorId() != null) {
            Mentor mentor = mentorRepository.findById(request.getMentorId())
                    .orElseThrow(() -> new RuntimeException("Mentor not found with id: " + request.getMentorId()));
            student.setMentor(mentor);
        }
    }

    private StudentResponse mapToResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getRollNo(),
                student.getDepartment(),
                student.getYear(),
                student.getCategory(),
                student.getAnnualIncome(),
                student.getState(),
                student.getMentor() != null ? student.getMentor().getId() : null,
                student.getMentor() != null ? student.getMentor().getName() : null
        );
    }
}