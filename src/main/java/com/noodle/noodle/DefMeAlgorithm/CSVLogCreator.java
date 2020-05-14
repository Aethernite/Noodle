package com.noodle.noodle.DefMeAlgorithm;

import com.noodle.noodle.Entities.Course;
import com.noodle.noodle.Entities.Log;
import com.noodle.noodle.Entities.Student;
import com.noodle.noodle.Models.User;
import com.noodle.noodle.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CSVLogCreator implements ApplicationRunner {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final LogRepository logRepository;
    private final File file;
    @Autowired
    public CSVLogCreator(CourseRepository courseRepository, UserRepository userRepository, StudentRepository studentRepository, GroupRepository groupRepository,LogRepository logRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.logRepository = logRepository;
        this.groupRepository = groupRepository;
        this.file = new File("filteredLogs.txt");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(logRepository.count()<10) {
            CSVDataFilter dataFilter = new CSVDataFilter();
            dataFilter.filter();
            List<Log> logs = extractLogs(file);
            loadLogs(logs);
        }
    }

    private void loadLogs(List<Log> logs){
        for(Log log: logs){
            logRepository.save(log);
        }
    }
    public List<Log> extractLogs(File file) throws IOException {
        List<Log> logs = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String cvsSplitBy = "::";
        while ((line = br.readLine()) != null) {
            // use separator
            String[] cols = line.split(cvsSplitBy);
            if(line.contains("Course viewed")){
                Log log = new Log();
                log.setTime(cols[1]);
                log.setDate(cols[0]);
                log.setContext(cols[2]);
                log.setActivity(cols[3]);
                log.setDescription(cols[4]);
                int[] ids = extractIds2fields(line);
                if(courseRepository.existsById(ids[1])){
                    Course course = courseRepository.findOneById(ids[1]);
                    log.setCourse(course);
                }

                if(userRepository.existsById(ids[0])){
                    User user = userRepository.findOneById(ids[0]);
                    log.setUser(user);
                }
                log.setPlaintext(cols[0] + ", " + cols[1]  + ", " + cols[2] + ", " + cols[3] + cols[4] + ", " + cols[5]);
                logs.add(log);
            }
            else if(line.contains("User enrolled")){
                Log log = new Log();
                log.setDate(cols[0]);
                log.setTime(cols[1]);
                log.setContext(cols[2]);
                log.setActivity(cols[3]);
                log.setDescription(cols[4]);
                int[] ids = extractIds3fields(line);
                if(courseRepository.existsById(ids[2])){
                    Course course = courseRepository.findOneById(ids[2]);
                    log.setCourse(course);
                }
                if(userRepository.existsById(ids[0])){
                    User user = userRepository.findOneById(ids[0]);
                    log.setUser(user);
                }
                if(studentRepository.existsById(ids[1])){
                    Student student = studentRepository.findOneById(ids[1]);
                    log.setStudent(student);
                }
                log.setPlaintext(cols[0] + ", " + cols[1]  + ", " + cols[2] + ", " + cols[3] + cols[4] + ", " + cols[5]);
                logs.add(log);
            }
        }
        return logs;
    }

    private static int[] extractIds2fields(String line){
        int[] ids = new int[2];
        // String to be scanned to find the pattern.
        String pattern = "'([0-9]+)'.+'([0-9]+)'";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(line);

        if (m.find()) {
            ids[0] = Integer.parseInt(m.group(1)); //User ID
            ids[1] = Integer.parseInt(m.group(2)); //CourseID
        }
        return ids;
    }

    private static int[] extractIds3fields(String line){
        int[] ids = new int[3];
        // String to be scanned to find the pattern.
        String pattern = "'([0-9]+)'.+'([0-9]+)'.+'([0-9]+)'";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(line);

        if (m.find()) {
            ids[0] = Integer.parseInt(m.group(1)); //User ID
            ids[1] = Integer.parseInt(m.group(2)); //StudentID
            ids[2] = Integer.parseInt(m.group(3)); //CourseID
        }
        return ids;
    }
}
