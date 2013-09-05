package com.aj.evaidya.patreg.bo;

import java.util.Map;

import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.beans.PatRegResponseBean;
import com.aj.evaidya.patreg.dao.PatRegDao;

public interface PatRegBo {
	public Map<String, String> getPatNames(PatRegDao patRegDao ,PatRegRequestBean patRegRequestBean);
	public PatRegResponseBean getPatDtls(PatRegDao patRegDao ,PatRegRequestBean patRegRequestBean);
	public PatRegResponseBean savePatDtls(PatRegDao patRegDao ,PatRegRequestBean patRegRequestBean);
	public PatRegResponseBean updatePatDtls(PatRegDao patRegDao ,PatRegRequestBean patRegRequestBean);
	public PatRegResponseBean uploadPatDtls(PatRegDao patRegDao ,PatRegRequestBean patRegRequestBean , int rowNum , int maxRowNum);
	public PatRegResponseBean getExcelRowsOnUpload(PatRegDao patRegDao ,PatRegRequestBean patRegRequestBean);
}
