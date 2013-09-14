package com.aj.evaidya.docreg.bo;

import java.util.Map;

import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.beans.DocRegResponseBean;

public interface DocRegBo {
	public Map<String, String> getDocNames(DocRegRequestBean docRegRequestBean);
	public DocRegResponseBean getDocDtls(DocRegRequestBean docRegRequestBean);
	public DocRegResponseBean saveDocDtls(DocRegRequestBean docRegRequestBean);
	public DocRegResponseBean updateDocDtls(DocRegRequestBean docRegRequestBean);
		
}
