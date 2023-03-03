package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.Student;
import ra.model.service.StudentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("api/v1/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Student> getAllStudent() {
        return studentService.findAll();
    }

    @GetMapping("/{studentId}")
    public Student getStudentById(@PathVariable("studentId") int studentId) {
        return studentService.findById(studentId);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.saveOrUpdate(student);
    }

    @PutMapping("/{studentId}")
    public Student updateStudent(@PathVariable("studentId") int studentId, @RequestBody Student student) {
        Student studentUpdate = studentService.findById(studentId);
        studentUpdate.setStudentName(student.getStudentName());
        studentUpdate.setAge(student.getAge());
        studentUpdate.setStudentStatus(student.isStudentStatus());
        //Cap nhat
        return studentService.saveOrUpdate(studentUpdate);
    }

    @DeleteMapping("/{studentId}")
    public void deleteStudent(@PathVariable("studentId") int studentId) {
        studentService.delete(studentId);
    }

    @GetMapping("/search")
    public List<Student> seachByName(@RequestParam("studentName") String studentName) {
        return studentService.searchByName(studentName);
    }

    @GetMapping("/sortByName")
    public ResponseEntity<List<Student>> sortStudentByStudentName(@RequestParam("direction") String direction) {
        List<Student> studentList = studentService.sortStudentByStudentName(direction);
        return new ResponseEntity<>(studentList, HttpStatus.OK);
    }

    @GetMapping("/getPagging")
    public ResponseEntity<Map<String, Object>> getPagging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Student> studentPage = studentService.getPagging(pageable);
        Map<String, Object> data = new HashMap<>();
        data.put("student", studentPage.getContent());
        data.put("total", studentPage.getSize());
        data.put("totalItems", studentPage.getTotalElements());
        data.put("totalPages", studentPage.getTotalPages());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/getPaggingAndSortByName")
    public ResponseEntity<Map<String, Object>> getPaggingAndSortByName(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam String direction) {
        Sort.Order order;
        if (direction.equals("asc")) {
            order = new Sort.Order(Sort.Direction.ASC, "studentName");
        } else {
            order = new Sort.Order(Sort.Direction.DESC, "studentName");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        Page<Student> pageBook = studentService.getPagging(pageable);
        Map<String, Object> data = new HashMap<>();
        data.put("student", pageBook.getContent());
        data.put("total", pageBook.getSize());
        data.put("totalItems", pageBook.getTotalElements());
        data.put("totalPages", pageBook.getTotalPages());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}

