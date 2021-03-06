package com.nyt.mpt.util.enums;

import java.util.HashMap;
import java.util.Map;

public enum LineItemViewableCriteriaEnum {

	NONVIEWABLE{
		@Override
		public String getDisplayName() {
			return "Not Viewable";
		};
		@Override
		public int getViewableValue() {
			return 0;
		}
	},VEIWABLE{
		@Override
		public String getDisplayName() {
			return "Viewable Billing";
		};
		@Override
		public int getViewableValue() {
			return 1;
		}
	},
	ORDERLEVELVEIWABLE{
		@Override
		public String getDisplayName() {
			return "Viewable Threshold";
		};
		@Override
		public int getViewableValue() {
			return 2;
		}
	};
	
	public abstract String getDisplayName();
	public abstract int getViewableValue();
	
	public static Map<Integer, String> getAllValuesMap(){
		Map<Integer, String> valuesMap = new HashMap<Integer, String>();
		for (LineItemViewableCriteriaEnum viewableCriteria : LineItemViewableCriteriaEnum.values()) {
			valuesMap.put(viewableCriteria.getViewableValue(), viewableCriteria.getDisplayName());
		}
		return valuesMap;
	}
	
	public static LineItemViewableCriteriaEnum getDisplayNameByValue(int code){
		for (LineItemViewableCriteriaEnum viewableCriteria : LineItemViewableCriteriaEnum.values()) {
			if (viewableCriteria.getViewableValue() == code){
				return viewableCriteria;
			}
		}
		throw new IllegalArgumentException("No lineItem viewable enum found for given Status code: " + code);
	}
	
}
