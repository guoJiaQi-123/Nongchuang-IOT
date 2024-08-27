package com.tyut.shopping_common.service;

import com.tyut.shopping_common.pojo.Address;
import com.tyut.shopping_common.pojo.Area;
import com.tyut.shopping_common.pojo.City;
import com.tyut.shopping_common.pojo.Province;

import java.util.List;

public interface AddressService {
    /**
     * 查询所有省份
     * @return
     */
    List<Province> findAllProvince();

    /**
     * 查询省份下的城市
     * @param provinceId
     * @return
     */
    List<City> findCityByProvince(Long provinceId);

    /**
     * 查询城市下的区县
     * @param cityId
     * @return
     */
    List<Area> findAreaByCity(Long cityId);

    /**
     * 增加地址
     * @param address
     */
    void add(Address address);

    /**
     * 修改地址
     * @param address
     */
    void update(Address address);

    /**
     * 根据id获取地址
     * @param id
     * @return
     */
    Address findById(Long id);

    /**
     * 删除地址
     * @param id
     */
    void delete(Long id);

    /**
     * 查询登录用户的所有地址
     * @param userId
     * @return
     */
    List<Address> findByUser(Long userId);
}