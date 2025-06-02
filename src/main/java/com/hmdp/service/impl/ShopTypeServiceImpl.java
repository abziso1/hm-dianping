package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryTypeList() {
        String shopTypeListJson = stringRedisTemplate.opsForValue().get("cache:shop:type:list");
        if (StrUtil.isNotBlank(shopTypeListJson)) {
            List<ShopType> shopTypeList = JSONUtil.toList(shopTypeListJson, ShopType.class);
            return Result.ok(shopTypeList);
        }
        List<ShopType> shopTypeListp = query().orderByAsc("sort").list();
        if (shopTypeListp == null || shopTypeListp.size() == 0) {
            return Result.fail("店铺类型查询错误");
        }
        stringRedisTemplate.opsForValue().set("cache:shop:type:list", JSONUtil.toJsonStr(shopTypeListp));
        return Result.ok(shopTypeListp);
    }
}
