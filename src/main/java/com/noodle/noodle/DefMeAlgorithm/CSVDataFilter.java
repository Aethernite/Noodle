package com.noodle.noodle.DefMeAlgorithm;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVDataFilter{
    private File input;
    private File output;
    public CSVDataFilter() {
        input = new File("logs_BCS37_20181103.txt");
        output = new File("filteredLogs.txt");
    }

    public void filter() throws IOException {
        FileWriter myWriter = new FileWriter(output);
        String cvsSplitBy = ",";
        BufferedReader br = new BufferedReader(new FileReader(input));
        String line;
        line = br.readLine(); //SKIP FIRST LINE
        int i=1;
        while ((line = br.readLine()) != null) {
            // use comma as separator
            String[] cols = line.split(cvsSplitBy);
            if(line.contains("Course viewed")){
                String date = cols[0].substring(1) +"::" + cols[1].substring(1,cols[1].length()-1);
                //System.out.println(date +"::" + cols[2] + "::" + cols[3] +"::" + cols[4] + "::" + cols[5]);
                myWriter.write(date +"::" + cols[2] + "::" + cols[3] +"::" +  cols[4] + "::" + cols[5] + "\n");
            }
            else if(line.contains("User enrolled")){
                String date = cols[0].substring(1) +"::" + cols[1].substring(1,cols[1].length()-1);
                StringBuilder sb = new StringBuilder(cols[5]);
                int index = sb.lastIndexOf("user");
                int[] ids = getIndexesThroughRegex(line);
                //userIds.add(ids[0]);
                //studentIds.add(ids[1]);
                //courseIds.add(ids[2]);
                sb.replace(index, index+4, "student");
                //System.out.println(date +"::" + cols[2] + "::" + cols[3] +"::"+ cols[4] + "::" + sb.toString());
                myWriter.write(date +"::" + cols[2] + "::" + cols[3] +"::"+ cols[4] + "::" + sb.toString() + "\n");
            }
        }
        myWriter.close();
    }

    private static int[] getIndexesThroughRegex(String line){
        int[] ids = new int[3];
        // String to be scanned to find the pattern.
        String pattern = "'([0-9]+)'.+'([0-9]+)'.+'([0-9]+)";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(line);

        if (m.find()) {
            ids[0] = Integer.parseInt(m.group(1));
            ids[1] = Integer.parseInt(m.group(2));
            ids[2] = Integer.parseInt(m.group(3));
        }
        return ids;
    }
}
