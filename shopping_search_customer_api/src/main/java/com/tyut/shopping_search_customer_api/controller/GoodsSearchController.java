package com.tyut.shopping_search_customer_api.controller;

import com.alibaba.fastjson2.JSON;
import com.tyut.shopping_common.pojo.GoodsDesc;
import com.tyut.shopping_common.pojo.GoodsSearchParam;
import com.tyut.shopping_common.pojo.GoodsSearchResult;
import com.tyut.shopping_common.result.BaseResult;
import com.tyut.shopping_common.service.GoodsService;
import com.tyut.shopping_common.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @version v1.0
 * @author OldGj 2024/6/17
 * @apiNote 商品搜索控制层
 */
@RestController
@RequestMapping("/user/goodsSearch")
@Slf4j
public class GoodsSearchController {


    @DubboReference
    private SearchService searchService; // 搜索相关接口
    @DubboReference
    private GoodsService goodsService; // 商品相关接口


    /**
     * 自动补全关键字
     * @param keyword 被补齐的词
     * @return 补齐的关联词集合
     */
    @GetMapping("/autoSuggest")
    public BaseResult<List<String>> autoSuggest(String keyword) {

        List<String> autoSuggest = searchService.autoSuggest(keyword);
        return BaseResult.ok(autoSuggest);
    }




    /**
     * 商品搜索
     * @param goodsSearchParam 搜索条件
     * @return 搜索结果
     */
    @PostMapping("/search")
    public BaseResult<GoodsSearchResult> search(@RequestBody GoodsSearchParam goodsSearchParam) {
        log.info("商品搜索：{}", JSON.toJSONString(goodsSearchParam));
        GoodsSearchResult goodsSearchResult = searchService.search(goodsSearchParam);
        return BaseResult.ok(goodsSearchResult);
    }

    

    /**
     * 根据商品ID查询商品详情
     * @param id
     * @return
     */
    @GetMapping("/findDesc")
    public BaseResult<GoodsDesc> findDesc(Long id) {
        GoodsDesc goodsDesc = goodsService.findGoodsDescById(id);
        return BaseResult.ok(goodsDesc);
    }

}
