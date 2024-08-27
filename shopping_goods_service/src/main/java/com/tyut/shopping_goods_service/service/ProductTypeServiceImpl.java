package com.tyut.shopping_goods_service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_common.pojo.ProductType;
import com.tyut.shopping_common.result.BusException;
import com.tyut.shopping_common.result.CodeEnum;
import com.tyut.shopping_common.service.ProductTypeService;
import com.tyut.shopping_goods_service.mapper.ProductTypeMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/6/5
 * @apiNote 商品类型管理业务层实现类
 */
@DubboService
@Transactional
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    private ProductTypeMapper productTypeMapper;

    /**
     * 新增商品类型
     * @param productType
     */
    @Override
    public void add(ProductType productType) {
        // 根据父类型的级别，来设置当前类型的级别
        determineLevel(productType);
        productTypeMapper.insert(productType);
    }

    /**
     * 设置当前类型级别
     * @param productType
     */
    private void determineLevel(ProductType productType) {
        // 获取父类型ID
        Long parentId = productType.getParentId();
        // 根据ID查询父类型
        ProductType productTypeParent = productTypeMapper.selectById(parentId);
        Integer parentLevel = productTypeParent.getLevel();
        // 根据父类型的级别，来设置当前类型的级别
        if (parentLevel == null || parentLevel == 0) {
            // 如果父级别不存在或者父级别为1，则当前类型为第一级别
            productType.setLevel(1);
        } else if (parentLevel == 3) {
            // 如果父类型级别为3，不可再添加子级别，抛出异常
            throw new BusException(CodeEnum.INSERT_PRODUCT_TYPE_ERROR);
        } else if (parentLevel < 3) {
            // 设置当前类型的级别为父级别+1
            productType.setLevel(parentLevel + 1);
        }
    }

    /**
     * 修改商品类型
     * @param productType
     */
    @Override
    public void update(ProductType productType) {
        // 根据父类型的级别，来设置当前类型的级别
        determineLevel(productType);
        productTypeMapper.updateById(productType);
    }

    /**
     * 根据id查询商品类型
     * @param id
     * @return
     */
    @Override
    public ProductType findById(Long id) {
        return productTypeMapper.selectById(id);
    }

    /**
     * 删除商品类型
     * @param id
     */
    @Override
    public void delete(Long id) {
        // 查询要删除的类型是否存在子类型，如果存在，不能删除当前类型
        LambdaQueryWrapper<ProductType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ProductType::getParentId, id);
        List<ProductType> productTypes = productTypeMapper.selectList(lambdaQueryWrapper);
        // 查询到的集合不为null，说明当前类型存在子类型，无法删除
        if (!CollectionUtils.isEmpty(productTypes)) {
            throw new BusException(CodeEnum.DELETE_PRODUCT_TYPE_ERROR);
        }
        productTypeMapper.deleteById(id);
    }

    /**
     * 分页查询
     * @param productType
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<ProductType> search(ProductType productType, int page, int size) {
        LambdaQueryWrapper<ProductType> lambdaQueryWrapper = buildQueryWrapper(productType);
        return productTypeMapper.selectPage(new Page<>(page, size), lambdaQueryWrapper);
    }

    @Override
    public List<ProductType> findProductType(ProductType productType) {
        LambdaQueryWrapper<ProductType> lambdaQueryWrapper = buildQueryWrapper(productType);
        return productTypeMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 构建查询条件
     * @param productType
     * @return
     */
    private static LambdaQueryWrapper<ProductType> buildQueryWrapper(ProductType productType) {
        LambdaQueryWrapper<ProductType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (productType != null) {
            Long parentId = productType.getParentId();
            String productTypeName = productType.getName();
            // 上级类型id不为空时
            if (parentId != null) {
                lambdaQueryWrapper.eq(ProductType::getParentId, parentId);
            }
            // 类型名不为空时
            if (StringUtils.hasText(productTypeName)) {
                lambdaQueryWrapper.like(ProductType::getName, productTypeName);
            }
        }
        return lambdaQueryWrapper;
    }
}
