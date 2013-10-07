package com.aj.evaidya.docreg.bo.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.beans.DocRegResponseBean;
import com.aj.evaidya.docreg.bo.DocRegBo;
import com.aj.evaidya.docreg.dao.DocRegDao;
import com.google.inject.Inject;

public class DocRegBoImpl implements DocRegBo{
	
	private static final Logger logger = Logger.getLogger(DocRegBoImpl.class);
	
	private DocRegDao docRegDao;
	
	public DocRegDao getDocRegDao() {
		return docRegDao;
	}

	@Inject
	public void setDocRegDao(DocRegDao docRegDao) {
		this.docRegDao = docRegDao;
	}

	@Override
	public DocRegResponseBean saveDocDtls(final DocRegRequestBean docRegRequestBean) {
		
		DocRegResponseBean docRegResponseBean = new DocRegResponseBean();
		
		try {
				
 			docRegResponseBean = docRegDao.saveDocDtls(docRegRequestBean);
 			
 		}  catch(Exception e) {
 			
 			logger.error("Error while saving ",e);
 			docRegResponseBean.setStatus("error");
 			docRegResponseBean.setMessage("Not Saved ...");
 			
 		} 
		
		return docRegResponseBean;
			
	}

	@Override
	public Map<String, String> getDocNames() {
		
		Map<String,String> docNameListMap = new LinkedHashMap<String,String>();
		
		try {
				
			docNameListMap = docRegDao.getDocNameDtls();
 			
 		}  catch(Exception e) {
 			
 			logger.error("Error while fetching ",e);
 			
 		} 
		
		return docNameListMap;
	}

	@Override
	public DocRegResponseBean getDocDtls(DocRegRequestBean docRegRequestBean) {
		
		DocRegResponseBean docRegResponseBean = new DocRegResponseBean();
		
		try {
				
 			docRegResponseBean = docRegDao.getDocDtls(docRegRequestBean);
 			
 		}  catch(Exception e) {
 			
 			logger.error("Error while fetching doc dtls ",e);
 			
 		} 
		
		return docRegResponseBean;
	}

	@Override
	public DocRegResponseBean updateDocDtls( DocRegRequestBean docRegRequestBean) {
		
		DocRegResponseBean docRegResponseBean = new DocRegResponseBean();
		
		try {
				
 			docRegResponseBean = docRegDao.updateDocDtls(docRegRequestBean);
 			
 		}  catch(Exception e) {
 			
 			logger.error("Error while saving ",e);
 			docRegResponseBean.setStatus("error");
 			docRegResponseBean.setMessage("Not Saved ...");
 			
 		} 
		
		return docRegResponseBean;
	}

}
