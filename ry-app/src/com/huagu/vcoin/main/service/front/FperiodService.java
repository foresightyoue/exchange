package com.huagu.vcoin.main.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huagu.vcoin.main.dao.FperiodDAO;
import com.huagu.vcoin.main.model.Fperiod;
import com.huagu.vcoin.main.service.BaseService;

@Service
public class FperiodService extends BaseService {

    @Autowired
    private FperiodDAO fperiodDao;

    public List<Fperiod> list(int firstResult, int maxResults, String filter, boolean isFY) {
        List<Fperiod> all = this.fperiodDao.list(firstResult, maxResults, filter, isFY);
        return null;
    }
}
