package com.aj.evaidya.config;

import org.h2.jdbcx.JdbcConnectionPool;

import com.aj.evaidya.common.bo.CommonBo;
import com.aj.evaidya.common.bo.impl.CommonBoImpl;
import com.aj.evaidya.common.dao.CommonDao;
import com.aj.evaidya.common.dao.impl.CommonDaoImpl;
import com.aj.evaidya.docreg.bo.DocRegBo;
import com.aj.evaidya.docreg.bo.impl.DocRegBoImpl;
import com.aj.evaidya.docreg.dao.DocRegDao;
import com.aj.evaidya.docreg.dao.impl.DocRegDaoImpl;
import com.aj.evaidya.patreg.bo.PatRegBo;
import com.aj.evaidya.patreg.bo.impl.PatRegBoImpl;
import com.aj.evaidya.patreg.dao.PatRegDao;
import com.aj.evaidya.patreg.dao.impl.PatRegDaoImpl;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class EvaidyaBindings extends AbstractModule{

	@Override
	protected void configure() {
		
		String dbUrl = "jdbc:h2:file:D:/projects/eVaidya/data/evaidya;CACHE_SIZE=1048576";
		String dbUsername = "ajay";
		String dbPwd = "ajaypwd";
		
		bind(JdbcConnectionPool.class).annotatedWith(Names.named("dbConnPool")).toInstance( JdbcConnectionPool.create( dbUrl  , dbUsername , dbPwd ) );
		
		bind(CommonBo.class).to(CommonBoImpl.class);
		bind(CommonDao.class).to(CommonDaoImpl.class);
		
		bind(DocRegBo.class).to(DocRegBoImpl.class);
		bind(DocRegDao.class).to(DocRegDaoImpl.class);
		
		bind(PatRegBo.class).to(PatRegBoImpl.class);
		bind(PatRegDao.class).to(PatRegDaoImpl.class);
		
	}

}
