package client.base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BurgerRestClient {
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site/api";

    protected RequestSpecification getBaseReqSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBasePath(BASE_URI)
                .build();
    }
}
