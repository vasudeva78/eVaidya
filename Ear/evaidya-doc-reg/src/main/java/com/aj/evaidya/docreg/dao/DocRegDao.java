package com.aj.evaidya.docreg.dao;

import java.util.Map;

import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.beans.DocRegResponseBean;

public interface DocRegDao {

	public Map<String, String> getDocNameDtls() throws Exception;
	public DocRegResponseBean getDocDtls(DocRegRequestBean docRegRequestBean) throws Exception;
	public DocRegResponseBean saveDocDtls(DocRegRequestBean docRegRequestBean) throws Exception;
	public DocRegResponseBean updateDocDtls(DocRegRequestBean docRegRequestBean) throws Exception;
}

