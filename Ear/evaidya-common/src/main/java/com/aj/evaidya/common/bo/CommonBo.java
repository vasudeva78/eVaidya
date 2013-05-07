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

	public void getStateDropDownList(final String connUrl , final String uName ,final String pwd , final ChoiceBox stateChoiceBox , final List<String> stateList , final List<String> stateIdList){
		logger.debug( "inside getStateDropDownList" );
						
		final Task<Map<String, String>> choiceListTask = new Task<Map<String, String>>() {
	         @Override protected Map<String, String> call() throws Exception {
	        	 
	        	 return commonDao.getStateDropDownList( connUrl , uName , pwd ); 
	        	 
	         }
	         
	     };
	     
	     choiceListTask.stateProperty().addListener(new ChangeListener<State>(){

			@Override
			public void changed(ObservableValue<? extends State> ov, State t, State newState) {
				if (newState == State.SUCCEEDED) {
					
					Map<String, String> stateListMap = choiceListTask.getValue();
					
					stateIdList.addAll( stateListMap.keySet() );
					stateList.addAll( stateListMap.values()  );
					
					stateChoiceBox.getItems().addAll( stateList );
					
					stateChoiceBox.setDisable(false);
					
					stateChoiceBox.setValue("-- Select --");
					
				}
				
			}	 
	     });
	     
	     new Thread(choiceListTask).start();
		
	}
}
