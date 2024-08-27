package com.tyut.shopping_search_service.repository;

import com.tyut.shopping_common.pojo.GoodsES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @version v1.0
 * @author OldGj 2024/6/17
 * @apiNote 商品搜索es持久层
 */
@Repository
public interface GoodsESRepository extends ElasticsearchRepository<GoodsES, Long> {
}
