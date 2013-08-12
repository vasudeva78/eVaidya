package com.aj.evaidya.common.bo.impl;

import java.util.Map;

import com.aj.evaidya.common.bo.CommonBo;
import com.aj.evaidya.common.dao.CommonDao;

public class CommonBoImpl implements CommonBo {
	
	public Map<String, String> getStateDropDownList(CommonDao commonDao , final String connUrl , final String uName ,final String pwd ){
					
		return commonDao.getStateDropDownList( connUrl , uName , pwd ); 
		
	}
}
