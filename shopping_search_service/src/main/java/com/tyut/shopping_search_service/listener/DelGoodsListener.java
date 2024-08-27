package com.tyut.shopping_search_service.listener;

import com.tyut.shopping_common.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @author OldGj 2024/6/20
 * @apiNote 删除商品监听器【监听mq中的消息】
 */
@RocketMQMessageListener(topic = "del_goods_queue", consumerGroup = "del_goods_group")
@Slf4j
@Service
public class DelGoodsListener implements RocketMQListener<Long> {

    @Autowired
    private SearchService searchService;

    /**
     *  消费消息
     * @param id
     */
    @Override
    public void onMessage(Long id) {
        log.debug("删除ES中的商品数据：{}", id);
        searchService.delete(id);
    }
}
