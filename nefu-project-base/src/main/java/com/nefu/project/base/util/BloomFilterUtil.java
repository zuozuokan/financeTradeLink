package com.nefu.project.base.util;

import cn.hutool.bloomfilter.BitMapBloomFilter;

/**
 * 使用单例模式创建一个布隆过滤器工具类
 */
public class BloomFilterUtil {

    private static final BloomFilterUtil instance = new BloomFilterUtil();
    private static final BitMapBloomFilter fillter = new BitMapBloomFilter(10);

    public static BloomFilterUtil getInstance() {
        return instance;
    }

    public void add(String str) {
        fillter.add(str);
    }

    public boolean contains(String str) {
        return fillter.contains(str);
    }

}
