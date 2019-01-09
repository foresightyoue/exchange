package com.huagu.vcoin.main.service.front;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huagu.vcoin.main.dao.UtilsDAO;
import com.huagu.vcoin.main.model.Farticle;
import com.huagu.vcoin.main.model.Fasset;
import com.huagu.vcoin.main.model.Fperiod;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.model.Fvirtualcaptualoperation;
import com.huagu.vcoin.main.model.Fvirtualcointype;

@Service
public class UtilsService {

    @Autowired
    private UtilsDAO utilsDAO;

    public List list(int firstResult, int maxResults, String filter, boolean isFY, Class c, Object... param) {
        return this.utilsDAO.findByParam(firstResult, maxResults, filter, isFY, c, param);
    }

    public List list(int firstResult, int maxResults, String filter, boolean isFY, Class c) {
        return this.utilsDAO.findByParam(firstResult, maxResults, filter, isFY, c);
    }

    public List list_admin(int firstResult, int maxResults, String filter, boolean isFY, Class c) {
        List<Fasset> all = (List<Fasset>) this.utilsDAO.findByParam(firstResult, maxResults, filter, isFY, c);
        for (Fasset fasset : all) {
            if (fasset.getFuser() != null)
                fasset.getFuser().getFnickName();
        }
        return all;
    }

    public int count(String filter, Class c) {
        return this.utilsDAO.findByParamCount(filter, c);
    }

    public double sum(String filter, String field, Class c) {
        return this.utilsDAO.sum(filter, field, c);
    }

    public List<Ftrademapping> list1(int firstResult, int maxResults, String filter, boolean isFY, Class c,
            Object... param) {
        List<Ftrademapping> list = this.utilsDAO.findByParam(firstResult, maxResults, filter, isFY, c, param);
        for (Ftrademapping ftrademapping : list) {
            ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFname();
            ftrademapping.getFvirtualcointypeByFvirtualcointype2().getFname();
        }
        return list;
    }

    public List<Fvirtualcaptualoperation> list3(int firstResult, int maxResults, String filter, boolean isFY, Class c) {
        List<Fvirtualcaptualoperation> list = this.utilsDAO.findByParam(firstResult, maxResults, filter, isFY, c);
        for (Fvirtualcaptualoperation fvirtualcaptualoperation : list) {
            Fuser fuser = fvirtualcaptualoperation.getFuser();
            if (fuser != null) {
                fuser.getFnickName();
            }

            Fvirtualcointype fvirtualcointype = fvirtualcaptualoperation.getFvirtualcointype();
            if (fvirtualcointype != null) {
                fvirtualcointype.getFname();
            }
        }
        return list;
    }

    public List<Farticle> list4(int firstResult, int maxResults, String filter, boolean isFY) {
        List<Farticle> list = this.utilsDAO.findByParam(firstResult, maxResults, filter, isFY, Farticle.class);
        for (Farticle farticle : list) {
            farticle.getFarticletype().getFname();
        }
        return list;
    }

    public List findHQL(int firstResult, int maxResults, String filter, boolean isFY, Class c, Object... param) {
        return this.utilsDAO.findHQL(firstResult, maxResults, filter, isFY, c, param);
    }

    public List findHQLKline(int firstResult, int maxResults, String filter, boolean isFY, Class c, Object... param) {
        return this.utilsDAO.findHQLKline(firstResult, maxResults, filter, isFY, c, param);
    }

    public List<Fperiod> findKlineQuery(Map<String, Object> map, Date datefrom, Date dateto) {
        return this.utilsDAO.findKlineQuery(map, datefrom, dateto);
    }
}
