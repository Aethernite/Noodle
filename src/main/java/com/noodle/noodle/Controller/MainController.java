package com.noodle.noodle.Controller;

import com.noodle.noodle.Entities.Course;
import com.noodle.noodle.Entities.Student;
import com.noodle.noodle.Models.UserDetails;
import com.noodle.noodle.Repositories.CourseRepository;
import com.noodle.noodle.Repositories.StudentRepository;
import com.noodle.noodle.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@SessionAttributes({"UserDetails"})
@Controller
public class MainController {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public MainController(UserRepository userRepository, CourseRepository courseRepository,StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/admin/home")
    public ModelAndView home(ModelAndView modelAndView, RedirectAttributes redirectAttributes, Principal principal){
        UserDetails userDetails = this.userRepository.findOneByUsername(principal.getName());
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view","views/home");
        modelAndView.addObject("UserDetails", userDetails);
        modelAndView.addObject("username", userDetails.getUsername());
        List<Course> courses = this.courseRepository.findAllByStatus("active");
        modelAndView.addObject("courses", courses);
        redirectAttributes.addFlashAttribute("UserDetails",userDetails);
        redirectAttributes.addFlashAttribute("username",userDetails.getUsername());
        return modelAndView;
    }

    @GetMapping("/admin/courses")
    public ModelAndView courses(ModelAndView modelAndView){
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "/views/courses");
        List<Course> courses = this.courseRepository.findAll();
        modelAndView.addObject("courses", courses);
        return modelAndView;
    }

    @GetMapping("/admin/students")
    public ModelAndView students(ModelAndView modelAndView){
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "/views/students");
        List<Student> students = this.studentRepository.findAll();
        modelAndView.addObject("students", students);
        return modelAndView;
    }

    @PostMapping("/admin/course/setactive/{id}")
    public String enableCourse(@PathVariable(value = "id") Integer id){
        Course course = this.courseRepository.findOneById(id);
        course.setStatusActive();
        this.courseRepository.save(course);
        return "redirect:/admin/courses";
    }

    @PostMapping("/admin/course/setinactive/{id}")
    public String disableCourse(@PathVariable(value = "id") Integer id){
        Course course = this.courseRepository.findOneById(id);
        course.setStatusInactive();
        this.courseRepository.save(course);
        return "redirect:/admin/courses";
    }

    @PostMapping("/admin/students/edit/{id}")
    public ModelAndView editStudent(ModelAndView modelAndView, @PathVariable(value = "id") Integer id){
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "/views/editstudent");
        Student student = studentRepository.findOneById(id);
        modelAndView.addObject("student",student);

     return modelAndView;
    }

    @PostMapping("/admin/students/edit/confirm/{id}")
    public String editStudent(Student student){
        studentRepository.save(student);
        return "redirect:/admin/students";
    }

    @GetMapping("/admin/contacts")
    public ModelAndView contacts(ModelAndView modelAndView) {
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "/views/contacts");
//        modelAndView.addObject()

        return modelAndView;
    }

}
