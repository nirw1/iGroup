# iGroup Project
A Web project as part of the Integrative Software Development Course at Afeka College.

- This project was developed using the Spring Boot framework and VueJS.
- Make sure to use the POM.XML file that includes all project dependencies.

## How to run the project

1. Download and install Spring Tool Suite from https://spring.io/tools
2. Use the import utility in Spring Tool Suite in order to clone the repository to your local PC.
3. After the import is completed, right click on the project and select **Configure** and then **Convert to Maven Project**
4. Wait until Maven will download all the project's dependencies.
5. Run the project and make sure no errors exist.

## Projects Notes
- The project is using a Postgres database for persistance
- A Postman collection exists for manual integration testing
- The project includes more than 70 JUnit tests for full coverage of automatic integration testing
- VueJS is the project's frontend

## Pipeline
A pipeline has been developed for our CI/CD lifecycle. 
The pipeline is trigger on each Commit Push or Pull Request Merge.

The pipeline includes the following:
- Build automation 
- Maven verfication on the POM.XML file
- JUnit testing 
- A security scan for sensitive data

For each pipeline we get a report of the tests that failed, if any.


## Postgres Database
A Postgres database has been deploy has a **Docker Container** on Digital Ocean cloud platform, together with the pgAdmin tool for DB management.
The docker compose YML is included in the project and is named `postgres-stack.yml`.

- Server IP: afeka-projects.ddns.net
- Postgres Username: root
- Postgress Password: Afeka2021
- Postgres DB name: igroup
- Postgres Port: 5432
- pgAdmin Username: admin@admin.com
- pgAdmin Password: Afeka2021
- pgAdmin Port: 8080
- pdAdmin URL: http://afeka-projects.ddns.net:8080/

### pgAdmin Access
Sign into the pgAdmin with `http://afeka-projects.ddns.net:8080/` and the next credentials:

- **Username:** root
- **Password:** Afeka2021



