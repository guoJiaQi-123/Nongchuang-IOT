package com.tyut.shopping_manager_api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_common.pojo.Goods;
import com.tyut.shopping_common.result.BaseResult;
import com.tyut.shopping_common.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * @version v1.0
 * @author OldGj 2024/6/7
 * @apiNote 商品相关控制层接口
 */
@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @DubboReference
    private GoodsService goodsService;

    /**
     * 新增商品
     * @param goods
     * @return
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody Goods goods) {
        goodsService.add(goods);
        return BaseResult.ok();
    }

    /**
     * 修改商品
     * @param goods
     * @return
     */
    @PutMapping("/update")
    public BaseResult update(@RequestBody Goods goods) {
        goodsService.update(goods);
        return BaseResult.ok();
    }

    /**
     * 上架/下架商品
     * @param id
     * @param isMarketable
     * @return
     */
    @PutMapping("/putAway")
    public BaseResult putAway(Long id, Boolean isMarketable) {
        goodsService.isMarketable(id, isMarketable);
        return BaseResult.ok();
    }

    /**
     * 根据ID查询商品
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public BaseResult<Goods> findByID(Long id) {
        Goods goods = goodsService.findById(id);
        return BaseResult.ok(goods);
    }

    /**
     * 分页查询商品
     * @param goods
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search")
    public BaseResult<Page<Goods>> search(Goods goods,int page,int size){
        Page<Goods> goodsPage = goodsService.search(goods, page, size);
        return BaseResult.ok(goodsPage);
    }
}
