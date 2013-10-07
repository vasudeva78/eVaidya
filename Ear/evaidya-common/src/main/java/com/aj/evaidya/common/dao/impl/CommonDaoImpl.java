package com.aj.evaidya.common.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;

import com.aj.evaidya.common.dao.CommonDao;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class CommonDaoImpl implements CommonDao {
	private Logger logger = Logger.getLogger(CommonDaoImpl.class);
	
	private JdbcConnectionPool dbConnPool;

	@Inject
	public void setDbConnPool(@Named("dbConnPool") JdbcConnectionPool dbConnPool) {
		this.dbConnPool = dbConnPool;
	}
	
	public Map<String, String> getStateDropDownList() {
		
		Map<String, String> stateListMap = null ;
					
		try ( Connection dbConn = dbConnPool.getConnection() ){
			
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
			
			stateListMap = new HashMap<String, String>();
			
			logger.error("Exception fetching State Lists" , e);
			
		}
		
		return stateListMap;
	}

}
