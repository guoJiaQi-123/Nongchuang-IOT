package com.tyut.shopping_manager_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class ShoppingManagerApiApplicationTests {


    @Autowired
    private PasswordEncoder encoder;

    @Test
    void contextLoads() {
        String guojiaqi = encoder.encode("guojiaqi");
        System.out.println(guojiaqi);
    }


}
