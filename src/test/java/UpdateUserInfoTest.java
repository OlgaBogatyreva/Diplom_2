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

public class UpdateUserInfoTest {

    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @After
    public void clearData() {
        userSteps.delete(accessToken);
    }

    @Test
    public void authorizidedUserCanChangeEmail() {
        User user = UserGenerator.getRandom();
        userSteps.create(user);

        Auth auth = new Auth(user.getEmail(), user.getPassword());
        ValidatableResponse createResponseUser = userSteps.login(auth);
        accessToken = createResponseUser.extract().path("accessToken");

        User userNewEmail = user;
        userNewEmail.setEmail((RandomStringUtils.randomAlphabetic(12) + "@" + RandomStringUtils.randomAlphabetic(6) + ".example"));
        ValidatableResponse createResponse = userSteps.update(userNewEmail, accessToken);


        int statusCode = createResponse.extract().statusCode();
        Boolean success = createResponse.extract().path("success");
        assertEquals("Status code is incorrect", 200, statusCode);
        assertEquals("Update is not success", true, success);
    }

    @Test
    public void authorizidedUserCanChangePwd() {
        User user = UserGenerator.getRandom();
        userSteps.create(user);

        Auth auth = new Auth(user.getEmail(), user.getPassword());
        ValidatableResponse createResponseUser = userSteps.login(auth);
        accessToken = createResponseUser.extract().path("accessToken");

        User userNewPwd = user;
        userNewPwd.setPassword(RandomStringUtils.randomAlphabetic(12));
        ValidatableResponse createResponse = userSteps.update(userNewPwd, accessToken);

        int statusCode = createResponse.extract().statusCode();
        Boolean success = createResponse.extract().path("success");
        assertEquals("Status code is incorrect", 200, statusCode);
        assertEquals("Update is not success", true, success);
    }

    @Test
    public void authorizidedUserCanChangeName() {
        User user = UserGenerator.getRandom();
        userSteps.create(user);

        Auth auth = new Auth(user.getEmail(), user.getPassword());
        ValidatableResponse createResponseUser = userSteps.login(auth);
        accessToken = createResponseUser.extract().path("accessToken");

        User userNewName = user;
        userNewName.setName(RandomStringUtils.randomAlphabetic(12));
        ValidatableResponse createResponse = userSteps.update(userNewName, accessToken);

        int statusCode = createResponse.extract().statusCode();
        Boolean success = createResponse.extract().path("success");
        assertEquals("Status code is incorrect", 200, statusCode);
        assertEquals("Update is not success", true, success);
    }

    @Test
    public void userCanNotUpdateInfoWithoutAuthorization() {
        User user = UserGenerator.getRandom();
        ValidatableResponse createResponseUser = userSteps.create(user);

        accessToken = createResponseUser.extract().path("accessToken");

        User userNewName = user;
        userNewName.setName(RandomStringUtils.randomAlphabetic(12));
        ValidatableResponse createResponse = userSteps.update(userNewName, "");

        int statusCode = createResponse.extract().statusCode();
        String errorMsg = createResponse.extract().path("message");
        assertEquals("Status code is incorrect", 401, statusCode);
        assertEquals("Error message is incorrect", "You should be authorised", errorMsg);
    }

}
