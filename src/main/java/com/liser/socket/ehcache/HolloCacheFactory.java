package com.liser.socket.ehcache;

import com.liser.socket.service.ServiceLocator;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 缓存工具类
 */
public class HolloCacheFactory {

    private static Cache socketCache = (Cache)ServiceLocator.getService("socketCache");
    private static Cache webSocketCache = (Cache)ServiceLocator.getService("webSocketCache");

    public HolloCacheFactory(){

    }

    /**
     * 存入缓存
     * @param key
     * @param value
     * @throws Exception
     */
    public static void putSocketCache(String key, Object value) throws Exception {
        removeSocketCache(key);
        Element element = new Element(key, value);
        socketCache.put(element);
    }

    /**
     * 存入缓存
     * @param key
     * @param value
     * @throws Exception
     */
    public static void putWebSocketCache(String key, Object value) throws Exception {
        removeWebSocketCache(key);
        Element element = new Element(key, value);
        webSocketCache.put(element);
    }

    /**
     * 从缓存中取出数据
     * @param key
     * @return
     */
    public static Object getSocketCache(String key) {
        Element element = socketCache.get(key);
        return element==null?null:element.getObjectValue();
    }

    /**
     * 从缓存中取出数据
     * @param key
     * @return
     */
    public static Object getWebSocketCache(String key) {
        Element element = webSocketCache.get(key);
        return element==null?null:element.getObjectValue();
    }

    /**
     * 通过key移除缓存
     * @param key
     * @throws Exception
     */
    public static void removeSocketCache(String key) throws Exception {
        socketCache.remove(key);
    }

    /**
     * 通过key移除缓存
     * @param key
     * @throws Exception
     */
    public static void removeWebSocketCache(String key) throws Exception {
        webSocketCache.remove(key);
    }

    /**
     * 移除所有的缓存
     * @param key
     * @throws Exception
     */
    public static void removeAllSocketCache(String key) throws Exception {
        socketCache.removeAll();
    }

    /**
     * 移除所有的缓存
     * @param key
     * @throws Exception
     */
    public static void removeAllWebSocketCache(String key) throws Exception {
        webSocketCache.removeAll();
    }

}
