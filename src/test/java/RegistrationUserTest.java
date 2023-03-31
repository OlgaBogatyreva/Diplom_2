import client.UserSteps;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.generators.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RegistrationUserTest {

    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @After
    public void clearData(){
        if (accessToken != null) {
            userSteps.delete(accessToken);
        }
    }

    @Test
    public void userCanBeCreatedWithValidData() {
        User user = UserGenerator.getRandom();
        ValidatableResponse createResponse = userSteps.create(user);
        int statusCode = createResponse.extract().statusCode();
        accessToken = createResponse.extract().path("accessToken");
        String refreshToken = createResponse.extract().path("refreshToken");

        assertEquals("Status code is incorrect", 200, statusCode);
        assertTrue("User accessToken is not created", accessToken != null);
        assertTrue("User refreshToken is not created", refreshToken != null);
    }

    @Test
    public void userCanNotBeCreatedTwice() {
        User user = UserGenerator.getRandom();
        userSteps.create(user);
        ValidatableResponse createResponse = userSteps.create(user);

        int statusCode = createResponse.extract().statusCode();
        String errorMsg = createResponse.extract().path("message");
        assertEquals("Status code is incorrect", 403, statusCode);
        assertEquals("Error message is incorrect", "User already exists", errorMsg);
    }

    @Test
    public void userCanNotBeCreatedWithoutEmail() {
        User user = UserGenerator.getRandom();
        user.setEmail(null);
        ValidatableResponse createResponse = userSteps.create(user);

        int statusCode = createResponse.extract().statusCode();
        String errorMsg = createResponse.extract().path("message");
        assertEquals("Status code is incorrect", 403, statusCode);
        assertEquals("Error message is incorrect", "Email, password and name are required fields", errorMsg);
    }

}
