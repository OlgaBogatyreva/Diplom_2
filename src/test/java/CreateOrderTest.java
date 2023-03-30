import client.OrderSteps;
import client.UserSteps;
import io.restassured.response.ValidatableResponse;
import model.Auth;
import model.Order;
import model.User;
import model.generators.UserGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateOrderTest {

    private OrderSteps orderSteps;
    private UserSteps userSteps;

    @Before
    public void setUp() {
        orderSteps = new OrderSteps();
        userSteps = new UserSteps();
    }

    @Test
    public void UnAuthUserCantCreateOrderWithValidData() { //тест падает, баг, неавторизованный пользовтель может создать заказ
        Order order = new Order(new String[]{"61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa73"});
        ValidatableResponse createResponse = orderSteps.create(order, "");
        int statusCode = createResponse.extract().statusCode();
        String nameOrder = createResponse.extract().path("name");

        assertEquals("Status code is not correct, unauthorized user can create order", 401, statusCode);
        assertTrue(nameOrder != null);
    }

    @Test
    public void createOrderWillBeOkForLoginedUserAndValidData() {
        User user = UserGenerator.getRandom();
        userSteps.create(user);

        Auth auth = new Auth(user.getEmail(), user.getPassword());
        ValidatableResponse createResponseUser = userSteps.login(auth);
        String accessToken = createResponseUser.extract().path("accessToken");

        Order order = new Order(new String[]{"61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa73"});
        ValidatableResponse createResponse = orderSteps.create(order, accessToken);
        int statusCode = createResponse.extract().statusCode();

        assertEquals("Status code is not correct", 200, statusCode);
    }

    @Test
    public void createOrderWontBeOkWithInvalidData() {
        User user = UserGenerator.getRandom();
        userSteps.create(user);

        Auth auth = new Auth(user.getEmail(), user.getPassword());
        ValidatableResponse createResponseUser = userSteps.login(auth);
        String accessToken = createResponseUser.extract().path("accessToken");

        Order order = new Order(new String[]{"invalidData61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa73"});
        ValidatableResponse createResponse = orderSteps.create(order, accessToken);
        int statusCode = createResponse.extract().statusCode();

        assertEquals("Status code is not correct", 500, statusCode);
    }

    @Test
    public void createOrderWontBeOkWithoutData() {
        User user = UserGenerator.getRandom();
        userSteps.create(user);

        Auth auth = new Auth(user.getEmail(), user.getPassword());
        ValidatableResponse createResponseUser = userSteps.login(auth);
        String accessToken = createResponseUser.extract().path("accessToken");

        Order order = new Order(new String[]{null});
        ValidatableResponse createResponse = orderSteps.create(order, accessToken);
        int statusCode = createResponse.extract().statusCode();

        assertEquals("Status code is not correct", 400, statusCode);
    }
}
