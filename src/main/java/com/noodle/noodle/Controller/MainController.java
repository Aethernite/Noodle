package com.noodle.noodle.Controller;

import com.noodle.noodle.Entities.Course;
import com.noodle.noodle.Entities.Group;
import com.noodle.noodle.Entities.Identification;
import com.noodle.noodle.Entities.Student;
import com.noodle.noodle.Models.UserDetails;
import com.noodle.noodle.Repositories.CourseRepository;
import com.noodle.noodle.Repositories.GroupRepository;
import com.noodle.noodle.Repositories.StudentRepository;
import com.noodle.noodle.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@SessionAttributes({"UserDetails"})
@Controller
public class MainController {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public MainController(CourseRepository courseRepository, UserRepository userRepository, StudentRepository studentRepository, GroupRepository groupRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
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
//ПОДОБРЕНИЕ НА СТРАНИЦА И СЕЙВ
    @GetMapping("/admin/students/edit/{id}")
    public ModelAndView editStudent(ModelAndView modelAndView,@PathVariable(value = "id") Integer id){
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "/views/edit");
        Student student = studentRepository.findOneById(id);
        Set<Course> courses = student.getCourses();
        List<Group> groups = groupRepository.findAll();
        Student.Semester[] semesters = Student.Semester.values();
        modelAndView.addObject("courses",courses);
        modelAndView.addObject("student",student);
        modelAndView.addObject("groups",groups);
        modelAndView.addObject("semesters",semesters);
        return modelAndView;
    }

    @PostMapping("/admin/students/edit/{id}")
    public String editStudent(Student student){
        Group group = groupRepository.findOneByNum(student.getGroup().getNum());
        Set<Course> courses = studentRepository.findOneById(student.getId()).getCourses();
        student.setGroup(group);
        student.setCourses(courses);
        studentRepository.save(student);
        return "redirect:/admin/students";
    }

//КОЦЕ КОД
    @GetMapping("/admin/courses/add")
    public ModelAndView addCourse(ModelAndView modelAndView) {
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "/views/addcourse");
        return modelAndView;
    }

    @PostMapping("/admin/courses/add/confirm")
    public String addCourseSave(@RequestParam("courseName") String name,
                                 @RequestParam("description") String description,
                                 @RequestParam("code") String code,
                                 @RequestParam("status") String status){

        try {
            Course course = new Course();
            course.setName(name);
            course.setDescription(description);
            course.setCode(code);
            course.setStatus(status);
            courseRepository.save(course);
        } catch(Exception e) {
            e.printStackTrace();

        }

        return "redirect:/admin/courses";
    }

    @GetMapping("/admin/students/add")
    public ModelAndView addStudent(ModelAndView modelAndView) {
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "/views/addstudent");

        Student.Semester[] semesters = Student.Semester.values();
        List<Group> groupList = groupRepository.findAll();

        Student student = new Student();
        modelAndView.addObject("stud", student);
        modelAndView.addObject("semesters", semesters);
        modelAndView.addObject("semester", semesters[0]);
        modelAndView.addObject("groups", groupList);
        modelAndView.addObject("group", groupList.get(0));

        return modelAndView;
    }


    @PostMapping("/admin/students/add/confirm")
    public String addStudentSave(@ModelAttribute(value="stud") Student student){
        student.setGroup(groupRepository.findOneByNum(student.getGroup().getNum()));
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



    @GetMapping("/admin/courses/edit/{id}")
    public ModelAndView editCourse(Identification identification,RedirectAttributes redirectAttributes,ModelAndView modelAndView, @PathVariable(value = "id") Integer id){
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "/views/editcourse");
        Course course = courseRepository.findOneById(id);
        Set<Student> students = course.getStudents();
        modelAndView.addObject("course", course);
        modelAndView.addObject("students", students);
        modelAndView.addObject("content", course.getDescription());
        modelAndView.addObject("identification", identification);
        redirectAttributes.addAttribute("course", course);
        return modelAndView;
    }


    @PostMapping("/admin/courses/edit/{id}")
    public String editCourse(Course course){
        Course temp = courseRepository.findOneById(course.getId());
        temp.setCode(course.getCode());
        temp.setName(course.getName());
        temp.setDescription(course.getDescription());
        temp.setStudents(course.getStudents());
        courseRepository.save(temp);
        return "redirect:/admin/courses";
    }


    @PostMapping("/admin/courses/removefromcourse/{student_id}")
    public String removeStudentFromCourse(@PathVariable(value = "student_id") Integer id, Identification identification){
        System.out.println("CourseID: " + identification.getId());
        Course course = courseRepository.findOneById(identification.getId());
        Set<Student> students = course.getStudents();
        Student student = studentRepository.findOneById(id);
        students.removeIf(entry -> entry.getId().equals(id));
        course.setStudents(students);
        courseRepository.save(course);
        student.getCourses().remove(course);
        studentRepository.save(student);
        return "redirect:/admin/courses/edit/" + course.getId();
    }





/* ПРЕДИ
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
*/
}
