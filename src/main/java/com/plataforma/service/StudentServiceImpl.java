package com.plataforma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plataforma.dto.StudentRequest;
import com.plataforma.dto.StudentResponse;
import com.plataforma.model.Student;
import com.plataforma.repository.StudentRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student con id " + id + " no encontrado"));
        return toResponse(student);
    }

    @Override
    public StudentResponse createStudent(StudentRequest request) {
        Student student = new Student();
        student.setName(request.getName());
        return toResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student con id " + id + " no encontrado"));
        student.setName(request.getName());
        return toResponse(studentRepository.save(student));
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new NoSuchElementException("Student con id " + id + " no encontrado");
        }
        studentRepository.deleteById(id);
    }

    private StudentResponse toResponse(Student student) {
        return new StudentResponse(student.getId(), student.getName());
    }
}
