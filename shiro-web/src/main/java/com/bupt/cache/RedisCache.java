package com.bupt.cache;

import com.bupt.uilt.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

@Component
public class RedisCache<K,V> implements Cache<K,V> {

    @Resource
    private JedisUtil jedisUtil;

    private final String CACHE_PREFIX = "imooc-cache:";

    private byte[] getKey(K k){
        if(k instanceof String){
            return (CACHE_PREFIX + k).getBytes();
        }
        return SerializationUtils.serialize(k);
    }

    //获取
    //这里应该有一个本地缓存，取数据的级别：本地缓存——》redis——》数据库
    @Override
    public V get(K k) throws CacheException {
        System.out.println("从redis获取权限数据");
        byte[] value = jedisUtil.get(getKey(k));
        if(value != null){
            return (V)SerializationUtils.deserialize(value);
        }
        return null;
    }

    //添加
    @Override
    public V put(K k, V v) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = SerializationUtils.serialize(v);
        jedisUtil.set(key, value);
        jedisUtil.expire(key, 1000);
        return v;
    }

    //删除
    @Override
    public V remove(K k) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = jedisUtil.get(key);
        jedisUtil.del(key);
        if(value != null){
            return (V)SerializationUtils.deserialize(value);
        }
        return null;
    }

    //清空所有数据
    @Override
    public void clear() throws CacheException {
        // TODO Auto-generated method stub

    }

    @Override
    public Set<K> keys() {

        return null;
    }

    @Override
    public int size() {

        return 0;
    }

    @Override
    public Collection<V> values() {

        return null;
    }

}

