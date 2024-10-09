package com.tyut.shopping_common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_common.pojo.Brand;

import java.util.List;

/**
 * @version v1.0
 * @apiNote 品牌相关业务层
 * @author OldGj 2024/5/31
 */
public interface BrandService {

    /**
     * 根据ID查询品牌
     * @param id
     * @return
     */
    Brand findById(Long id);

    /**
     * 查询所有品牌
     * @return
     */
    List<Brand> findAllBrand();

    /**
     * 新增品牌
     * @param brand
     */
    void add(Brand brand);

    /**
     * 修改品牌
     * @param brand
     */
    void update(Brand brand);

    /**
     * 根据ID删除品牌
     * @param bid
     */
    void delete(Long bid);

    /**
     * 品牌分页查询
     * @param page
     * @param size
     * @param brand
     * @return
     */
    Page<Brand> search(int page, int size, Brand brand);

}
