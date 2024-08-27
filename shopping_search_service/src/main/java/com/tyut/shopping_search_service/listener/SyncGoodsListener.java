package com.tyut.shopping_search_service.listener;

import com.tyut.shopping_common.pojo.GoodsDesc;
import com.tyut.shopping_common.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @version v1.0
 * @author OldGj 2024/6/20
 * @apiNote 同步商品数据
 */
@Service
@RocketMQMessageListener(topic = "sync_goods_queue", consumerGroup = "sync_goods_group")
@Slf4j
public class SyncGoodsListener implements RocketMQListener<GoodsDesc> {

    @Autowired
    private SearchService searchService;


    /**
     * 消费消息
     * @param goodsDesc
     */
    @Override
    public void onMessage(GoodsDesc goodsDesc) {
        log.debug("同步商品到ES中：{}", goodsDesc);
        searchService.syncGoodsToES(goodsDesc);
    }
}
