package com.noodle.noodle.DefMeAlgorithm;

import ca.pfv.spmf.input.transaction_database_list_integers.TransactionDatabase;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        AlgoDefMe adm = new AlgoDefMe();
        TransactionDatabase tb = new TransactionDatabase();
        tb.loadFile("C:\\Users\\appar\\Desktop\\Stuff\\NoodleProject\\test.txt");
        adm.runAlgorithm("C:\\Users\\appar\\Desktop\\Stuff\\NoodleProject\\testOutput.txt",tb,0.1);
    }
}