package it.cnr.ilc.ga.model.indexsearch;

import it.cnr.ilc.ga.model.pericope.Pericope;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

public class PericopeSet {
	  
	private LinkedHashMap<String, Pericope> pericopeSet;

	/**
	 * @return the pericopeSet
	 */
	private static Logger logger = Logger.getLogger("GAlogger"); 

	
	public PericopeSet() {
		pericopeSet=new LinkedHashMap<String, Pericope>(1024);
		logger.info("sono in pericope set...");
	}

	
	public LinkedHashMap<String, Pericope> getPericopeSet() {
		return pericopeSet;
	}

	/**
	 * @param pericopeSet the pericopeSet to set
	 */
	public void setPericopeSet(LinkedHashMap<String, Pericope> pericopeSet) {
		this.pericopeSet = pericopeSet;
	}
	
	public void addPericope(String name, Pericope pericope){
		pericopeSet.put(name, pericope);
		//System.out.println(pericope.getGreekText().getContent());
	}
	
}
