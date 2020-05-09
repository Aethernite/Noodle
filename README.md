# Noodle
Website project using Spring Framework + Relational DB (Hibernate) + Thymeleaf
# Requirements:
- MySQL Workbench server running on 3306 port (Set password in application.properties file)
- HeidiSQL (Optional) to look and easily edit the DB

# SQL 
Login is: **admin/admin**
First you have to add the admin account and the ADMIN role.
Give the admin account the role via middle table.
```sh
INSERT INTO auth_role(role_name)
VALUES ('ADMIN');

INSERT INTO auth_user(password,username,status)
VALUES ('$2a$10$0qRxPWKmW5BH1w1hVFSOtulnPIhUD6tAXX77b6ZjRWpEU1PROEuI2','admin','VERIFIED');

INSERT INTO auth_user_role(auth_user_id,auth_role_id)
VALUES (1,1);
```
### Todos

 - Create the pages using HTML/CSS/JS and Thymeleaf
 - Create the methods in the controller for the pages
 - Create UML diagram
 - Create Sequence diagram
 - Create Documentation
 - Create ER Diagram