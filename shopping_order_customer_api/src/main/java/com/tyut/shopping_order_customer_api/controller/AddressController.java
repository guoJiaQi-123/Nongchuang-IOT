package com.tyut.shopping_order_customer_api.controller;

import com.tyut.shopping_common.pojo.Address;
import com.tyut.shopping_common.pojo.Area;
import com.tyut.shopping_common.pojo.City;
import com.tyut.shopping_common.pojo.Province;
import com.tyut.shopping_common.result.BaseResult;
import com.tyut.shopping_common.service.AddressService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收货地址
 */
@RestController
@RequestMapping("/user/address")
public class AddressController {
    @DubboReference
    private AddressService addressService;

    /**
   * 查询所有省份
   * @return 所有省份集合
   */
    @GetMapping("/findAllProvince")
    public BaseResult<List<Province>> findAllProvince(){
        List<Province> provinces = addressService.findAllProvince();
        return BaseResult.ok(provinces);
    }


    /**
   * 查询某省下的所有城市
   * @param provinceId 省份id
   * @return 城市集合
   */
    @GetMapping("/findCityByProvince")
    public BaseResult<List<City>> findCityByProvince(Long provinceId){
        List<City> cities = addressService.findCityByProvince(provinceId);
        return BaseResult.ok(cities);
    }


    /**
   * 查询某市下的所有区县
   * @param cityId 城市id
   * @return 区县集合
   */
    @GetMapping("/findAreaByCity")
    public BaseResult<List<Area>> findAreaByCity(Long cityId){
        List<Area> areas = addressService.findAreaByCity(cityId);
        return BaseResult.ok(areas);
    }


    /**
   * 新增地址
   * @param userId 用户id
   * @param address 地址对象
   * @return 执行结果
   */
    @PostMapping("/add")
    public BaseResult add(@RequestHeader Long userId, @RequestBody Address address){
        address.setUserId(userId);
        addressService.add(address);
        return BaseResult.ok();
    }


    /**
   * 删除地址
   * @param userId 用户id
   * @param address 地址对象
   * @return 执行结果
   */
    @PutMapping("/update")
    public BaseResult update(@RequestHeader Long userId, @RequestBody Address address){
        address.setUserId(userId);
        addressService.update(address);
        return BaseResult.ok();
    }


    /**
   * 根据id查询地址
   * @param id 地址id
   * @return 查询结果
   */
    @GetMapping("/findById")
    public BaseResult<Address> findById(Long id){
        Address address = addressService.findById(id);
        return BaseResult.ok(address);
    }


    /**
   * 删除地址
   * @param id 地址id
   * @return 执行结果
   */
    @DeleteMapping("/delete")
    public BaseResult delete(Long id){
        addressService.delete(id);
        return BaseResult.ok();
    }


    /**
   * 查询登录用户的所有地址
   * @param userId 用户id
   * @return 查询结果
   */
    @GetMapping("/findByUser")
    public BaseResult<List<Address>> findByUser(@RequestHeader Long userId){
        List<Address> addresses = addressService.findByUser(userId);
        return BaseResult.ok(addresses);
    }
}