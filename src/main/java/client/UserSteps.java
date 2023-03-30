package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import model.Auth;
import model.Order;
import model.User;
import client.base.BurgerRestClient;
import static client.base.BurgerRestClient.BASE_URI;
import static io.restassured.RestAssured.given;

public class UserSteps extends BurgerRestClient {

    private static final String ORDER_URI = BASE_URI + "/auth";

    @Step("Create user {user}")
    public ValidatableResponse create(User user){
        return given()
                .spec(getBaseReqSpec())
                .body(user)
                .when()
                .post(ORDER_URI + "/register")
                .then();
    }

    @Step("Login user {user}")
    public ValidatableResponse login(Auth auth){
        return given()
                .spec(getBaseReqSpec())
                .body(auth)
                .when()
                .post(ORDER_URI + "/login")
                .then();
    }

    @Step("Update user info {user}")
    public ValidatableResponse update(User user, String token){
        return given()
                .spec(getBaseReqSpec())
                .headers("authorization", token)
                .body(user)
                .when()
                .patch(ORDER_URI + "/user")
                .then();
    }

}
