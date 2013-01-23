package com.aj.evaidya.common.bo;

import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.scene.control.ChoiceBox;

import org.apache.log4j.Logger;

import com.aj.evaidya.common.dao.CommonDao;

public class CommonBo {
	
	private CommonDao commonDao;
	private static final Logger logger = Logger.getLogger(CommonBo.class);
	
	public CommonBo(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public void getStateDropDownList(final ChoiceBox stateChoiceBox){
		logger.debug( "inside getStateDropDownList" );
						
		final Task<Map<String, List<String>>> choiceListTask = new Task<Map<String, List<String>>>() {
	         @Override protected Map<String, List<String>> call() throws Exception {
	        	 
	        	 return commonDao.getStateDropDownList(); 
	        	 
	         }
	         
	     };
	     
	     choiceListTask.stateProperty().addListener(new ChangeListener<State>(){

			@Override
			public void changed(ObservableValue<? extends State> ov, State t, State newState) {
				if (newState == State.SUCCEEDED) {
					
					Map<String, List<String>> stateListMap = choiceListTask.getValue();
					
					stateChoiceBox.getItems().addAll( stateListMap.values() );
					
					stateChoiceBox.setDisable(false);
					
				}
				
			}	 
	     });
	     
	     new Thread(choiceListTask).start();
		
	}
}
