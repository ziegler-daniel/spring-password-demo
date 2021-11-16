# Spring Password Demo
This example project shows how you can handle and update old password hashes stored in a database on the fly with 
Spring Boot Security.

## Create a new user
You can create a new user with the following curl call:
`curl -d '{"username": "test", "password": "test"}' -H 'Content-Type: application/json' -X POST http://localhost:8080/api/v1/user/register`

This will create the user "test" with password "test". The password encoder used to store the password is defined in 
[application.properties](src/main/resources/application.properties).

## Login
Open [http://localhost:8080](http://localhost:8080) in the browser and you will see a login form.

## Password hash update
If a user's password was not encoded with the current password encoder, it will be updated upon the next login.
