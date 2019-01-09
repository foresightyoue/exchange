package com.huagu.vcoin.main.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huagu.vcoin.main.dao.FaboutDAO;
import com.huagu.vcoin.main.dao.FappVersionDao;
import com.huagu.vcoin.main.dao.FarticleDAO;
import com.huagu.vcoin.main.dao.FarticletypeDAO;
import com.huagu.vcoin.main.dao.FbankinDAO;
import com.huagu.vcoin.main.dao.FcapitaloperationDAO;
import com.huagu.vcoin.main.dao.FentrustlogDAO;
import com.huagu.vcoin.main.dao.FfriendlinkDAO;
import com.huagu.vcoin.main.dao.FintrolinfoDAO;
import com.huagu.vcoin.main.dao.FmessageDAO;
import com.huagu.vcoin.main.dao.FperiodDAO;
import com.huagu.vcoin.main.dao.FscoreDAO;
import com.huagu.vcoin.main.dao.FuserDAO;
import com.huagu.vcoin.main.dao.FvirtualcointypeDAO;
import com.huagu.vcoin.main.dao.FvirtualwalletDAO;
import com.huagu.vcoin.main.model.Fabout;
import com.huagu.vcoin.main.model.FappVersion;
import com.huagu.vcoin.main.model.Farticle;
import com.huagu.vcoin.main.model.Farticletype;
import com.huagu.vcoin.main.model.Fbankin;
import com.huagu.vcoin.main.model.Ffriendlink;
import com.huagu.vcoin.main.model.Fperiod;
import com.huagu.vcoin.main.service.admin.SystemArgsService;

@Service
public class FrontOthersService {
    @Autowired
    private FfriendlinkDAO ffriendlinkDAO;
    @Autowired
    private FarticleDAO farticleDAO;
    @Autowired
    private FaboutDAO faboutDAO;
    @Autowired
    private FarticletypeDAO farticletypeDAO;
    @Autowired
    private FperiodDAO fperiodDAO;
    @Autowired
    private FentrustlogDAO fentrustlogDAO;
    @Autowired
    private FbankinDAO fbankinDAO;
    @Autowired
    private FuserDAO fuserDao;
    @Autowired
    private FscoreDAO fscoreDAO;
    @Autowired
    private FintrolinfoDAO fintrolinfoDAO;
    @Autowired
    private SystemArgsService systemArgsService;
    @Autowired
    private FcapitaloperationDAO fcapitaloperationDAO;
    @Autowired
    private FvirtualcointypeDAO virtualcointypeDAO;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FvirtualwalletDAO fvirtualwalletDAO;
    @Autowired
    private FmessageDAO fmessageDAO;
    @Autowired
    private FappVersionDao fappVersionDao;

    public List<Ffriendlink> findFfriendLink(int type) {
        return this.ffriendlinkDAO.findByProperty("ftype", type);
    }

    public Fabout findFabout(int fid) {
        return this.faboutDAO.findById(fid);
    }

    public List<Fabout> findFaboutAll() {
        return this.faboutDAO.findAll();
    }

    public List<Fabout> findFaboutByProperty(String propertyName, Object value) {
        return this.faboutDAO.findByProperty(propertyName, value);
    }

    public List<Farticle> findFarticle(int farticletype, int firstResult, int maxResult) {
        List<Farticle> farticles = this.farticleDAO.findFarticle(farticletype, firstResult, maxResult);
        return farticles;
    }

    public int findFarticleCount(int farticletype) {
        return this.farticleDAO.findFarticleCount(farticletype);
    }

    public int findFarticle(int farticletype) {
        return this.farticleDAO.findFarticleCount(farticletype);
    }

    public List<Ffriendlink> findFriendLink(int type, int firstResult, int maxResult) {
        return this.ffriendlinkDAO.findFriendLink(type, firstResult, maxResult);
    }

    public Farticle findFarticleById(int id) {
        Farticle farticle = this.farticleDAO.findById(id);
        return farticle;
    }

    public Farticletype findFarticleTypeById(int id) {
        return this.farticletypeDAO.findById(id);
    }

    public List<Farticletype> findFarticleTypeAll() {
        return this.farticletypeDAO.findAll();
    }

    public List<Fperiod> findAllFperiod(long fromTime, int fvirtualCoinType) {
        return this.fperiodDAO.findAllFperiod(fromTime, fvirtualCoinType);
    }

    public void addFperiod(Fperiod fperiod) {
        this.fperiodDAO.save(fperiod);
    }

    public void addFperiods(List<Fperiod> fperiods) {
        for (Fperiod fperiod : fperiods) {
            this.fperiodDAO.save(fperiod);
        }
    }

    public List<FappVersion> findNewVersion(int type) {
        return this.fappVersionDao.findNewVersion("type", type);
    }
    // public Fperiod getLastFperiod(Fvirtualcointype fvirtualcointype){
    // return this.fperiodDAO.getLastFperiod(fvirtualcointype) ;
    // }
    //
    // public Fentrustlog getLastFpeFentrustlog(int fvirtualcointype){
    // return this.fentrustlogDAO.getLastFpeFentrustlog(fvirtualcointype) ;
    // }
    //

    public List<Fbankin> findFbankin() {
        return this.fbankinDAO.findByProperty("ok", 0);
    }

}
