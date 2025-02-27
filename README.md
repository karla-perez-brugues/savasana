# Savasana
This application allows admins to create and manage yoga sessions. Users can create an account and participate in the available yoga sessions.

## Requirements
This application requires :
- Java 11
- NodeJS 16
- Angular CLI 14
- MySQL

Make sure all of the above are installed before going forward.

## Database
Create a new database named `test`.  
Then, at in the `back` folder, create a file named `.env` with these properties and fill them according to your configuration : 
- `SPRING_DATASOURCE_URL` (you should set `allowPublicKeyRetrieval` to `true`)
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`

Then, you can import the SQL file located in the folder sql, itself in the folder ressources.

## Backend 
Install Maven dependencies
> mvn clean install

Run development server
> mvn spring-boot:run

Execute unit tests (coverage report available here back/target/site/jacoco/index.html)
> mvn clean test

## Frontend
Install Node dependencies
> npm install 

Run development server
> ng serve

Execute unit tests
> npm run test

Generate coverage report (available here front/coverage/jest/lcov-report/index.html)
> npm jest --coverage

Execute end-to-end tests
> npm run e2e  

Generate coverage report (available here front/coverage/lcov-report/index.html)
> npm run e2e:coverage


