package it.cnr.ilc.ga.action.management.model;


import java.io.Serializable;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

public class GuestPreferences implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1595403478969745500L;
	private static Logger logger = Logger.getLogger("GAlogger"); 
	
	private String theme = "aristo"; //default

	public String getTheme() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if(params.containsKey("theme")) {
			theme = params.get("theme");
		}

		logger.fine("themes: " + theme);
		
		
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}
}

