/**
 * 
 */
package it.cnr.ilc.ga.action.user;

import it.cnr.ilc.ga.action.management.ManagerBean;
import it.cnr.ilc.ga.model.pericope.Pericope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;


/**
 * @author Angelo Del Grosso
 *
 */
@ManagedBean
@SessionScoped
public class ParallelViewBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8761677373617116582L;

	private static Logger logger = Logger.getLogger("GAlogger"); 

	@ManagedProperty(value="#{manager}")
	private ManagerBean managerBean;
	
	/**
	 * 
	 */
	private Pericope selectedPericope;
	private String selectedContent;
	
	public ParallelViewBean() {
		// TODO Auto-generated constructor stub
		logger.fine("parallel");
	}
	
	public ArrayList<Pericope> getPericopesByArabic(){
		
		return managerBean.getPericopesOrderedByArabic();
		
	}
	
	public ArrayList<Pericope> getPericopesByGreek(){
		
		return managerBean.getPericopesOrderedByGreek();
		
	}

/**
 * @return the managerBean
 */
	public ManagerBean getManagerBean() {
		return managerBean;
	}

/**
 * @param managerBean the managerBean to set
 */
	public void setManagerBean(ManagerBean managerBean) {
		this.managerBean = managerBean;
	}

/**
 * @return the selectedPericope
 */
public Pericope getSelectedPericope() {
	return selectedPericope;
}

/**
 * @param selectedPericope the selectedPericope to set
 */
public void setSelectedPericope(Pericope selectedPericope) {
	this.selectedPericope = selectedPericope;
	logger.fine("select pericope in parallel");
}

/**
 * @return the selectedContent
 */
public String getSelectedContent() {
	return selectedContent;
}

/**
 * @param selectedContent the selectedContent to set
 */
public void setSelectedContent(String selectedContent) {
	this.selectedContent = selectedContent;
	logger.fine("selected content da table view");
}

	

}
