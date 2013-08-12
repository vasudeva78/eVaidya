package com.aj.evaidya.common.bo;

import java.util.Map;

import com.aj.evaidya.common.dao.CommonDao;

public interface CommonBo {
	public Map<String, String> getStateDropDownList(CommonDao commonDao , final String connUrl , final String uName ,final String pwd );
}
