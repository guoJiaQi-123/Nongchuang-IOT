package com.tyut.shopping_manager_api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_common.pojo.Specification;
import com.tyut.shopping_common.pojo.SpecificationOptions;
import com.tyut.shopping_common.result.BaseResult;
import com.tyut.shopping_common.service.SpecificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/6/5
 * @apiNote 商品规格相关控制层
 */
@RestController
@RequestMapping("/specification")
@Slf4j
public class SpecificationController {

    @DubboReference
    private SpecificationService specificationService;


    /**
     * 新增商品规格
     * @param specification
     * @return
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody Specification specification) {
        log.info("新增商品规格：{}", specification);
        specificationService.add(specification);
        return BaseResult.ok();
    }

    /**
     * 修改商品规格
     * @param specification
     * @return
     */
    @PutMapping("/update")
    public BaseResult update(@RequestBody Specification specification) {
        specificationService.update(specification);
        return BaseResult.ok();
    }

    /**
     * 删除商品规格
     * @param ids
     * @return
     */
    @DeleteMapping("/delete")
    public BaseResult delete(Long[] ids) {
        specificationService.delete(ids);
        return BaseResult.ok();
    }

    /**
     * 根据id查询商品规格
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public BaseResult<Specification> findById(Long id) {
        Specification specification = specificationService.findById(id);
        return BaseResult.ok(specification);
    }

    /**
     * 分页查询商品规格
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search")
    public BaseResult<Page<Specification>> search(int page, int size) {
        Page<Specification> search = specificationService.search(page, size);
        return BaseResult.ok(search);
    }


    /**
     * 查询某种商品类型下的所有规格
     * @param id 商品类型id
     * @return
     */
    @GetMapping("/findByProductTypeId")
    public BaseResult<List<Specification>> findByProductTypeId(Long id) {
        List<Specification> specificationList = specificationService.findByProductTypeId(id);
        return BaseResult.ok(specificationList);
    }


    /**
     * 新增商品规格项
     * @param specificationOptions
     * @return
     */
    @PostMapping("/addOption")
    public BaseResult addOption(@RequestBody SpecificationOptions specificationOptions) {
        specificationService.addOption(specificationOptions);
        return BaseResult.ok();
    }

    /**
     * 删除商品规格项
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteOption")
    public BaseResult deleteOption(Long[] ids) {
        specificationService.deleteOption(ids);
        return BaseResult.ok();
    }


}
