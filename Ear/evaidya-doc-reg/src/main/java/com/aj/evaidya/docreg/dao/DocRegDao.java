package com.aj.evaidya.docreg.dao;

import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.beans.DocRegResponseBean;

public interface DocRegDao {

	public DocRegResponseBean insertDocRegDtls(DocRegRequestBean docRegRequestBean) throws Exception;
}
