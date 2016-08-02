package com.egen;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import static com.mongodb.client.model.Filters.*;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
	private final Logger logger=LoggerFactory.getLogger(UserService.class);
	private final MongoCollection<Document> users;
	
	
	
	public UserService(final String database,final String UserCollection){
		users=getConnection(database,UserCollection);
	}


	private static MongoCollection<Document> getConnection(final String database, final String UserCollection) {
        final MongoClient client = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase db = client.getDatabase(database);
        MongoCollection<Document> coll = db.getCollection(UserCollection);
        return coll;
    }
	
    public String createUser(String json) {
    	logger.info("parsing the Json data to a document");
        final Document document = Document.parse(json);
       	logger.info("preparing to create a user");
       	users.insertOne(document);
        return "User created successfully";
    }	
    
    public List<Document> getAllUsers() {
        this.logger.info("Preparing to get users");
        final List<Document> documents = new ArrayList<>();
        final MongoCursor<Document> cursor = users.find().iterator();
        logger.info("Adding users to the list");
        while (cursor.hasNext()) {
            documents.add(cursor.next());
        }

        return documents;
    }
    
    public Document updateUser(final String data) throws Exception {
        logger.info("Preparing to update the user");
        Document document = Document.parse(data);
        String id = document.getString("id");
        Document user = users.find(eq("id",id)).first();
        return user;
    }
    
	
    
}
