package com.huagu.vcoin.main.dao;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.huagu.vcoin.main.dao.comm.HibernateDaoSupport;
import com.huagu.vcoin.main.model.FappVersion;

/**
 * @Description
 * @author Peng
 * @date 2018年3月20日
 */
@Repository
public class FappVersionDao extends HibernateDaoSupport {
    private static final Logger log = LoggerFactory.getLogger(FappVersionDao.class);

    public List<FappVersion> findNewVersion(String propertyName, Object value) {
        try {
            String queryString = "from FappVersion as model where model." + propertyName
                    + "=? order by model.updateTime desc limit 1";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            throw re;
        }
    }
}
