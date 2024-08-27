package com.tyut.shopping_order_service.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tyut.shopping_common.pojo.Address;
import com.tyut.shopping_common.pojo.Area;
import com.tyut.shopping_common.pojo.City;
import com.tyut.shopping_common.pojo.Province;
import com.tyut.shopping_common.service.AddressService;
import com.tyut.shopping_order_service.mapper.AddressMapper;
import com.tyut.shopping_order_service.mapper.AreaMapper;
import com.tyut.shopping_order_service.mapper.CityMapper;
import com.tyut.shopping_order_service.mapper.ProvinceMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/6/23
 * @apiNote 地址服务实现类
 */
@DubboService
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private ProvinceMapper provinceMapper;


    /**
     * 查询所有省份
     * @return
     */
    @Override
    public List<Province> findAllProvince() {
        return provinceMapper.selectList(null);
    }

    /**
     * 查询省份下的城市
     * @param provinceId
     * @return
     */
    @Override
    public List<City> findCityByProvince(Long provinceId) {
        QueryWrapper<City> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("provinceId", provinceId);
        List<City> cities = cityMapper.selectList(queryWrapper);
        return cities;
    }

    /**
     * 查询城市下的区县
     * @param cityId
     * @return
     */
    @Override
    public List<Area> findAreaByCity(Long cityId) {
        QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cityId", cityId);
        List<Area> areas = areaMapper.selectList(queryWrapper);
        return areas;
    }

    /**
     * 增加地址
     * @param address
     */
    @Override
    public void add(Address address) {
        addressMapper.insert(address);
    }

    /**
     * 修改地址
     * @param address
     */
    @Override
    public void update(Address address) {
        addressMapper.updateById(address);
    }

    /**
     * 根据id获取地址
     * @param id
     * @return
     */
    @Override
    public Address findById(Long id) {
        return addressMapper.selectById(id);
    }

    /**
     * 删除地址
     * @param id
     */
    @Override
    public void delete(Long id) {
        addressMapper.deleteById(id);
    }

    /**
     * 查询登录用户的所有地址
     * @param userId
     * @return
     */
    @Override
    public List<Address> findByUser(Long userId) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper();
        queryWrapper.eq("userId", userId);
        List<Address> addresses = addressMapper.selectList(queryWrapper);
        return addresses;
    }
}
