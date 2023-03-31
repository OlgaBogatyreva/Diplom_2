import client.UserSteps;
import io.restassured.response.ValidatableResponse;
import model.Auth;
import model.User;
import model.generators.UserGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AuthUserTest {

    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @After
    public void clearData(){
        userSteps.delete(accessToken);
    }

    @Test
    public void userCanLoginWithValidData() {
        User user = UserGenerator.getRandom();
        userSteps.create(user);

        Auth auth = new Auth(user.getEmail(), user.getPassword());
        ValidatableResponse createResponse = userSteps.login(auth);

        accessToken = createResponse.extract().path("accessToken");
        int statusCode = createResponse.extract().statusCode();
        boolean success = createResponse.extract().path("success");
        assertEquals("Status code is incorrect", 200, statusCode);
        assertEquals("Update is not success", true, success);
    }

    @Test
    public void userCanNotLoginWithInvalidPwd() {
        User user = UserGenerator.getRandom();
        ValidatableResponse createResponseUser = userSteps.create(user);
        accessToken = createResponseUser.extract().path("accessToken");

        Auth auth = new Auth(user.getEmail(), RandomStringUtils.randomAlphabetic(10));
        ValidatableResponse createResponse = userSteps.login(auth);

        int statusCode = createResponse.extract().statusCode();
        String errorMsg = createResponse.extract().path("message");
        assertEquals("Status code is incorrect", 401, statusCode);
        assertEquals("Error message is incorrect", "email or password are incorrect", errorMsg);
    }

    @Test
    public void userCanNotLoginWithInvalidEmail() {
        User user = UserGenerator.getRandom();
        ValidatableResponse createResponseUser = userSteps.create(user);
        accessToken = createResponseUser.extract().path("accessToken");

        Auth auth = new Auth(RandomStringUtils.randomAlphabetic(10), user.getPassword());
        ValidatableResponse createResponse = userSteps.login(auth);

        int statusCode = createResponse.extract().statusCode();
        String errorMsg = createResponse.extract().path("message");
        assertEquals("Status code is incorrect", 401, statusCode);
        assertEquals("Error message is incorrect", "email or password are incorrect", errorMsg);
    }
}
