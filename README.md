# Noodle
Web application project using Spring Framework + Relational DB (Hibernate) + Thymeleaf.

# Description
Noodle is an online education system for teachers. Manages courses, students and
logs various activities. The project supports authentication of a teacher using Spring Security.

# Why I chose this as a project?
Currently (16.07.2020) I am studying at Technical University of Sofia. This project was given to me as an
assignment. The purpose of this project was to implement "Itemset Mining" for a certain file filled with various of logs.
When Noodle runs for the first time, it parses this file and loads the logs into the MySQL database.
Using the SPMF(open-source data mining library) I was able to implement the DefMe Algorithm for discovering minimal patterns in set systems.

# Requirements:
- MySQL Workbench server running on 3306 port (Set password in application.properties file)
- HeidiSQL (Optional) to look and easily edit the DB
- IntelliJ IDEA IDE with Java 11 SDK

# SQL 
Login is: **admin**</br>
The login form in the assignment is via barcode.</br>
First you have to add the admin account and the ADMIN role.</br>
Give the admin account the role via middle table.
```sh
INSERT INTO auth_role(role_name)
VALUES ('ADMIN');

INSERT INTO auth_user(password,username,status)
VALUES ('$2a$10$0qRxPWKmW5BH1w1hVFSOtulnPIhUD6tAXX77b6ZjRWpEU1PROEuI2','admin','VERIFIED');

INSERT INTO auth_user_role(auth_user_id,auth_role_id)
VALUES (1,1);
```
