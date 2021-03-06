package com.aj.evaidya.patreg.dao;

import java.util.Map;

import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.beans.PatRegResponseBean;

public interface PatRegDao {
	public Map<String, String> getPatNameDtls(PatRegRequestBean PatRegRequestBean) throws Exception;
	public PatRegResponseBean getPatDtls(PatRegRequestBean patRegRequestBean) throws Exception;
	public PatRegResponseBean savePatDtls(PatRegRequestBean patRegRequestBean) throws Exception;
	public PatRegResponseBean updatePatDtls(PatRegRequestBean patRegRequestBean) throws Exception;
	public PatRegResponseBean uploadPatDtlsToDb(PatRegRequestBean patRegRequestBean , boolean isLastRow) throws Exception;
	public PatRegResponseBean getExcelRowsOnUpload(PatRegRequestBean patRegRequestBean) throws Exception;
	public PatRegResponseBean getExcelCellDtls(PatRegRequestBean patRegRequestBean , int rowNum) throws Exception;
}
