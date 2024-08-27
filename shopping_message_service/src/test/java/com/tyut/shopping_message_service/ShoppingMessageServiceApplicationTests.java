package com.tyut.shopping_message_service;

import com.tyut.shopping_common.result.BaseResult;
import com.tyut.shopping_common.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShoppingMessageServiceApplicationTests {

    @Autowired
    private MessageService messageService;

    @Test
    void contextLoads() {
        BaseResult<String> baseResult = messageService.sendMessage("17200606763", "1314");

        System.out.println(baseResult);

    }

}
