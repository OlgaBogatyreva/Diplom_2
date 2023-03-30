package client;

import client.base.BurgerRestClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Order;
import model.orderList.OrdersList;

import static client.base.BurgerRestClient.BASE_URI;
import static io.restassured.RestAssured.given;

public class OrderSteps extends BurgerRestClient {

    private static final String ORDER_URI = BASE_URI + "/orders";

    @Step("Create order {order}")
    public ValidatableResponse create(Order order, String token){
        return given()
                .spec(getBaseReqSpec())
                .headers("authorization", token)
                .and()
                .body(order)
                .when()
                .post(ORDER_URI)
                .then();
    }

    @Step("Get orders list")
    public OrdersList get(String token){
        return given()
                .header("Content-type", "application/json")
                .headers("authorization", token)
                .get(ORDER_URI)
                .body()
                .as(OrdersList.class);
    }
}
