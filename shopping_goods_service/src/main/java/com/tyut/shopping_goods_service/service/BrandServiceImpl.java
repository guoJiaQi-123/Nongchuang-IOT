package com.tyut.shopping_goods_service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_common.pojo.Brand;
import com.tyut.shopping_common.service.BrandService;
import com.tyut.shopping_goods_service.mapper.BrandMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/5/31
 * @apiNote 品牌相关业务层实现类
 */
@DubboService
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 根据ID查询品牌
     * @param id
     * @return
     */
    @Override
    public Brand findById(Long id) {
        return brandMapper.selectById(id);
    }

    /**
     * 查询所有品牌
     * @return
     */
    @Override
    public List<Brand> findAllBrand() {
        return brandMapper.selectList(null);
    }

    /**
     * 新增品牌
     * @param brand
     */
    @Override
    public void add(Brand brand) {
        brandMapper.insert(brand);
    }

    /**
     * 修改品牌
     * @param brand
     */
    @Override
    public void update(Brand brand) {
        brandMapper.updateById(brand);
    }

    /**
     * 根据ID删除品牌
     * @param bid
     */
    @Override
    public void delete(Long bid) {
        brandMapper.deleteById(bid);
    }

    /**
     * 品牌分页查询
     * @param page
     * @param size
     * @param brand
     * @return
     */
    @Override
    public Page<Brand> search(int page, int size, Brand brand) {
        LambdaQueryWrapper<Brand> queryWrapper = null;
        if (brand != null && StringUtils.hasText(brand.getName())) {
            LambdaQueryWrapper<Brand> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            queryWrapper = lambdaQueryWrapper.like(Brand::getName, brand.getName());
        }
        return brandMapper.selectPage(new Page<>(page, size), queryWrapper);
    }
}
