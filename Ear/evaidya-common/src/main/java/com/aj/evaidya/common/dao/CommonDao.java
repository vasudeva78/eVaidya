package com.aj.evaidya.common.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.Logger;

public class CommonDao {
	
	private static final Logger logger = Logger.getLogger(CommonDao.class);
	
	public Map<String, List<String>> getStateDropDownList() {
		
		logger.debug("inside CommonDao");
		
		Connection dbConn = null;
		Map<String, List<String>> stateListMap = null ;
					
		try {
						
			Class.forName("org.h2.Driver");
			
			dbConn = DriverManager.getConnection(" jdbc:h2:~/test", "SA", "");
			
			logger.debug("after getting db Conn => "+dbConn);
			
			QueryRunner qRunner = new QueryRunner();
			
			stateListMap = qRunner.query(dbConn , "select EV_ID , EV_GRP_COD , EV_GRP_VAL from EVAIDYA.EV_MASTER order by EV_ID" , 
					new ResultSetHandler<Map<String, List<String>>>(){

						public Map<String, List<String>> handle(ResultSet resultSet) throws SQLException {
							
							Map<String, List<String>> stateListMap = new LinkedHashMap<String, List<String>>() ;
							
							while( resultSet.next() ){
								
								String key = resultSet.getString("EV_ID");
								
								List<String> rowAsList = new ArrayList<String>();
								rowAsList.add(resultSet.getString("EV_GRP_COD"));
								rowAsList.add(resultSet.getString("EV_GRP_VAL"));
								
								stateListMap.put(key, rowAsList);
							}
							
							return stateListMap;
						}
				});
			
		}  catch(Exception e) {
			
			logger.error("Error fetching Column lists " ,e);
			stateListMap = new HashMap<String, List<String>>();
			
		} finally {
			DbUtils.closeQuietly(dbConn);
		}
		
		logger.debug("stateListMap size => "+stateListMap.size());
		
		return stateListMap;
	}

}
