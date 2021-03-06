package com.huagu.vcoin.main.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huagu.vcoin.main.dao.FentrustlogDAO;
import com.huagu.vcoin.main.dao.FshareplanlogDAO;
import com.huagu.vcoin.main.model.Fshareplanlog;
import com.huagu.vcoin.main.model.Fuser;
import com.huagu.vcoin.main.service.BaseService;

@Service
public class FrontDivideService extends BaseService {

    @Autowired
    private FentrustlogDAO fentrustlogDAO;
    @Autowired
    private FshareplanlogDAO fshareplanlogDAO;

    public List<Fshareplanlog> findFshareplanlogs(Fuser fuser, int type, int firstResult, int maxResult) {
        return this.fshareplanlogDAO.findByParam(firstResult, maxResult,
                " where fuser.fid=" + fuser.getFid() + " and fshareplan.ftype=" + type + " order by fid desc ", true,
                Fshareplanlog.class);
    }

    public int findFshareplanlogsCount(Fuser fuser, int type) {
        return this.fshareplanlogDAO.findByParamCount(
                " where fuser.fid=" + fuser.getFid() + " and fshareplan.ftype=" + type + " order by fid",
                Fshareplanlog.class);
    }

}
