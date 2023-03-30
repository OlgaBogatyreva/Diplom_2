import client.UserSteps;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.generators.UserGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RegistrationUserTest {

    private UserSteps userSteps;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @Test
    public void userCanBeCreatedWithValidData() {
        User user = UserGenerator.getRandom();
        ValidatableResponse createResponse = userSteps.create(user);
        int statusCode = createResponse.extract().statusCode();
        String accessToken = createResponse.extract().path("accessToken");
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
        assertEquals("Status code is incorrect", 403, statusCode);
    }

    @Test
    public void userCanNotBeCreatedWithoutEmail() {
        User user = UserGenerator.getRandom();
        user.setEmail(null);
        ValidatableResponse createResponse = userSteps.create(user);
        int statusCode = createResponse.extract().statusCode();
        assertEquals("Status code is incorrect", 403, statusCode);
    }

}
