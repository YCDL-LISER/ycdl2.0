package com.liser.common.dao;

import java.util.List;

public interface IDao {

    /**
     * 添加数据
     * @param sql  SQL语句
     * @param obj  参数对象
     * @return
     * @throws Exception
     */
    public Object insert(String sql, Object obj) throws Exception;

    /**
     * 删除数据
     * @param sql  SQL语句
     * @param obj  参数对象
     * @return
     * @throws Exception
     */
    public Object delete(String sql, Object obj) throws Exception;

    /** 更新数据
     * @param sql  SQL语句
     * @param obj  参数对象
     * @return
     * @throws Exception
     */
    public Object update(String sql, Object obj) throws Exception;

    /** 查询一条数据
     * @param sql  SQL语句
     * @param obj  参数对象
     * @return
     * @throws Exception
     */
    public Object queryForObject(String sql, Object obj) throws Exception;

    /** 查询多条数据
     * @param sql  SQL语句
     * @param obj  参数对象
     * @return
     * @throws Exception
     */
    public List<?> queryForList(String sql, Object obj) throws Exception;

    /** 查询全部数据
     * @param sql  SQL语句
     * @return
     * @throws Exception
     */
    public List<?> queryForAllList(String sql) throws Exception;
}
