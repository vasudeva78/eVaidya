package com.aj.evaidya.common.dao;

import java.util.Map;

public interface CommonDao {
	public Map<String, String> getStateDropDownList(String connUrl , String uName , String pwd);
}
