package egen;

import org.junit.Test;

import com.egen.Main;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.egen.UserService;
import egen.TestData;
import org.bson.Document;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Spark;
import spark.utils.IOUtils;

public class UserServiceTest {
	private static MongoCollection<Document> testusers;
	private static final Logger logger = LoggerFactory
			.getLogger(UserServiceTest.class);
	private static final String TestDB = "testdb";
	private static final String TestCollection = "testcol";
	private static UserService userserviceinstance;
	private static TestData testDatainstance;

	@BeforeClass
	public static void beforeClass() {
		Main.main(null);
		UserServiceTest.logger.info("Testing for the connection to MongoDb");
		UserServiceTest.testusers = UserService.getConnection(
				UserServiceTest.TestDB, UserServiceTest.TestCollection);
		UserServiceTest.userserviceinstance = new UserService(
				UserServiceTest.TestDB, UserServiceTest.TestCollection);
		if (UserServiceTest.userserviceinstance.users == null) {
			Assert.fail("Error retrieving data from Mongo");
		}
		UserServiceTest.logger.info("Connection established");
	}

	@AfterClass
	public static void afterClass() {
		UserServiceTest.logger.info("Dropping the database");
		UserServiceTest.testusers.drop();
		Spark.stop();
		UserServiceTest.logger.info("Testing done");
	}

	@Test
	public void createUser() {
		UserServiceTest.testusers.deleteMany(new Document());
		UserServiceTest.logger.info("Creating 2 users...");
		UserServiceTest.userserviceinstance
				.createUser(testDatainstance.TestUser1);
		UserServiceTest.userserviceinstance
				.createUser(testDatainstance.TestUser2);

		// Validate
		Assert.assertEquals("check if the list size is 2", 2,
				UserServiceTest.userserviceinstance.getAllUsers().size());

		final Document document = UserServiceTest.userserviceinstance
				.getAllUsers().get(0);

		Assert.assertEquals("Fields created as expected", "test1",
				document.getString("firstName"));

	}

	@Test
	public void getAllUsers() {
		UserServiceTest.testusers.deleteMany(new Document());
		UserServiceTest.userserviceinstance
				.createUser(testDatainstance.TestUser1);
		UserServiceTest.userserviceinstance
				.createUser(testDatainstance.TestUser2);
		Assert.assertEquals("Number of users created should be two", 2,
				UserServiceTest.userserviceinstance.getAllUsers().size());

		UserServiceTest.testusers.deleteMany(new Document());
		Assert.assertEquals(
				"Number of users should be 0 after deleting the documents", 0,
				UserServiceTest.userserviceinstance.getAllUsers().size());

	}

	@Test
	public void updateUser() throws Exception {
		UserServiceTest.testusers.deleteMany(new Document());

		// Create a user
		UserServiceTest.logger.info("Creating a user..");
		UserServiceTest.userserviceinstance
				.createUser(testDatainstance.TestUser2);

		// Update the user
		UserServiceTest.logger.info("Updating created user..");
		UserServiceTest.userserviceinstance
				.updateUser(testDatainstance.Test2UpdateUser);

		// Validate update
		final Document document = UserServiceTest.userserviceinstance
				.getAllUsers().get(0);

		Assert.assertEquals("Fields not created as expected", "ron",
				document.getString("firstName"));

	}

}
