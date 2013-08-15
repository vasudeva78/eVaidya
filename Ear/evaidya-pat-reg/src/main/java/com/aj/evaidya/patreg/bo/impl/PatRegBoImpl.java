package com.aj.evaidya.patreg.bo.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.beans.PatRegResponseBean;
import com.aj.evaidya.patreg.bo.PatRegBo;
import com.aj.evaidya.patreg.dao.PatRegDao;

public class PatRegBoImpl implements PatRegBo {

	private static final Logger logger = Logger.getLogger(PatRegBoImpl.class);

	
	@Override
	public PatRegResponseBean savePatDtls(PatRegDao patRegDao, PatRegRequestBean patRegRequestBean) {
		
		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
		
		try {
				
			patRegResponseBean = patRegDao.savePatDtls(patRegRequestBean);
 			
 		}  catch(Exception e) {
 			
 			logger.error("Error while saving ",e);
 			patRegResponseBean.setStatus("error");
 			patRegResponseBean.setMessage("Not Saved ...");
 			
 		} 
		
		return patRegResponseBean;
		
	}


	@Override
	public Map<String, String> getPatNames(PatRegDao patRegDao, PatRegRequestBean patRegRequestBean) {
		
		Map<String,String> patNameListMap = new LinkedHashMap<String,String>();
		
		try {
			
			patNameListMap = patRegDao.getPatNameDtls(patRegRequestBean);
 			
 		}  catch(Exception e) {
 			
 			logger.error("Error while fetching ",e);
 			
 		} 
		
		return patNameListMap;
	}


	@Override
	public PatRegResponseBean getPatDtls(PatRegDao patRegDao,PatRegRequestBean patRegRequestBean) {
		
		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
		
		try {
			
			patRegResponseBean = patRegDao.getPatDtls(patRegRequestBean);
 			
 		}  catch(Exception e) {
 			
 			logger.error("Error while fetching ",e);
 			
 		} 
		
		return patRegResponseBean;
	}


	@Override
	public PatRegResponseBean updatePatDtls(PatRegDao patRegDao, PatRegRequestBean patRegRequestBean) {
		
		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
		
		try {
			
			patRegResponseBean = patRegDao.updatePatDtls(patRegRequestBean);
 			
 		}  catch(Exception e) {
 			
 			logger.error("Error while fetching ",e);
 			
 			patRegResponseBean.setStatus("error");
 			patRegResponseBean.setMessage("Not Saved ...");
 		} 
		
		return patRegResponseBean;
	}

}
