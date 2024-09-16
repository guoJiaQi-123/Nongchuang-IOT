package com.tyut.shopping_category_customer_api.controller;

import com.alibaba.fastjson2.JSON;
import com.tyut.shopping_common.pojo.Category;
import com.tyut.shopping_common.result.BaseResult;
import com.tyut.shopping_common.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;
import java.util.List;


/**
 * @version v1.0
 * @author OldGj 2024/6/11
 * @apiNote 前台用户的广告控制器
 */
@RestController
@RequestMapping("/user/category")
@Slf4j
public class CategoryController {

    @DubboReference
    private CategoryService categoryService;

    /**
     * 查询所有启用的广告
     * @return
     */
    @GetMapping("/all")
    public BaseResult<List<Category>> all() {
        List<Category> categoryList = categoryService.findAll();
        return BaseResult.ok(categoryList);
    }


    /**
     * 测试请求头中是否封装了jwt中加入的参数
     *
     * @param request
     * @return
     */
    @GetMapping("/test")
    public BaseResult<String> test(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        return BaseResult.ok();
    }


}
