package com.tyut.shopping_goods_service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_common.pojo.Specification;
import com.tyut.shopping_common.pojo.SpecificationOption;
import com.tyut.shopping_common.pojo.SpecificationOptions;
import com.tyut.shopping_common.service.SpecificationService;
import com.tyut.shopping_goods_service.mapper.SpecificationMapper;
import com.tyut.shopping_goods_service.mapper.SpecificationOptionMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/6/5
 * @apiNote 商品规格业务层实现类
 */
@DubboService
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;
    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;

    /**
     * 添加商品规格
     * @param specification
     */
    @Override
    public void add(Specification specification) {
        specificationMapper.insert(specification);
    }

    /**
     * 修改商品规格
     * @param specification
     */
    @Override
    public void update(Specification specification) {
        specificationMapper.updateById(specification);
    }

    /**
     * 删除商品规格
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            // 删除商品规格对应的规格项
            LambdaQueryWrapper<SpecificationOption> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SpecificationOption::getSpecId, id);
            specificationOptionMapper.delete(lambdaQueryWrapper);
            // 删除商品规格
            specificationMapper.deleteById(id);
        }
    }

    /**
     * 根据id查询商品规格
     * @param id
     * @return
     */
    @Override
    public Specification findById(Long id) {
        return specificationMapper.findById(id);
    }

    /**
     * 分页查询商品规格
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Specification> search(int page, int size) {
        return specificationMapper.selectPage(new Page<>(page, size), null);
    }

    /**
     * 查询某种商品类型下的所有规格
     * @param id
     * @return
     */
    @Override
    public List<Specification> findByProductTypeId(Long id) {
        return specificationMapper.findByProductTypeId(id);
    }


    /**
     * 新增商品规格项
     * @param specificationOptions
     */
    @Override
    public void addOption(SpecificationOptions specificationOptions) {
        String[] optionName = specificationOptions.getOptionName();
        Long specId = specificationOptions.getSpecId();
        for (String option : optionName) {
            // 构建规格项
            SpecificationOption specificationOption = SpecificationOption.builder()
                    .optionName(option)
                    .specId(specId)
                    .build();
            // 插入数据库
            specificationOptionMapper.insert(specificationOption);
        }
    }

    /**
     * 删除商品规格项
     * @param ids
     */
    @Override
    public void deleteOption(Long[] ids) {
        specificationOptionMapper.deleteBatchIds(Arrays.asList(ids));
    }
}
