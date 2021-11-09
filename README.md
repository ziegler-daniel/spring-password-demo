# Spring Password Demo
This example project shows how you can handle and update old password hashes stored in a database on the fly with Spring Boot Security.

## Create a new user
With curl call (this will use the bcrypt hash function)
`curl -d '{"username": "test", "password": "test"}' -H 'Content-Type: application/json' -X POST http://localhost:8080/api/v1/user/register`

Or you can manually insert the user in the database with a md5 (hex encoded) hash.

## Login
Open [http://localhost:8080](http://localhost:8080) and you will see a login form.

## Password hash update
If a user's password was not hashed with bcrypt, it will be updated upon the next login.
