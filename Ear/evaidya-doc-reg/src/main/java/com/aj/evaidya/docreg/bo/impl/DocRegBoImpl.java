package com.aj.evaidya.docreg.bo.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.beans.DocRegResponseBean;
import com.aj.evaidya.docreg.bo.DocRegBo;
import com.aj.evaidya.docreg.dao.DocRegDao;

public class DocRegBoImpl implements DocRegBo{
	
	private static final Logger logger = Logger.getLogger(DocRegBoImpl.class);

	@Override
	public DocRegResponseBean saveDocDtls(DocRegDao docRegDao , final DocRegRequestBean docRegRequestBean) {
		
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
	public Map<String, String> getDocNames(DocRegDao docRegDao, DocRegRequestBean docRegRequestBean) {
		
		Map<String,String> docNameListMap = new LinkedHashMap<String,String>();
		
		try {
				
			docNameListMap = docRegDao.getDocNameDtls(docRegRequestBean);
 			
 		}  catch(Exception e) {
 			
 			logger.error("Error while fetching ",e);
 			
 		} 
		
		return docNameListMap;
	}

	@Override
	public DocRegResponseBean getDocDtls(DocRegDao docRegDao,DocRegRequestBean docRegRequestBean) {
		
		DocRegResponseBean docRegResponseBean = new DocRegResponseBean();
		
		try {
				
 			docRegResponseBean = docRegDao.getDocDtls(docRegRequestBean);
 			
 		}  catch(Exception e) {
 			
 			logger.error("Error while fetching doc dtls ",e);
 			
 		} 
		
		return docRegResponseBean;
	}

	@Override
	public DocRegResponseBean updateDocDtls(DocRegDao docRegDao, DocRegRequestBean docRegRequestBean) {
		
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
