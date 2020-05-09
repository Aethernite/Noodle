# Noodle
website project using Spring Framework + Relational DB (Hibernate) + Thymeleaf
# Requirements:
MySQL Workbench server running on 3306 port (Set password in application.properties file)
HeidiSQL (Optional) to look and easily edit the DB

SQL: 
INSERT INTO auth_role(role_name)
VALUES ('ADMIN');

INSERT INTO auth_user(password,username,status)
VALUES ('$2a$10$0qRxPWKmW5BH1w1hVFSOtulnPIhUD6tAXX77b6ZjRWpEU1PROEuI2','admin','VERIFIED');


INSERT INTO auth_user_role(auth_user_id,auth_role_id)
VALUES (1,1);

Login is: admin/admin


