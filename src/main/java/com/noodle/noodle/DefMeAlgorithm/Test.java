package com.noodle.noodle.DefMeAlgorithm;

import com.noodle.noodle.Entities.Course;
import com.noodle.noodle.Entities.Log;
import com.noodle.noodle.Models.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\appar\\Desktop\\Stuff\\NoodleProject\\ПТС-проект\\filteredLogs.txt");
        List<Log> logs = extractLogs(file);
       for(Log log: logs){
           System.out.println(log.getTime());
        }
    }
    public static List<Log> extractLogs(File file) throws IOException {
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
                int[] ids = extractIds(line);
                Course course = null;
                //if(courseRepository.existsById(ids[1])){
                //    course = courseRepository.findOneById(ids[1]);
                //}
                log.setCourse(course);
                User user = null;
                //if(userRepository.existsById(ids[0])){
                //    user = userRepository.findOneById(ids[0]);
                //}
                log.setUser(user);
                log.setPlaintext(cols[0] + ", " + cols[1] + ", " + ", " + cols[2] + ", " + cols[3] + cols[4] + ", " + cols[5]);
                logs.add(log);
            }
            else if(line.contains("User enrolled")){
                //
            }
        }
        return logs;
    }
    private static int[] extractIds(String line){
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

