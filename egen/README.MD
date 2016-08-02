**Egen Coding Challenge – Level 1**

**Restful web service for User Management application using **:
•	Spark Java http://sparkjava.com/ and
•	MongoDB

**Assumptions**
•	User has the following properties
{  
   "id":"1630215c-2608-44b9-aad4-9d56d8aafd4c",
   "firstName":"Dorris",
   "lastName":"Keeling",
   "email":"Darby_Leffler68@gmail.com",
   "address":{  
      "street":"193 Talon Valley",
      "city":"South Tate furt",
      "zip":"47069",
      "state":"IA",
      "country":"US"
   },
   "dateCreated":"2016-03-15T07:02:40.896Z",
   "company":{  
      "name":"Denesik Group",
      "website":"http://jodie.org"
   },
   "profilePic":"http://lorempixel.com/640/480/people"
}
•	Use MongoDB as your data store.
•	The User's ID uniquely identifies a user.

**Methods Implemented**

o	createUser
o	getAllUsers
o	updateUser

**Testing Framerwork:** Junit
10 test cases

**Dependencies:**

Java JDK 1.8
Spark Java 2.3
GSON 2.7
MongoDB driver for Java 3.3.0
JUnit for unit testing

**Helpful Information:**

http://www.programcreek.com/java-api-examples/index.php?api=com.mongodb.client.MongoCursor
https://dzone.com/articles/building-simple-restful-api
https://docs.mongodb.com/spark-connector/java-api/
http://stackoverflow.com/questions/30625380/find-in-mongocollectiondocument
http://blog.mongodb.org/post/94065240033/getting-started-with-mongodb-and-java-part-i
