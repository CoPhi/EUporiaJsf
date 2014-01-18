package it.cnr.ilc.ga.utils;

import it.cnr.ilc.ga.model.pericope.Pericope;

import java.util.*;
 
public class PericopeSort {

	
	   public static final Comparator<Pericope> 
	    ARABIC_ORDER = 
	    	new Comparator<Pericope>() {
	        	
	    		public int compare(Pericope p1, Pericope p2) {
	        		return 	(p1.getArabicText().getId() < p2.getArabicText().getId() ? -1 :
	        				(p1.getArabicText().getId() == p2.getArabicText().getId() ? 0 : 1));
	           }
	    	};
	    	
	   public static final Comparator<Pericope> 
	 	GREEK_ORDER = 
	 		new Comparator<Pericope>() {
	 	        	
	 	    	public int compare(Pericope p1, Pericope p2) {
	 	        	return 	(p1.getGreekText().getId() < p2.getGreekText().getId() ? -1 :
	 	        				(p1.getGreekText().getId() == p2.getGreekText().getId() ? 0 : 1));
	 	           }
	 	    	};	

}