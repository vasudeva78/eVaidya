package com.aj.evaidya.patreg.bo;

import java.util.Map;

import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.beans.PatRegResponseBean;

public interface PatRegBo {
	public Map<String, String> getPatNames(PatRegRequestBean patRegRequestBean);
	public PatRegResponseBean getPatDtls(PatRegRequestBean patRegRequestBean);
	public PatRegResponseBean savePatDtls(PatRegRequestBean patRegRequestBean);
	public PatRegResponseBean updatePatDtls(PatRegRequestBean patRegRequestBean);
	public PatRegResponseBean uploadPatDtlsToDb(PatRegRequestBean patRegRequestBean , boolean isLastRow);
	public PatRegResponseBean getExcelRowsOnUpload(PatRegRequestBean patRegRequestBean);
	public PatRegResponseBean getExcelCellDtls(PatRegRequestBean patRegRequestBean , int rowNum);
}
