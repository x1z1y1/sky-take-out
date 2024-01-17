package com.sky.service.impl;


import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品和对应的口味
     * 这里操作dish和dish_flavor两张表，所以加上Transactional注解，保证事物的原子性
     *
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        //将DTO转成entity对象
        Dish dish = new Dish();
        //对象属性拷贝
        BeanUtils.copyProperties(dishDTO, dish);

        //向菜品表中插入一条数据
        dishMapper.insert(dish);

        //获取insert语句生成的主键值
        Long id = dish.getId();

        //向口味表中插入多条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(id);
            });
            //口味数据不为空
            dishFlavorMapper.insertBatch(flavors);
        }
    }
}