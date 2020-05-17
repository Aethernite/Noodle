package com.noodle.noodle.Controller;

import com.noodle.noodle.DefMeAlgorithm.Algorithm;
import com.noodle.noodle.Entities.*;
import com.noodle.noodle.Models.User;
import com.noodle.noodle.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@SessionAttributes("userId")
@Controller
public class MainController {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final LogRepository logRepository;
    private Algorithm algorithm;

    @Autowired
    public MainController(CourseRepository courseRepository, UserRepository userRepository, StudentRepository studentRepository, GroupRepository groupRepository, LogRepository logRepository, Algorithm algorithm) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.logRepository = logRepository;
        this.algorithm = algorithm;
    }

    @GetMapping("/admin/logged")
    public String logged(ModelAndView modelAndView,Principal principal,RedirectAttributes redirectAttributes){
        User user = this.userRepository.findOneByUsername(principal.getName());
        modelAndView.addObject("userId", user.getId());
        redirectAttributes.addFlashAttribute("userId", user.getId());
        return "redirect:/admin/home";
    }
    @GetMapping("/admin/home")
    public ModelAndView home(ModelAndView modelAndView, Principal principal,RedirectAttributes redirectAttributes){
        User user = this.userRepository.findOneByUsername(principal.getName());
        modelAndView.addObject("userId", user.getId());
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view","views/home");
        List<Course> courses = this.courseRepository.findAllByStatus("активен");
        modelAndView.addObject("courses", courses);
        redirectAttributes.addFlashAttribute("userId", user.getId());
        return modelAndView;
    }

    @GetMapping("/admin/courses")
    public ModelAndView courses(ModelAndView modelAndView,@ModelAttribute("userId") int id){
        User us = userRepository.findOneById(id);
        logRepository.save(createLogViewedCourses(us));
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "/views/courses");
        List<Course> courses = this.courseRepository.findAll();
        modelAndView.addObject("courses", courses);
        return modelAndView;
    }



    @GetMapping("/admin/students")
    public ModelAndView students(ModelAndView modelAndView,@ModelAttribute("userId") int id){
        User us = userRepository.findOneById(id);
        logRepository.save(createLogViewedStudents(us));
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
        modelAndView.addObject("view", "/views/editstudent");
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
    public String editStudent(Student student,@ModelAttribute("userId") int id){
        Group group = groupRepository.findOneByNum(student.getGroup().getNum());
        Set<Course> courses = studentRepository.findOneById(student.getId()).getCourses();
        student.setGroup(group);
        student.setCourses(courses);
        studentRepository.save(student);
        User us = userRepository.findOneById(id);
        Log log = createLogEditedStudent(us,student);
        logRepository.save(log);
        return "redirect:/admin/students";
    }

//КОЦЕ КОД
    @GetMapping("/admin/courses/add")
    public ModelAndView addCourse(ModelAndView modelAndView, Course course) {
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "/views/addcourse");
        return modelAndView;
    }

    @PostMapping("/admin/courses/add/confirm")
    public String addCourseSave(Course course,@ModelAttribute("userId") int id){
        course.setStatusActive();
        courseRepository.save(course);
        User user = userRepository.findOneById(id);
        Log log = createLogAddedCourse(user,course);
        logRepository.save(log);
        return "redirect:/admin/courses";
    }

    @GetMapping("/admin/students/add")
    public ModelAndView addStudent(ModelAndView modelAndView,Student student) {
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "/views/addstudent");
        Student.Semester[] semesters = Student.Semester.values();
        List<Group> groupList = groupRepository.findAll();

        modelAndView.addObject("semesters", semesters);
        modelAndView.addObject("groups", groupList);

        return modelAndView;
    }


    @PostMapping("/admin/students/add/confirm")
    public String addStudentSave(Student student, @ModelAttribute("userId") int id){
        student.setGroup(groupRepository.findOneByNum(student.getGroup().getNum()));
        studentRepository.save(student);
        User user = userRepository.findOneById(id);
        Log log = createLogAddedStudent(user,student);
        logRepository.save(log);
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
    public String editCourse(@ModelAttribute("userId") int id,Course course, RedirectAttributes redirectAttributes){
        User us = userRepository.findOneById(id);

        Course temp = courseRepository.findOneById(course.getId());
        temp.setCode(course.getCode());
        temp.setName(course.getName());
        temp.setDescription(course.getDescription());
        temp.setStudents(course.getStudents());
        courseRepository.save(temp);
        Log log = createLogEditedCourse(us,temp);
        logRepository.save(log);
        redirectAttributes.addFlashAttribute("userId", id);
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

    private Log createLogEditedCourse(User user, Course course) {
        Log log = new Log();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
        String[] dateTime = formatter.format(date).split(" ");
        log.setUser(user);
        log.setTime(dateTime[1]);
        log.setDate(dateTime[0]);
        log.setActivity("System");
        log.setContext("Course: " + course.getName());
        log.setDescription("Course edited");
        log.setCourse(course);
        log.setPlaintext(log.getDate() + ", " + log.getTime() + ", " + log.getContext() + ", " +log.getDescription() + ", " + "The user with id '"+ user.getId() +"' edited the course with id '"+ course.getId() +"'.");
        return log;
    }

    private Log createLogViewedCourses(User user) {
        Log log = new Log();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
        String[] dateTime = formatter.format(date).split(" ");
        log.setUser(user);
        log.setTime(dateTime[1]);
        log.setDate(dateTime[0]);
        log.setActivity("System");
        log.setContext("Courses: Courses page");
        log.setDescription("Courses viewed");
        log.setPlaintext(log.getDate() + ", " + log.getTime() + ", " + log.getContext() + ", " +log.getDescription() + ", " + "The user with id '"+ user.getId() +"' viewed the courses.");
        return log;
    }

    private Log createLogEditedStudent(User user, Student student) {
        Log log = new Log();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
        String[] dateTime = formatter.format(date).split(" ");
        log.setUser(user);
        log.setTime(dateTime[1]);
        log.setDate(dateTime[0]);
        log.setActivity("System");
        log.setContext("Student: " + student.getName());
        log.setDescription("Student edited");
        log.setStudent(student);
        log.setPlaintext(log.getDate() + ", " + log.getTime() + ", " + log.getContext() + ", " +log.getDescription() + ", " + "The user with id '"+ user.getId() +"' edited the student with id '"+ student.getId() +"'.");
        return log;
    }
    private Log createLogViewedStudents(User user) {
        Log log = new Log();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
        String[] dateTime = formatter.format(date).split(" ");
        log.setUser(user);
        log.setTime(dateTime[1]);
        log.setDate(dateTime[0]);
        log.setActivity("System");
        log.setContext("Students: Students page");
        log.setDescription("Students viewed");
        log.setPlaintext(log.getDate() + ", " + log.getTime() + ", " + log.getContext() + ", " +log.getDescription() + ", " + "The user with id '"+ user.getId() +"' viewed the students.");
        return log;
    }


    private Log createLogAddedStudent(User user,Student student) {
        Log log = new Log();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
        String[] dateTime = formatter.format(date).split(" ");
        log.setUser(user);
        log.setStudent(student);
        log.setTime(dateTime[1]);
        log.setDate(dateTime[0]);
        log.setActivity("System");
        log.setContext("Students: " + student.getName());
        log.setDescription("Student added");
        log.setPlaintext(log.getDate() + ", " + log.getTime() + ", " + log.getContext() + ", " +log.getDescription() + ", " + "The user with id '"+ user.getId() +"' added the student with id '"+ student.getId() +"'.");
        return log;
    }


    private Log createLogAddedCourse(User user,Course course) {
        Log log = new Log();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
        String[] dateTime = formatter.format(date).split(" ");
        log.setUser(user);
        log.setCourse(course);
        log.setTime(dateTime[1]);
        log.setDate(dateTime[0]);
        log.setActivity("System");
        log.setContext("Course: " + course.getName());
        log.setDescription("Course added");
        log.setPlaintext(log.getDate() + ", " + log.getTime() + ", " + log.getContext() + ", " +log.getDescription() + ", " + "The user with id '"+ user.getId() +"' added the course with id '"+ course.getId() +"'.");
        return log;
    }





    @GetMapping("/admin/logs")
    public ModelAndView logs(ModelAndView modelAndView){
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "/views/logs");
        List<Log> logsList = logRepository.findAll();
        modelAndView.addObject("logs", logsList);

        return modelAndView;

    }

    @GetMapping("/admin/logs/algorithm/1")
    public ModelAndView algorithmOne(ModelAndView modelAndView){
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "/views/algorithmOne");
        algorithm.extractToFileCourseEditedIds();
        algorithm.runAlgorithm();
        Map<Integer,Integer> intMap = algorithm.getDefMeInformationCourses();
        Map<Course, Integer> map = new HashMap<>();
        intMap.forEach((k, v) -> map.put(courseRepository.findOneById(k),v));
        modelAndView.addObject("map", map.entrySet());
        return modelAndView;

    }


    @GetMapping("/admin/logs/algorithm/2")
    public ModelAndView algorithmTwo(ModelAndView modelAndView){
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "/views/algorithmTwo");
        algorithm.extractToFileLogTypesIds();
        algorithm.runAlgorithm();
        HashMap<String, Integer> map = algorithm.getDefMeInformationLogs();
        modelAndView.addObject("map", map.entrySet());
        return modelAndView;

    }

    @GetMapping("/admin/logs/algorithm/3")
    public ModelAndView algorithmThree(ModelAndView modelAndView){
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "/views/algorithmThree");
        algorithm.extractToFileUserIds();
        algorithm.runAlgorithm();
        HashMap<Integer, Integer> map = algorithm.getDefMeInformationUsers();
        modelAndView.addObject("map", map.entrySet());
        return modelAndView;
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
