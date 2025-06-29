package com.nefu.project.base.service;

import java.util.List;
import java.util.Map;

public interface IProductCacheService {
     Map<String, Boolean> batchCheckProductExists(List<String> productUuids);
     void addProductToCache(String productUuid);
     void clearProductCache(String productUuid);
     void clearProductCacheBatch(List<String> productUuids);
}
