package com.tyut.shopping_manager_api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_common.pojo.Category;
import com.tyut.shopping_common.result.BaseResult;
import com.tyut.shopping_common.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * @version v1.0
 * @author OldGj 2024/6/1
 * @apiNote 广告相关控制层
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @DubboReference
    private CategoryService categoryService;


    /**
     * 分页查询广告
     *
     * @param page 页码
     * @param size 每页条数
     * @return 查询结果
     */
    @GetMapping("/search")
    public BaseResult<Page<Category>> search(int page, int size) {
        Page<Category> page1 = categoryService.search(page, size);
        return BaseResult.ok(page1);
    }


    /**
     * 增加广告
     *
     * @param category 广告对象
     * @return 操作结果
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody Category category) {
        categoryService.add(category);
        return BaseResult.ok();
    }


    /**
     * 修改广告
     *
     * @param category 广告对象
     * @return 操作结果
     */
    @PutMapping("/update")
    public BaseResult update(@RequestBody Category category) {
        categoryService.update(category);
        return BaseResult.ok();
    }


    /**
     * 修改广告状态
     *
     * @param id   广告id
     * @param status 广告状态 0:未启用 1:启用
     * @return 操作结果
     */
    @PutMapping("/updateStatus")
    public BaseResult updateStatus(Long id, Integer status) {
        log.info("修改广告状态：广告ID：{},广告状态：{}", id, status);
        categoryService.updateStatus(id, status);
        return BaseResult.ok();
    }


    /**
     * 根据Id查询广告
     *
     * @param id 广告id
     * @return 查询结果
     */
    @GetMapping("/findById")
    public BaseResult<Category> findById(Long id) {
        Category category = categoryService.findById(id);
        return BaseResult.ok(category);
    }


    /**
     * 删除广告
     *
     * @param ids 广告id集合
     * @return 操作结果
     */
    @DeleteMapping("/delete")
    public BaseResult delete(Long[] ids) {
        categoryService.delete(ids);
        return BaseResult.ok();
    }
}
