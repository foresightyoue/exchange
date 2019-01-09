package com.huagu.vcoin.main.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huagu.vcoin.main.dao.AppDao;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.service.BaseService;

@Service
public class FrontApiService extends BaseService {
    @Autowired
    private AppDao appDao;

    public int findTradeRecordllCount(Fuser fuser) {
        return this.appDao.findTradeRecordAllCount(fuser);
    }

    public List<Object[]> findTradeRecordAll(Fuser fuser, int firstResult, int maxResult, boolean isFY) {
        return this.appDao.findTradeRecordAll(fuser, firstResult, maxResult, isFY);
    }
}
