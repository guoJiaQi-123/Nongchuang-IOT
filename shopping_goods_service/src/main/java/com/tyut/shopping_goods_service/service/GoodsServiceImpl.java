package com.tyut.shopping_goods_service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_common.enu.RocketmqTopicEnum;
import com.tyut.shopping_common.pojo.*;
import com.tyut.shopping_common.service.GoodsService;
import com.tyut.shopping_goods_service.mapper.GoodsImageMapper;
import com.tyut.shopping_goods_service.mapper.GoodsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/6/7
 * @apiNote 商品业务层实现类
 */
@DubboService
//@Transactional
@Slf4j
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsImageMapper goodsImageMapper;
    //    商品服务在更新商品后向ES同步数据需要调用搜索服务，使用rocketMQ优化，降低系统耦合
    //    @DubboReference
    //    private SearchService searchService;
    //  商品服务在更新商品后，需要将商品数据同步到redis中，因为用户的购物车数据保存在redis中，使用rocketMQ优化，降低系统耦合
    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    /**
     * 新增商品
     * @param goods
     */
    @Override
    public void add(Goods goods) {
        // 1.插入商品
        goodsMapper.insert(goods);
        // 2.插入商品图片
        Long goodsId = goods.getId(); // 获取商品主键
        insertImgAndOpt(goods, goodsId);
        GoodsDesc goodsDesc = findGoodsDescById(goodsId);
        // 管理员修改数据后，将数据同步到ES当中
//        searchService.syncGoodsToES(goodsDesc);
        rocketMQTemplate.syncSend(RocketmqTopicEnum.SYNC_GOODS_QUEUE.getTopicName(), goodsDesc);
    }

    /**
     * 插入商品图片和商品规格
     * @param goods
     * @param goodsId
     */
    private void insertImgAndOpt(Goods goods, Long goodsId) {
        List<GoodsImage> goodsImageList = goods.getImages();
        for (GoodsImage goodsImage : goodsImageList) {
            // 给图片设置商品id
            goodsImage.setGoodsId(goodsId);
            //插入图片
            goodsImageMapper.insert(goodsImage);
        }
        // 3.插入商品规格
        List<Specification> specificationList = goods.getSpecifications();
        List<SpecificationOption> options = new ArrayList<>();
        // 遍历规格，获取规格中的所有规格项
        for (Specification specification : specificationList) {
            // 所有规格项
            List<SpecificationOption> optionList = specification.getSpecificationOptions();
            options.addAll(optionList);
        }
        // 3.遍历规格项，插入商品_规格项数据
        for (SpecificationOption option : options) {
            // 插入规格项
            goodsMapper.addGoodsSpecificationOption(goodsId, option.getId());
        }
    }

    /**
     * 更新商品
     * @param goods
     */
    @Override
    public void update(Goods goods) {
        //  1.删除商品图片数据
        Long goodsId = goods.getId();
        LambdaQueryWrapper<GoodsImage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(GoodsImage::getGoodsId, goodsId);
        goodsImageMapper.delete(lambdaQueryWrapper);
        //  2. 删除商品规格数据
        goodsMapper.deleteOption(goodsId);
        //  3.修改商品
        // 更新商品
        goodsMapper.updateById(goods);
        // 插入商品图片
        insertImgAndOpt(goods, goodsId);
        GoodsDesc goodsDesc = findGoodsDescById(goodsId);
        // 管理员修改数据后，将数据同步到ES当中
//        searchService.syncGoodsToES(goodsDesc);
        rocketMQTemplate.syncSend(RocketmqTopicEnum.SYNC_GOODS_QUEUE.getTopicName(), goodsDesc);
        // 管理员修改数据后，将购物车中的数据同步，购物车数据在redis中。
        // 构造商品购物车数据
        CartGoods cartGoods = CartGoods.builder()
                .goodId(goods.getId())
                .goodsName(goods.getGoodsName())
                .headerPic(goods.getHeaderPic())
                .price(goods.getPrice())
                .build();
        // 向消息队列中发送同步消息
        rocketMQTemplate.syncSend(RocketmqTopicEnum.SYNC_CART_QUEUE.getTopicName(), cartGoods);
    }

    /**
     * 根据ID查询商品
     * @param id
     * @return
     */
    @Override
    public Goods findById(Long id) {
        return goodsMapper.findById(id);
    }

    /**
     * 上架/下架商品
     * @param id
     * @param isMarketable
     */
    @Override
    public void isMarketable(Long id, boolean isMarketable) {
        LambdaUpdateWrapper<Goods> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Goods::getId, id);
        lambdaUpdateWrapper.set(Goods::getIsMarketable, isMarketable);
        goodsMapper.update(lambdaUpdateWrapper);
        if (isMarketable) {
            GoodsDesc goodsDesc = findGoodsDescById(id);
            // 管理员修改数据后，将数据同步到ES当中
//            searchService.syncGoodsToES(goodsDesc);
            rocketMQTemplate.syncSend(RocketmqTopicEnum.SYNC_GOODS_QUEUE.getTopicName(), goodsDesc);
        } else {
            // 下架商品，将商品从ES中删除
//            searchService.delete(id);
            rocketMQTemplate.syncSend(RocketmqTopicEnum.DEL_GOODS_QUEUE.getTopicName(), id);
            // 下架商品，将商品从购物车中删除
            rocketMQTemplate.syncSend(RocketmqTopicEnum.DEL_CART_QUEUE.getTopicName(), id);
        }

    }

    /**
     * 分页查询商品
     * @param goods
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Goods> search(Goods goods, int page, int size) {
        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 判断商品名不为空
        if (goods != null && StringUtils.hasText(goods.getGoodsName())) {
            lambdaQueryWrapper.like(Goods::getGoodsName, goods.getGoodsName());
        }
        return goodsMapper.selectPage(new Page<>(page, size), lambdaQueryWrapper);
    }

    /**
     * 查询所有商品详情
     * @return
     */
    @Override
    public List<GoodsDesc> findAll() {
        return goodsMapper.findAll();
    }

    /**
     * 根据ID搜索商品详情
     * @param id
     * @return
     */
    @Override
    public GoodsDesc findGoodsDescById(Long id) {
        return goodsMapper.findGoodsDescById(id);
    }
}
