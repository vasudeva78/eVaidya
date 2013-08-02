package com.aj.evaidya.common.bo.impl;

import java.util.Map;

import org.apache.log4j.Logger;

import com.aj.evaidya.common.dao.impl.CommonDaoImpl;

public class CommonBoImpl {
	
	private CommonDaoImpl commonDaoImpl;
	private static final Logger logger = Logger.getLogger(CommonBoImpl.class);
	
	public CommonBoImpl(CommonDaoImpl commonDaoImpl) {
		this.commonDaoImpl = commonDaoImpl;
	}

	public Map<String, String> getStateDropDownList(final String connUrl , final String uName ,final String pwd ){
		logger.debug( "inside getStateDropDownList" );
						
		return commonDaoImpl.getStateDropDownList( connUrl , uName , pwd ); 
		
	}
}
