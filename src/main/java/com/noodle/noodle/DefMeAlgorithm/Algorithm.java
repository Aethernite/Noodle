package com.noodle.noodle.DefMeAlgorithm;

import ca.pfv.spmf.algorithms.frequentpatterns.defme.AlgoDefMe;
import ca.pfv.spmf.algorithms.sequenceprediction.ipredict.helpers.Algo;
import ca.pfv.spmf.input.transaction_database_list_integers.TransactionDatabase;
import com.noodle.noodle.Entities.Log;
import com.noodle.noodle.Models.User;
import com.noodle.noodle.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.List;

@Component
public class Algorithm {
    private AlgoDefMe defMe = new AlgoDefMe();
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final LogRepository logRepository;


    @Autowired
    public Algorithm(CourseRepository courseRepository, UserRepository userRepository, StudentRepository studentRepository, GroupRepository groupRepository, LogRepository logRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.logRepository = logRepository;
    }

    public void runAlgorithm() {
        try {
            TransactionDatabase db = new TransactionDatabase();
            db.loadFile("Ids.txt");
            defMe.runAlgorithm("defMeResults.txt", db, 0.00001);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public HashMap<Integer, Integer> getDefMeInformationCourses() {
        HashMap<Integer, Integer> map = new HashMap<>();
        try {
            File file = new File("defMeResults.txt");
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(file));

            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] info = line.split(" #SUP: ");
                map.put(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return map;
    }

    public void extractToFileCourseEditedIds() {
        try {
            File file = new File("Ids.txt");
            FileWriter wr = null;
            wr = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(wr);
            List<Log> list = logRepository.findAllByDescription("Course edited");
            //System.out.println("SIZE: " + list.size());
            for (Log log : list) {
                // System.out.println("COURSE ID: " + log.getCourse().getId());
                int num = log.getCourse().getId();
                bw.write("" + num);
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void extractToFileLogTypesIds() {
        try {
            File file = new File("Ids.txt");
            FileWriter wr = null;
            wr = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(wr);
            List<Log> list = logRepository.findAll();
           for (Log log : list) {
               switch (log.getDescription()) {
                   case "User enrolled in course":
                       bw.write("" + 1);
                       bw.newLine();
                       break;
                   case "Course viewed":
                       bw.write("" + 2);
                       bw.newLine();
                       break;
                   case "Course edited":
                       bw.write("" + 3);
                       bw.newLine();
                       break;
                   case "Students viewed":
                       bw.write("" + 4);
                       bw.newLine();
                       break;
                   case "Student added":
                       bw.write("" + 5);
                       bw.newLine();
                       break;
                   case "Course added":
                       bw.write("" + 6);
                       bw.newLine();
                       break;
                   case "Courses viewed":
                       bw.write("" + 7);
                       bw.newLine();
                       break;
               }

           }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public HashMap<String, Integer> getDefMeInformationLogs() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("User enrolled in course",0);
        map.put("Course viewed",0);
        map.put("Course edited",0);
        map.put("Students viewed",0);
        map.put("Student added",0);
        map.put("Course added",0);
        map.put("Courses viewed", 0);

        try {
            File file = new File("defMeResults.txt");
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(file));

            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] info = line.split(" #SUP: ");
                if(info[0].equals("1")){
                    map.put("User enrolled in course", Integer.parseInt(info[1]));
                }
                else if(info[0].equals("2")){
                    map.put("Course viewed", Integer.parseInt(info[1]));
                }
                else if(info[0].equals("3")){
                    map.put("Course edited", Integer.parseInt(info[1]));
                }
                else if(info[0].equals("4")){
                    map.put("Students viewed", Integer.parseInt(info[1]));
                }
                else if(info[0].equals("5")){
                    map.put("Student added", Integer.parseInt(info[1]));
                }
                else if(info[0].equals("6")){
                    map.put("Course added", Integer.parseInt(info[1]));
                }
                else if(info[0].equals("7")){
                    map.put("Courses viewed", Integer.parseInt(info[1]));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return map;
    }


    public void extractToFileUserIds() {
        try {
            File file = new File("Ids.txt");
            FileWriter wr = null;
            wr = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(wr);
            List<Log> list = logRepository.findAll();
            for (Log log : list) {
                if(log.getUser()!=null){
                    bw.write("" + log.getUser().getId());
                    bw.newLine();
                }
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<Integer, Integer> getDefMeInformationUsers() {
        HashMap<Integer, Integer> map = new HashMap<>();

        try {
            File file = new File("defMeResults.txt");
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(file));

            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] info = line.split(" #SUP: ");
                map.put(Integer.parseInt(info[0]),Integer.parseInt(info[1]));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return map;
    }

}
