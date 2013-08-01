package com.aj.evaidya.common.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.Logger;

public class CommonDao {
	
	private static final Logger logger = Logger.getLogger(CommonDao.class);
	
	public Map<String, String> getStateDropDownList(String connUrl , String uName , String pwd) {
		
		logger.debug("inside CommonDao");
		
		Map<String, String> stateListMap = null ;
					
		try ( Connection dbConn = DriverManager.getConnection(connUrl, uName , pwd ) ){

			logger.debug("after getting db Conn => "+dbConn);
			
			QueryRunner qRunner = new QueryRunner();
			
			stateListMap = qRunner.query(dbConn , "select EV_ID , EV_GRP_COD , EV_GRP_VAL from EV_MASTER order by EV_ID" , 
					new ResultSetHandler<Map<String, String>>(){

						public Map<String, String> handle(ResultSet resultSet) throws SQLException {
							
							Map<String, String> stateListMap = new LinkedHashMap<String, String>() ;
							
							stateListMap.put("--","-- Select --");
							
							while( resultSet.next() ){
								stateListMap.put( resultSet.getString("EV_GRP_COD") , resultSet.getString("EV_GRP_VAL") );
							}
							
							return stateListMap;
						}
				});
			
		}  catch(Exception e) {
			
			logger.error("Error fetching Column lists " ,e);
			
			stateListMap = new HashMap<String, String>();
			
		}
		
		logger.debug("stateListMap size => "+stateListMap.size());
		
		return stateListMap;
	}

}
