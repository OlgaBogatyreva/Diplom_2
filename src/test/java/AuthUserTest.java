import client.UserSteps;
import io.restassured.response.ValidatableResponse;
import model.Auth;
import model.User;
import model.generators.UserGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AuthUserTest {

    private UserSteps userSteps;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @Test
    public void userCanLoginWithValidData() {
        User user = UserGenerator.getRandom();
        userSteps.create(user);

        Auth auth = new Auth(user.getEmail(), user.getPassword());
        ValidatableResponse createResponse = userSteps.login(auth);

        int statusCode = createResponse.extract().statusCode();
        assertEquals("Status code is incorrect", 200, statusCode);
    }

    @Test
    public void userCanNotLoginWithInvalidPwd() {
        User user = UserGenerator.getRandom();
        userSteps.create(user);

        Auth auth = new Auth(user.getEmail(), RandomStringUtils.randomAlphabetic(10));
        ValidatableResponse createResponse = userSteps.login(auth);

        int statusCode = createResponse.extract().statusCode();
        assertEquals("Status code is incorrect", 401, statusCode);
    }

    @Test
    public void userCanNotLoginWithInvalidEmail() {
        User user = UserGenerator.getRandom();
        userSteps.create(user);

        Auth auth = new Auth(RandomStringUtils.randomAlphabetic(10), user.getPassword());
        ValidatableResponse createResponse = userSteps.login(auth);

        int statusCode = createResponse.extract().statusCode();
        assertEquals("Status code is incorrect", 401, statusCode);
    }

}
