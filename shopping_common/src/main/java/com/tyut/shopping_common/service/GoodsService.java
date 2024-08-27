package com.tyut.shopping_common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_common.pojo.Goods;
import com.tyut.shopping_common.pojo.GoodsDesc;

import java.util.List;

/**
 * @version v1.0
 * @apiNote 商品业务层接口
 * @author OldGj 2024/6/7
 */
public interface GoodsService {

    /**
     * 新增商品
     * @param goods
     */
    void add(Goods goods);

    /**
     * 更新商品
     * @param goods
     */
    void update(Goods goods);


    /**
     * 根据ID查询商品
     * @param id
     * @return
     */
    Goods findById(Long id);


    /**
     * 上架/下架商品
     * @param id
     * @param isMarketable
     */
    void isMarketable(Long id,boolean isMarketable);

    /**
     * 分页查询商品
     * @param goods
     * @param page
     * @param size
     * @return
     */
    Page<Goods> search(Goods goods,int page,int size);

    /**
     * 查询所有商品详情
     * @return
     */
    List<GoodsDesc> findAll();

    /**
     * 根据ID搜索商品详情
     * @param id
     * @return
     */
    GoodsDesc findGoodsDescById(Long id);
}
