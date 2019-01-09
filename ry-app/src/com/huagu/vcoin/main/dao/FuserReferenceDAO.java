package com.huagu.vcoin.main.dao;

import com.huagu.vcoin.main.dao.comm.HibernateDaoSupport;
import com.huagu.vcoin.main.model.FuserReference;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class FuserReferenceDAO extends HibernateDaoSupport {
    private static final Logger log = LoggerFactory.getLogger(FuserReferenceDAO.class);

    public void save(FuserReference transientInstance) {
        log.debug("saving Fuser instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(FuserReference persistentInstance) {
        log.debug("deleting Fuser instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public FuserReference findById(Integer id) {
        log.debug("getting Fuser instance with id: " + id);
        try {
            FuserReference instance = (FuserReference) getSession().get("com.huagu.vcoin.main.model.FuserReference", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding Fuser instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from FuserReference as model where model." + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);

            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List<FuserReference> findByMap(Map<String, Object> param) {
        log.debug("getting Fuser instance with param");
        try {
            StringBuffer queryString = new StringBuffer("from FuserReference as model where ");
            Object[] keys = null;
            if (param != null) {
                keys = param.keySet().toArray();
                for (Object object : keys) {
                    String keystr = (String) object;
                    queryString.append(keystr + "= ? and ");
                }

            }

            queryString.append(" 1=1 ");

            Query queryObject = getSession().createQuery(queryString.toString());
            if (keys != null) {
                for (int i = 0; i < keys.length; i++) {
                    Object value = param.get(keys[i]);
                    queryObject.setParameter(i, value);
                }
            }
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public void attachDirty(FuserReference instance) {
        log.debug("attaching dirty Fuser instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

}
