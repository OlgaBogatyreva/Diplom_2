package model.generators;

import model.User;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

    public static User getRandom(){
        String email = (RandomStringUtils.randomAlphabetic(12) + "@" + RandomStringUtils.randomAlphabetic(6)+".example");
        String password = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(10);
        return new User(email, password, name);
    }
}
