### Introduction

Aquila is an efficiency software which is powered by Spring MVC 3.2, Hibernate, JPA, spring-data. 


### Installation

* `git clone https://github.com/bml3i/aquila.git`

* `cd aquila`

* `mvn eclipse:eclipse`

* `mvn eclipse:clean`


### Run

DEV:

* `mvn clean -P dev install`

* `java -Dspring.profiles.active=dev -jar target/dependency/jetty-runner.jar target/*.war`  

OR

* `mvn clean -Dspring.profiles.active="dev" -P dev tomcat7:run`  


PROD:

* `mvn clean -P prod install`

* `java -Dspring.profiles.active=prod -jar target/dependency/jetty-runner.jar target/*.war`  

OR

* `mvn clean -Dspring.profiles.active="prod" -P prod tomcat7:run`  
