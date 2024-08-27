package com.tyut.shopping_search_service;

import com.tyut.shopping_search_service.service.SearchServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ShoppingSearchServiceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private SearchServiceImpl searchService;
//    @DubboReference
//    private GoodsService goodsService;

    @Test
    void testAnalyze() {
        List<String> analyze = searchService.analyze("我爱百战程序员", "ik_pinyin");
        System.out.println(analyze);
    }


//    /**
//     * 测试将数据库中的商品数据同步到elasticSearch中
//     */
//    @Test
//    void testSyncGoodsToES() {
//        List<GoodsDesc> goodsDescList = goodsService.findAll();
//        for (GoodsDesc goodsDesc : goodsDescList) {
//            // 如果商品是上架状态
//            if (goodsDesc.getIsMarketable()) {
//                searchService.syncGoodsToES(goodsDesc);
//            }
//        }
//    }

}
