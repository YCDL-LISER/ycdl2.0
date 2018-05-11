package com.liser.common.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository定义成一个资源   目的是：在其他地方可以直接自动注入该资源
@Repository("baseDao")
public class BaseDao implements IDao {

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    public Object insert(String sql, Object obj) throws Exception {
        return sqlSessionTemplate.insert(sql, obj);
    }

    public Object delete(String sql, Object obj) throws Exception {
        return sqlSessionTemplate.delete(sql, obj);
    }

    public Object update(String sql, Object obj) throws Exception {
        return sqlSessionTemplate.update(sql, obj);
    }

    public Object queryForObject(String sql, Object obj) throws Exception {
        return sqlSessionTemplate.selectOne(sql, obj);
    }

    public List<?> queryForList(String sql, Object obj) throws Exception {
        return sqlSessionTemplate.selectList(sql,obj);
    }

    public List<?> queryForAllList(String sql) throws Exception {
        return sqlSessionTemplate.selectList(sql);
    }
}
