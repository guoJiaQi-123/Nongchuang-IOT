package com.tyut.shopping_goods_service;

import com.tyut.shopping_common.pojo.Goods;
import com.tyut.shopping_common.pojo.GoodsDesc;
import com.tyut.shopping_common.service.GoodsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ShoppingGoodsServiceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private GoodsService goodsService;

    /**
     * 测试查询所有商品详情
     */
    @Test
    void testFindAllGoodsDesc() {
        List<GoodsDesc> goodsDescList = goodsService.findAll();
        System.out.println(goodsDescList);
    }

    /**
     * 测试查询所有商品详情
     */
    @Test
    void testFindAllGoodsDesc2() {
        Goods goods = new Goods();
        goods.setId(149187842868021L);
        GoodsDesc goodsDesc = goodsService.findGoodsDescById(goods.getId());
        System.out.println(goodsDesc);
    }

}
