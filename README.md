# Employee managment service
> Service for accounting of employees and their employment on projects. The service allows you to store information about employees, their departments and projects they are working on. The service provides basic CRUD operations for entities, API for retrieving available employees, API for exporting workload reports of employees and API for uploading employees from CSV file. The service secured by Basic Authorization.

# Technologies
- Java 8
- Spring Framework: Core, REST, MVC, ORM, Data, JPA, Security, Test
- Hibernate Framework: ORM, Bean Validation
- Databases: PostgreSQL, Liquibase
- Testing: Spring Test, Mockito, JUnit5
- Security: Basic Authorization, OAuth2
- Logging: log4j2
- Documentation: Swagger 2.9.2
- Project builder: Maven
- Server: Tomcat 9.0.55
- Containers: Docker
- VCS: git
- Cloud: Heroku
- IDE: IntelliJ IDEA Ultimate
- Others: Jackson, AspectJ, poi, opencsv

# Installation
- **Download the project**
- **Build project by maven**
-  **Go to the directory:** ```employeestat/target```
- **Copy archive** ```employeestat.war``` **to the directory:** ```employeestat/src/main/docker/app```
-  **Go to the file:** ```employeestat/webapp/resources/properties/db.properties```
-  **Set the property value** ```jdbcUrl``` **to** ```jdbc:postgresql://db:5432/employee_db```
-  **Install the Docker Desktop** 
-  **Open the command prompt and go to the directory:** ```employeestat/src/main/docker```
-  **Enter the command:** ```docker-compose up```

# Endpoints

### API for exporting workload reports of employees
**GET** ```/api/employees/available``` - Get available employees at the current moment   
**GET** ```/api/employees/available/{days}``` -  Get available employees for the next amount of days

### API for uploading employees from CSV file
**GET** ```/api/employees/upload``` - Upload employees

### Authorization
**POST** ```/api/register``` - Register employee    
**POST** ```/api/reset-password``` - Reset password of employee   

### Department CRUD operations
**GET** ```/api/departments``` - Get list of all departments    
**GET** ```/api/departments{id}``` - Get department by id    
**POST** ```/api/departments``` - Create department   
**PUT** ```/api/departments``` - Update department    
**DELETE** ```/api/departments``` - Delete department    

### Employee CRUD operations
**GET** ```/api/employees``` - Get list of all employees    
**GET** ```/api/employees{id}``` - Get employee by id    
**PUT** ```/api/employees``` - Update employee    
**DELETE** ```/api/employees``` - Delete employee    

### Project CRUD operations
**GET** ```/api/projects``` - Get list of all projects    
**GET** ```/api/projects{id}``` - Get project by id    
**POST** ```/api/projects``` - Create project   
**PUT** ```/api/projects``` - Update project    
**DELETE** ```/api/projects``` - Delete project    

### Work CRUD operations
**GET** ```/api/works``` - Get list of all works    
**GET** ```/api/works/empId={empId}/projId={projId}``` - Get work by employee id and project id        
**PUT** ```/api/works``` - Update work    
**DELETE** ```/api/works/empId={empId}/projId={projId}``` - Delete work    

# Swagger Documentation
**To open the swagger documentation go to URL:** ```http://localhost:8085/swagger-ui.htm```

# Database scheme
![alt text](db.png)

