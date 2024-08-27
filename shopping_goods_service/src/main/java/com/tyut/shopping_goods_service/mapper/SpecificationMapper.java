package com.tyut.shopping_goods_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyut.shopping_common.pojo.Specification;

import java.util.List;

/**
 * @version v1.0
 * @apiNote 商品规格业务层
 * @author OldGj 2024/6/5
 */
public interface SpecificationMapper extends BaseMapper<Specification> {

    Specification findById(Long id);


    List<Specification> findByProductTypeId(Long productTypeId);
}
