package com.aj.evaidya.common.bo.impl;

import java.util.Map;

import com.aj.evaidya.common.bo.CommonBo;
import com.aj.evaidya.common.dao.CommonDao;
import com.google.inject.Inject;

public class CommonBoImpl implements CommonBo {
	private CommonDao commonDao;
	
	public CommonDao getCommonDao() {
		return commonDao;
	}

	@Inject
	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public Map<String, String> getStateDropDownList(){
					
		return commonDao.getStateDropDownList(); 
		
	}
}
