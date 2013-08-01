package com.aj.evaidya.docreg.bo;

import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.beans.DocRegResponseBean;
import com.aj.evaidya.docreg.dao.DocRegDao;

public interface DocRegBo {

	public DocRegResponseBean saveDbTask(DocRegDao docRegDao ,DocRegRequestBean docRegRequestBean);
		
}
