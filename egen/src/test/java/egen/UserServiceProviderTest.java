package egen;

import static org.junit.Assert.assertEquals;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bson.Document;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Spark;
import spark.utils.IOUtils;

import com.egen.Main;
import com.egen.UserService;
import com.mongodb.client.MongoCollection;

public class UserServiceProviderTest {
	private static final Logger logger = LoggerFactory
			.getLogger(UserServiceProviderTest.class);
	private static MongoCollection<Document> testusers;
	private static final String TestDB = "testdb";
	private static final String TestCollection = "testcol";
	private static UserServiceProviderTest userserviceinstance;
	private static TestData testDatainstance;

	@BeforeClass
	public static void beforeClass() {
		Main.main(null);
		UserServiceProviderTest.logger
				.info("Testing for the connection to MongoDb");
		UserServiceProviderTest.testusers = UserService.getConnection(
				userserviceinstance.TestDB, userserviceinstance.TestCollection);

		if (UserServiceProviderTest.testusers == null) {
			Assert.fail("Error retrieving data from Mongo");
		}
		UserServiceProviderTest.logger.info("Connection established");
	}

	@AfterClass
	public static void afterClass() {
		UserServiceProviderTest.logger.info("Dropping the database");
		UserServiceProviderTest.testusers.drop();
		Spark.stop();
		UserServiceProviderTest.logger.info("Testing done");
	}

	@Test
	public void shouldNotRetrieveUsers() {
		UserServiceProviderTest.testusers.deleteMany(new Document());
		String res = request("/getAllUsers", "", "GET");
		System.out.print(res);
		assertEquals("No User list found", "", res);
	}

	@Test
	public void aNewUserShouldBeCreated() {
		String res = request("/createUser", testDatainstance.TestUser1, "POST");
		assertEquals("User not updated", "User created successfully", res);
	}

	@Test
	public void shouldRetrieveUsers() {
		String res = request("/getAllUsers", "", "GET");
		System.out.print(res);
		assertEquals("User list found", "", res);
	}

	@Test
	public void ShouldUpdateUser() {
		request("/createUser", testDatainstance.TestUser1, "POST");
		String res = request("/updateUser", testDatainstance.Test1UpdateUser,
				"PUT");
		assertEquals("User not updated", testDatainstance.Test1UpdateUser, res);
	}

	private String request(String serviceUrl, String data, String type) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL("http://localhost:4567" + serviceUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(type);
			connection.setRequestProperty("Content-Type", "application/json");

			// check if the data you will be sending is not null
			if (data != null && !data.isEmpty()) {
				connection.setRequestProperty("Content-Length",
						Integer.toString(data.getBytes().length));
				connection.setRequestProperty("Content-Language", "en-US");
				connection.setUseCaches(false);
				connection.setDoOutput(true);
				connection.connect();

				// writing the data in the output stream and sending it.
				final DataOutputStream wr = new DataOutputStream(
						connection.getOutputStream());
				wr.writeBytes(data);
				wr.close();
			}

			// getting the response from the service request.
			InputStream inputStream = null;
			if (connection.getResponseCode() == 200) {
				inputStream = connection.getInputStream();
			} else {
				inputStream = connection.getErrorStream();
			}

			final String body = IOUtils.toString(inputStream);
			return body;
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

	}

}
