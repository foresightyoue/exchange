package com.huagu.vcoin.main.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huagu.vcoin.main.dao.FuserReceiptDAO;
import com.huagu.vcoin.main.model.FuserReceipt;

@Service
public class FrontUserReceiptService {
	
	@Autowired
	private FuserReceiptDAO fuserReceiptDAO;
	
	public List<FuserReceipt> list(int firstResult, int maxResults, String filter, boolean isFY){
		List<FuserReceipt> list = fuserReceiptDAO.list(firstResult, maxResults, filter, isFY);
		return list;
	}
	
	public List<FuserReceipt> findAll(){
		return fuserReceiptDAO.findAll();
	}
	
	public FuserReceipt findById(Integer id){
		return this.fuserReceiptDAO.findById(id);
	}
	
	public void save(FuserReceipt fuserReceipt){
		this.fuserReceiptDAO.save(fuserReceipt);
	}
	
	public void saveOrUpdate(FuserReceipt fuserReceipt){
		this.fuserReceiptDAO.saveOrUpdate(fuserReceipt);
	}
	
	public void delect(int id){
		FuserReceipt fr = fuserReceiptDAO.findById(id);
		this.fuserReceiptDAO.delete(fr);
	}
	
	public void update(FuserReceipt fuserReceipt){
		this.fuserReceiptDAO.update(fuserReceipt);
	}
	
}


















