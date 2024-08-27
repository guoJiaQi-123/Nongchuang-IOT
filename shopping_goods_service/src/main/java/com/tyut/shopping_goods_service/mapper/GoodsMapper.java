package com.tyut.shopping_goods_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyut.shopping_common.pojo.Goods;
import com.tyut.shopping_common.pojo.GoodsDesc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version v1.0
 * @apiNote 商品实体类持久层
 * @author OldGj 2024/6/7
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    // 添加商品_规格项数据
    void addGoodsSpecificationOption(@Param("gid") Long gid, @Param("optionId")Long optionId);


    /**
     * 删除商品规格
     * @param goodsId
     */
    void deleteOption(Long goodsId);

    /**
     * 根据ID查询商品
     * @param gid
     * @return
     */
    Goods findById(Long gid);

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
