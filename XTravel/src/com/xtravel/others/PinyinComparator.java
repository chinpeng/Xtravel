package com.xtravel.others;

import java.util.Comparator;

import com.xtravel.entity.LocModel;

public class PinyinComparator implements Comparator<LocModel> {

	public int compare(LocModel aCityModel, LocModel bCityModel) {
		if (bCityModel.sLetter.equals("#")) {  
            return -1;  
        } else if (aCityModel.sLetter.equals("#")) {  
            return 1;  
        } else {  
            return aCityModel.sLetter.compareTo(bCityModel.sLetter);  
        }  
	}
}