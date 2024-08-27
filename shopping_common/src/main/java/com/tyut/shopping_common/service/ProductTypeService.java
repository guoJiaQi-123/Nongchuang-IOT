package com.tyut.shopping_common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_common.pojo.ProductType;

import java.util.List;

// 商品类型
public interface ProductTypeService {
    /**
     * 新增商品类型
     * @param productType
     */
    void add(ProductType productType);

    /**
     * 修改商品类型
     * @param productType
     */
    void update(ProductType productType);

    /**
     * 根据id查询商品类型
     * @param id
     * @return
     */
    ProductType findById(Long id);

    /**
     * 删除商品类型
     * @param id
     */
    void delete(Long id);

    /**
     * 分页查询
     * @param productType
     * @param page
     * @param size
     * @return
     */
    Page<ProductType> search(ProductType productType, int page, int size);

    /**
     * 根据条件查询商品类型
     * @param productType
     * @return
     */
    List<ProductType> findProductType(ProductType productType);
}