package com.nefu.project.base.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nefu.project.base.mapper.IProductManageMapper;
import com.nefu.project.base.service.IProductCacheService;
import com.nefu.project.domain.entity.Product;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductCacheImpl implements IProductCacheService {
    private static final String CACHE_KEY_PREFIX = "product:exists:";
    private static final long CACHE_EXPIRE_TIME = 24; // 缓存过期时间（小时）
    private static final TimeUnit CACHE_TIME_UNIT = TimeUnit.HOURS;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IProductManageMapper productMapper;

    /**
     * 批量检查商品是否存在（优先从缓存读取，未命中则查数据库）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Boolean> batchCheckProductExists(List<String> productUuids) {
        Map<String, Boolean> resultMap = new HashMap<>();
        List<String> missingFromCache = new ArrayList<>();

        // 1. 先从缓存获取已存在的商品
        for (String uuid : productUuids) {
            String cacheKey = CACHE_KEY_PREFIX + uuid;
            String cachedValue = stringRedisTemplate.opsForValue().get(cacheKey);

            if ("true".equals(cachedValue)) {
                resultMap.put(uuid, true);
            } else if ("false".equals(cachedValue)) {
                resultMap.put(uuid, false);
            } else {
                missingFromCache.add(uuid);
            }
        }
        log.debug("redis已经存在的resultMap: {}", resultMap);
        log.debug("missingFromCache:{}", missingFromCache);
        // 2. 缓存未命中的商品查数据库
        if (!missingFromCache.isEmpty()) {
            List<Product> existingProducts = productMapper.selectList(
                    new LambdaQueryWrapper<Product>()
                            .select(Product::getProductUuid)  // 只查询 UUID 字段
                            .in(Product::getProductUuid, missingFromCache)  //只查询未命中的
                            .eq(Product::getProductStatus, "enabled") // 只查询上架商品
            );
            log.debug("数据库查询没有的existingProducts:{}", existingProducts);
            Set<String> existingSet = existingProducts.stream()
                    .map(Product::getProductUuid)
                    .collect(Collectors.toSet());
            // 3. 更新缓存（
            Map<String, String> cacheMap = new HashMap<>();
            for (String uuid : missingFromCache) {
                boolean exists = existingSet.contains(uuid);
                log.debug("uuid:{}", uuid);
                log.debug("exists:{}", exists);
                resultMap.put(uuid, exists);
                cacheMap.put(CACHE_KEY_PREFIX + uuid, String.valueOf(exists));
            }

            // 使用Redis Pipeline批量写入缓存
            stringRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                Random random = new Random();
                for (Map.Entry<String, String> entry : cacheMap.entrySet()) {
                    connection.setEx(
                            entry.getKey().getBytes(),
                            CACHE_EXPIRE_TIME * 3600+random.nextInt(10),
                            entry.getValue().getBytes()
                    );
                }
                return null;
            });

        }
        log.debug("resultMap: {}", resultMap);
        return resultMap;
    }
    /**
     * 加入单个商品缓存
     */
    @Override
    public void addProductToCache(String productUuid) {

    }

    /**
     * 清除商品缓存
     */
    @Override
    public void clearProductCache(String productUuid) {
        stringRedisTemplate.delete(CACHE_KEY_PREFIX + productUuid);
    }

    /**
     * 批量清除商品缓存
     */
    @Override
    public void clearProductCacheBatch(List<String> productUuids) {
        List<String> keys = productUuids.stream()
                .map(uuid -> CACHE_KEY_PREFIX + uuid)
                .collect(Collectors.toList());
        stringRedisTemplate.delete(keys);
    }
}
