package com.aj.evaidya.patreg.bo.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.beans.PatRegResponseBean;
import com.aj.evaidya.patreg.bo.PatRegBo;
import com.aj.evaidya.patreg.dao.PatRegDao;
import com.google.inject.Inject;

public class PatRegBoImpl implements PatRegBo {
	
	private static final Logger logger = Logger.getLogger(PatRegBoImpl.class);
	private PatRegDao patRegDao;
	
	public PatRegDao getPatRegDao() {
		return patRegDao;
	}

	@Inject
	public void setPatRegDao(PatRegDao patRegDao) {
		this.patRegDao = patRegDao;
	}

	@Override
	public PatRegResponseBean savePatDtls(PatRegRequestBean patRegRequestBean) {
		
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
	public Map<String, String> getPatNames(PatRegRequestBean patRegRequestBean) {
		
		Map<String,String> patNameListMap = new LinkedHashMap<String,String>();
		
		try {
			
			patNameListMap = patRegDao.getPatNameDtls(patRegRequestBean);
 			
 		}  catch(Exception e) {
 			
 			logger.error("Error while fetching ",e);
 			
 		} 
		
		return patNameListMap;
	}


	@Override
	public PatRegResponseBean getPatDtls(PatRegRequestBean patRegRequestBean) {
		
		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
		
		try {
			
			patRegResponseBean = patRegDao.getPatDtls(patRegRequestBean);
 			
 		}  catch(Exception e) {
 			
 			logger.error("Error while fetching ",e);
 			
 		} 
		
		return patRegResponseBean;
	}


	@Override
	public PatRegResponseBean updatePatDtls(PatRegRequestBean patRegRequestBean) {
		
		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
		logger.debug("inside updatePatDtls => "+patRegRequestBean);
		
		try {
			
			patRegResponseBean = patRegDao.updatePatDtls(patRegRequestBean);
 			
 		}  catch(Exception e) {
 			
 			logger.error("Error while fetching ",e);
 			
 			patRegResponseBean.setStatus("error");
 			patRegResponseBean.setMessage("Not Saved ...");
 		} 
		
		return patRegResponseBean;
	}


	@Override
	public PatRegResponseBean uploadPatDtlsToDb(PatRegRequestBean patRegRequestBean,boolean isLastRow) {
		
		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
		
		try {
			
			patRegResponseBean = patRegDao.uploadPatDtlsToDb(patRegRequestBean,isLastRow);
 			
 		}  catch(Exception e) {
 			
 			logger.error("Error while uploading ",e);
 			
 			patRegResponseBean.setStatus("error");

 		} 
		
		return patRegResponseBean;
	}


	@Override
	public PatRegResponseBean getExcelRowsOnUpload(PatRegRequestBean patRegRequestBean) {
		
		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
		
		try {
			
			patRegResponseBean = patRegDao.getExcelRowsOnUpload(patRegRequestBean);
 			
			patRegResponseBean.setStatus("success");
			
 		}  catch(Exception e) {
 			
 			logger.error("Error fetching row num ",e);
 			
 			patRegResponseBean.setStatus("error");
 			patRegResponseBean.setMessage("Error Fetching Row Count ...");
 		} 
		
		return patRegResponseBean;
	}

	@Override
	public PatRegResponseBean getExcelCellDtls(PatRegRequestBean patRegRequestBean, int rowNum) {
		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
		
		try {
			
			patRegResponseBean = patRegDao.getExcelCellDtls(patRegRequestBean,rowNum);
 			
			patRegResponseBean.setStatus("success");
			
 		}  catch(Exception e) {
 			
 			logger.error("Error fetching row num ",e);
 			
 			patRegResponseBean.setStatus("error");
 			patRegResponseBean.setMessage("Error Fetching Row Count ...");
 		} 
		
		return patRegResponseBean;
	}

}
