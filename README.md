# AOP auditing with Mongo database
This project illustrates using of AOP to audit data to MongoDB database.

## Installing Mongo Service
To install MongoDB visit the link below.
https://docs.mongodb.com/manual/installation/

## Initial configuration
1. Install MySQL database
2. Create database `aop-test`

## Project configuration
To run this project you have to run **mongod** service on specified port.
Go to application.yml and set MongoDB credentials - target prefix is **data**.

After configuring **mongod** service you have to create user and to create collection by typing command:
1. `mongo admin -u <username> -p <password> --eval "db.createUser({user: 'your_audit_user', pwd: 'your_audit_password', roles: [{role: 'dbOwner', db: 'audit_db'}]});"`  
2. `mongo -u <your_new_user_username> -p <your_new_user_password> --eval "db = db.getSiblingDB('audit_db'); db.createCollection('journal')";`

Those two commands will create new user (only for auditing) and owner database audit_db. Also, after
creating a database the collection (table) will be created.

## Basic Authentication
For authentication is used Basic Auth with in-memory auth user. Credentials are:
- username: `hedza06`
- password: `hedza123`  

This is only for demo purpose but you can configure Security to use JWT token if you need it.

## REST API calls
To call target REST APIs you can use Postman or some other REST API client
or you can just use **curl** and type in terminal command below:

1. For getting all users: `curl -X GET -i --user hedza06:hedza123 http://localhost:8080/api/user`

2. For creating an user: `curl -X POST --data='{}' -i --user hedza06:hedza123 http://localhost:8080/api/user`

3. For deleting an user: `curl -X DELETE -i --user hedza06:hedza123 http://localhost:8080/api/user/{id}`

## Audit sample
```
{
	"_id" : ObjectId("5d515681a2de9439752da57a"),
	"auditor" : "hedza06",
	"timestamp" : ISODate("2019-08-12T12:07:08.429Z"),
	"route" : "/api/user",
	"method" : "GET",
	"signatureName" : "getAll",
	"signatureDeclaringType" : "com.springaopmongo.auditing.controllers.UserController",
	"actionType" : "VIEW",
	"requestData" : {
		"args" : "[]",
		"remoteAddress" : "0:0:0:0:0:0:0:1"
	},
	"_class" : "com.springaopmongo.auditing.audit.AuditJournal"
}
```

## Contribution
If someone is interesting in contribution please contact me on e-mail ```hedzaprog@gmail.com```. There will be more interesting things to come for Spring Boot especially.

## Author
Heril Muratović   
Software Engineer  
<br>
**Mobile**: +38269657962  
**E-mail**: hedzaprog@gmail.com  
**Skype**: hedza06  
**Twitter**: hedzakirk  
**LinkedIn**: https://www.linkedin.com/in/heril-muratovi%C4%87-021097132/  
**StackOverflow**: https://stackoverflow.com/users/4078505/heril-muratovic