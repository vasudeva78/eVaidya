package com.aj.evaidya.docreg.bo.impl;

import org.apache.log4j.Logger;

import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.beans.DocRegResponseBean;
import com.aj.evaidya.docreg.bo.DocRegBo;
import com.aj.evaidya.docreg.dao.DocRegDao;

public class DocRegBoImpl implements DocRegBo{
	
	private static final Logger logger = Logger.getLogger(DocRegBoImpl.class);

	@Override
	public DocRegResponseBean saveDbTask(DocRegDao docRegDao , final DocRegRequestBean docRegRequestBean) {
		
		DocRegResponseBean docRegResponseBean = new DocRegResponseBean();
		
		docRegResponseBean.setStatus("success");
		docRegResponseBean.setMessage("Saved ...");
		
		try {
				
 			docRegResponseBean = docRegDao.insertDocRegDtls(docRegRequestBean);
 			
 		}  catch(Exception e) {
 			
 			logger.error("Error while saving ",e);
 			docRegResponseBean.setStatus("error");
 			docRegResponseBean.setMessage("Not Saved ...");
 			
 		} 
		
		return docRegResponseBean;
			
	}

}
