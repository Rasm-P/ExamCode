[![Build Status](https://travis-ci.org/Rasm-P/ExamCode.svg?branch=master)](https://travis-ci.org/Rasm-P/ExamCode)

*Projects which are expected to use this code require all, or most of the following technologies:*
 - *JPA and REST*
- *Testing, including database test*
- *Testing, including tests of REST-API's*
- *CI and CONTINUOUS DELIVERY*

### Preconditions
*In order to use this code, you should have a local developer setup + a "matching" droplet on Digital Ocean as described in the 3. semester guidelines*

To set up the project backend to work on your machine and pipeline, change the following:
- pom.xml : Domain name
- config.properties : names of both databases
- .travis.yaml : name of test database
- Travis, environment variables: REMOTE_USER + REMOTE_PW
- rest, @OpenAPIDefinition: Local and remote server url for openapi.
- Droplet, /opt/tomcat/bin/setenv.sh: Change to export CONNECTION_STR="jdbc:mysql://localhost:3306/NAME_OF_YOUR_DB"
- CorsResponseFilter, Access-Control-Allow-Origin: Your frontend deployment
- if you want user functionality: run the createUserRoles.sql script on your non-test database

This project contains two major documentation files: 
 - [First time users - getting started](README_proof_of_concept.md)
 - [How to use for future projects](README_how_to_use.md)