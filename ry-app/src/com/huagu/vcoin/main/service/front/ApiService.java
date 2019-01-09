package com.huagu.vcoin.main.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huagu.vcoin.main.dao.UtilsDAO;
import com.huagu.vcoin.main.model.Fapi;

@Service
public class ApiService {

    @Autowired
    private UtilsDAO utilsDAO;

    public Fapi findFapi(String api_key) {
        StringBuffer filter = new StringBuffer(" where fpartner='" + api_key + "'");
        List<Fapi> fapis = this.utilsDAO.findByParam(0, 0, filter.toString(), false, Fapi.class);
        if (fapis.size() == 1) {
            return fapis.get(0);
        }
        return null;
    }
}
