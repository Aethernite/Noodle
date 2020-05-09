package com.noodle.noodle.Controller;

import com.noodle.noodle.Entities.Course;
import com.noodle.noodle.Models.UserDetails;
import com.noodle.noodle.Repositories.CourseRepository;
import com.noodle.noodle.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    public MainController(UserRepository userRepository, CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
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
    public ModelAndView orders(ModelAndView modelAndView){

        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "/views/courses");
        List<Course> courses = this.courseRepository.findAllByStatus("active");
        modelAndView.addObject("courses", courses);
        return modelAndView;
    }

    @PostMapping("/admin/courses/setactive/{id}")
    public String completeOrder(@PathVariable(value = "id") Integer id){
        Course course = this.courseRepository.findOneById(id);
        course.setStatusActive();
        this.courseRepository.save(course);
        return "redirect:/admin/courses";
    }


}
