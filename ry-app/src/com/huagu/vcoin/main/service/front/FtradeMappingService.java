package com.huagu.vcoin.main.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huagu.vcoin.main.Enum.TrademappingStatusEnum;
import com.huagu.vcoin.main.dao.FtrademappingDAO;
import com.huagu.vcoin.main.dao.UtilsDAO;
import com.huagu.vcoin.main.model.Ftrademapping;
import com.huagu.vcoin.main.model.Fvirtualcointype;
import com.huagu.vcoin.main.service.BaseService;

@Service
public class FtradeMappingService extends BaseService {

    @Autowired
    private FtrademappingDAO ftrademappingDAO;
    @Autowired
    private UtilsDAO utilsDAO;

    public Ftrademapping findFtrademapping(int fid) {
        return this.ftrademappingDAO.findById(fid);
    }

    public Ftrademapping findFtrademapping2(int fid) {
        Ftrademapping ftrademapping = this.ftrademappingDAO.findById(fid);
        if (ftrademapping != null) {
            ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFname();
            ftrademapping.getFvirtualcointypeByFvirtualcointype2().getFname();
        }
        return ftrademapping;
    }

    public List<Ftrademapping> list1(int firstResult, int maxResults, String filter, boolean isFY, Class c,
            Object... param) {
        List<Ftrademapping> ftrademappings = this.utilsDAO.findByParam(firstResult, maxResults, filter, isFY, c, param);
        for (Ftrademapping ftrademapping : ftrademappings) {
            ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFname();
            ftrademapping.getFvirtualcointypeByFvirtualcointype2().getFname();
        }
        return ftrademappings;
    }

    // 可以交易的
    public List<Ftrademapping> findActiveTradeMapping() {
        return this.utilsDAO.findByParam(0, 0, " where fstatus=? order by fid asc ", false, Ftrademapping.class,
                TrademappingStatusEnum.ACTIVE);
    }

    // 按法币查询
    public List<Ftrademapping> findActiveTradeMappingByFB(Fvirtualcointype fvirtualcointype) {
        List<Ftrademapping> ftrademappings = this.utilsDAO.findByParam(0, 0,
                " where fvirtualcointypeByFvirtualcointype1.fid=? and fstatus=? order by fid asc ", false,
                Ftrademapping.class, fvirtualcointype.getFid(), TrademappingStatusEnum.ACTIVE);
        for (Ftrademapping ftrademapping : ftrademappings) {
            if(ftrademapping.getFisActive() == null) {
                ftrademapping.setFisActive(false);
            }
            ftrademapping.getFvirtualcointypeByFvirtualcointype1().getFname();
            ftrademapping.getFvirtualcointypeByFvirtualcointype2().getFname();
        }
        return ftrademappings;
    }

}
