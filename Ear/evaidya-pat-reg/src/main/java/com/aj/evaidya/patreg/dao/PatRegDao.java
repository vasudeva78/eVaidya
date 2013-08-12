package com.aj.evaidya.patreg.dao;

import java.util.Map;

import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.beans.PatRegResponseBean;

public interface PatRegDao {
	public Map<String, String> getPatNameDtls(PatRegRequestBean PatRegRequestBean) throws Exception;
	public PatRegResponseBean getPatDtls(PatRegRequestBean patRegRequestBean) throws Exception;
	public PatRegResponseBean savePatDtls(PatRegRequestBean patRegRequestBean) throws Exception;
}
