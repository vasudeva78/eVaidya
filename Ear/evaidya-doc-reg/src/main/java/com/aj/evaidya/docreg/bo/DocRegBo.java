package com.aj.evaidya.docreg.bo;

import java.util.Map;

import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.beans.DocRegResponseBean;
import com.aj.evaidya.docreg.dao.DocRegDao;

public interface DocRegBo {

	public Map<String, String> getDocNames(DocRegDao docRegDao ,DocRegRequestBean docRegRequestBean);
	public DocRegResponseBean getDocDtls(DocRegDao docRegDao ,DocRegRequestBean docRegRequestBean);
	public DocRegResponseBean saveDocDtls(DocRegDao docRegDao ,DocRegRequestBean docRegRequestBean);
	public DocRegResponseBean updateDocDtls(DocRegDao docRegDao ,DocRegRequestBean docRegRequestBean);
		
}
