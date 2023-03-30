import client.OrderSteps;
import client.UserSteps;
import io.restassured.response.ValidatableResponse;
import model.Auth;
import model.Order;
import model.User;
import model.generators.UserGenerator;
import model.orderList.OrdersList;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class GetOrdersList {
    private OrderSteps orderSteps;
    private UserSteps userSteps;

    @Before
    public void setUp() {
        orderSteps = new OrderSteps();
        userSteps = new UserSteps();
    }

    @Test
    public void registeredUserCanGetOrdersList() {
        //юзер регистрируется
        User user = UserGenerator.getRandom();
        ValidatableResponse createResponseUser = userSteps.create(user);

        //юзер авторизуется
        Auth auth = new Auth(user.getEmail(), user.getPassword());
        ValidatableResponse createResponse = userSteps.login(auth);
        String accessToken = createResponse.extract().path("accessToken");

        //создает 4 заказа
        Order order = new Order(new String[]{"61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa73"});
        for (int i = 1; i < 5; i++) {
            orderSteps.create(order, accessToken);
        }

        //получает список заказов, проверка на то, что пришло 4 его заказа
        OrdersList ordersList = orderSteps.get(accessToken);
        MatcherAssert.assertThat(ordersList, notNullValue());
        assertEquals("Count of user's orders is incorrect", ordersList.getOrders().stream().count(), 4);
    }

    @Test
    public void UnregisteredUserCanNotGetOrdersList() {
        OrdersList ordersList = orderSteps.get("");
        assertEquals(ordersList.isSuccess(), false);
    }
}
