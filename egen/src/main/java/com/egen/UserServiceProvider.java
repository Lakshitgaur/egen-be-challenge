package com.egen;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Spark;

import static com.egen.JsonUtil.*;

public class UserServiceProvider {
	private final Logger logger= LoggerFactory.getLogger(UserServiceProvider.class);
	//final String database="codingDB";
	//final String UserCollection="usercollection";
	public UserServiceProvider(UserService userservice){
		
		Spark.post("/createUser","application/json",(req,res) -> userservice.createUser(req.body()));
	
		Spark.get("/getAllUsers","application/json",(req,res) -> userservice.getAllUsers());
		
		Spark.put("/updateUser","application/json",(req,res) -> 
		{
			Document user=userservice.updateUser(req.body());
			if(user!=null){
				logger.info("User updated successfully");
				return req;
			}
			res.status(404);
			//return res;
			return new ResponseError("user not found with the requested body","abc");
			
			
		},json());
	
	}
}
