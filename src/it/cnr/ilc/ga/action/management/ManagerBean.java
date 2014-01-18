/**
 * 
 */
package it.cnr.ilc.ga.action.management;

import it.cnr.ilc.ga.handlers.exist.CommentStorer;
import it.cnr.ilc.ga.handlers.exist.CommentsLoader;
import it.cnr.ilc.ga.handlers.exist.PericopesLoader;
import it.cnr.ilc.ga.model.comment.Comment;
import it.cnr.ilc.ga.model.comment.PericopeComments;
import it.cnr.ilc.ga.model.indexsearch.IndexItemSet;
import it.cnr.ilc.ga.model.indexsearch.PericopeSet;
import it.cnr.ilc.ga.model.pericope.Pericope;
import it.cnr.ilc.ga.utils.CommentSort;
import it.cnr.ilc.ga.utils.PericopeSort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * @author Angelo Del Grosso
 *
 */
@ManagedBean(name="manager", eager=true)
@ApplicationScoped
public class ManagerBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2599593222146552613L;

	private static Logger logger = Logger.getLogger("GAlogger"); 

	private PericopesLoader pl;
	private CommentsLoader cl;
	private PericopeSet pericopeSet;
	private IndexItemSet indexItemSet;
	//	private CommentSet commentSet;
	private LinkedHashMap<String, PericopeComments> hashComments;
	private CommentStorer storer;	
	/**
	 * 
	 */
	
	private static ManagerBean mb;
	

	public ManagerBean() {
		logger.info("manager Application init");
		//System.getProperties().list(System.out);
		pl = new PericopesLoader();
		cl = new CommentsLoader();
		//TODO
		//cl = new CommentLoader();
		pericopeSet  = new PericopeSet();
		indexItemSet = new IndexItemSet(); //CONTROLLARE!!!!!
		storer = new CommentStorer();

		/*String path="log4j.properties";
        PropertyConfigurator.configure(path);
		logger.info("Test Log");*/
		//System.err.println("user dir: " + System.getProperty("user.dir"));
		//commentSet = new CommentSet();

		try {
			loadPericopes();
			if (null == mb) {
				mb = this;
			}
		
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

	}

	/**
	 * Load all pericopes from database
	 * @throws Exception
	 */
	public  void loadPericopes() throws Exception {
		loadPericopes(null);
	}
	
	/**
	 * Reload some pericopes from database
	 * @param how list of name of resource to be reloaded
	 * @throws Exception
	 */
	public void loadPericopes(String[] how) throws Exception {
		
		if (null == how) {
			hashComments = pl.load(pericopeSet);
			cl.load(hashComments);
			indexItemSet.buildIndexes(pericopeSet); //TODO esportare il build in un handler come pl
		} else {
			for (String resource : how) {
				pl.updatePericopeSet(pericopeSet, resource);
			}
		}
	}

	public static ManagerBean getInstance () {
		return mb;
	}
	
	public ArrayList<Pericope> getPericopesOrderedByGreek(){

		/*
		Collections.sort(pericopes,PericopeSort.GREEK_ORDER);*/
		LinkedHashMap<String, Pericope> pericopes = pericopeSet.getPericopeSet();
		ArrayList<Pericope> pericopesList = new ArrayList<Pericope>(pericopes.values());

		Collections.sort(pericopesList,PericopeSort.GREEK_ORDER);
		return pericopesList;
	}

	public ArrayList<Pericope> getPericopesOrderedByArabic(){
		/*
		Collections.sort(pericopes,PericopeSort.ARABIC_ORDER);*/
		LinkedHashMap<String, Pericope> pericopes = pericopeSet.getPericopeSet();
		ArrayList<Pericope> pericopesList = new ArrayList<Pericope>(pericopes.values());

		Collections.sort(pericopesList,PericopeSort.ARABIC_ORDER);

		return pericopesList;
	}

	public void viewAnalysisAr(){
		LinkedHashMap<String, Pericope> pericopes = pericopeSet.getPericopeSet();
		for (Pericope pericope : pericopes.values()) {
			try{
				logger.fine(pericope.getArabicText().getAnalysis().getAnalysis().get(0).getForm().getValue() + " : "+pericope.getArabicText().getContent());
			}catch(Exception ex){
				logger.warning(ex.getMessage());
			}
		}
	}

	public void viewAnalysisGr(){
		LinkedHashMap<String, Pericope> pericopes = pericopeSet.getPericopeSet();
		for (Pericope pericope : pericopes.values()) {
			try{
				int lenght = pericope.getGreekText().getAnalysis().getAnalysis().size();
				logger.fine( "(1) " + pericope.getGreekText().getAnalysis().getAnalysis().get(lenght-1).getForm().getValue() + " ["+lenght+"]: "+ pericope.getGreekText().getContent());
			}catch(Exception ex){
				logger.warning(ex.getMessage());
			}
		}
	}

	/**
	 * @return the pl
	 */
	public PericopesLoader getPl() {
		return pl;
	}


	/**
	 * @param pl the pl to set
	 */
	public void setPl(PericopesLoader pl) {
		this.pl = pl;
	}


	/**
	 * @return the cl
	 */
	public CommentsLoader getCl() {
		return cl;
	}

	/**
	 * @param cl the cl to set
	 */
	public void setCl(CommentsLoader cl) {
		this.cl = cl;
	}

	/**
	 * @return the pericopeSet
	 */
	public PericopeSet getPericopeSet() {
		return pericopeSet;
	}


	/**
	 * @param pericopeSet the pericopeSet to set
	 */
	public void setPericopeSet(PericopeSet pericopeSet) {
		this.pericopeSet = pericopeSet;
	}

	/**
	 * @return the indexItemSet
	 */
	public IndexItemSet getIndexItemSet() {
		return indexItemSet;
	}

	/**
	 * @param indexItemSet the indexItemSet to set
	 */
	public void setIndexItemSet(IndexItemSet indexItemSet) {
		this.indexItemSet = indexItemSet;
	}

	/**
	 * @return the hashComments
	 */
	public LinkedHashMap<String, PericopeComments> getHashComments() {
		return hashComments;
	}

	/**
	 * @param hashComments the hashComments to set
	 */
	public void setHashComments(LinkedHashMap<String, PericopeComments> hashComments) {
		this.hashComments = hashComments;
	}

	public synchronized boolean addComment(double idPericope, Comment comment){
		String idPericopeStr = Double.toString(idPericope);
		boolean ret = true;
		if (null != hashComments) {
			if( hashComments.containsKey(idPericopeStr)) {
				if (null == hashComments.get(idPericopeStr)){
					hashComments.put(idPericopeStr, new PericopeComments(idPericopeStr));
				}
				PericopeComments pc = hashComments.get(idPericopeStr);

				//sia x agguinta che update perche' uso una hashmap
				pc.addComment(comment);

				//Salvataggio del commento nel db eXist
				try {
					storer.store(comment, pc);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.severe(e.getMessage());
				}
			}
		} else {
			ret = false;
		}
		return ret;

	}

	/**
	 * 
	 * @param idPericope
	 * @param comment
	 * @return
	 */
	public synchronized boolean removeComment(double idPericope, Comment comment){
		String idPericopeStr = Double.toString(idPericope);
		boolean ret = true;
		if (null != hashComments) {
			if( hashComments.containsKey(idPericopeStr)) {
				PericopeComments pc = hashComments.get(idPericopeStr);
				pc.removeComment(comment);
				storer.remove(comment);
			}
		} else {
			ret = false;
		}
		return ret;

	}

	public synchronized boolean removeComments(double idPericope){
		String idPericopeStr = Double.toString(idPericope);
		if (null != hashComments) {
			hashComments.put(idPericopeStr,new PericopeComments(idPericopeStr)); //TODO inserire il controlla in add comment sulla lista di pericopeComments

		} else {System.err.println("di lì no!");}

		return true;
	}

	public List<Comment> getPericopeComments(double idPericope){
		String idPericopeStr = Double.toString(idPericope);
		//String idPericopeStr = "429";
		System.err.println(idPericopeStr);
		HashMap<String, Comment> comments = new HashMap<String, Comment>();
		PericopeComments pc ;
		if (null != hashComments) {
			if(null != (pc = hashComments.get(idPericopeStr) )){
				comments = pc.getComments();
			}
		}
		List<Comment> lc = new ArrayList<Comment>(comments.values());
		Collections.sort(lc, CommentSort.ID_ORDER);
		return lc;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ManagerBean mb = new ManagerBean();
		//		ArrayList<Pericope> orderedPericopes = mb.getPericopesOrderedByGreek();
		//		
		//		for (Pericope pericope : orderedPericopes) {
		//			
		//			String greekText = pericope.getGreekText().getContent();
		//			double greekId = pericope.getGreekText().getId();
		//			
		//			System.out.println(greekId + ": " + greekText);
		//			
		//		}
		//		
		//		orderedPericopes = mb.getPericopesOrderedByArabic();
		//		
		//		for (Pericope pericope : orderedPericopes) {
		//			
		//			String arabicText = pericope.getArabicText().getContent();
		//			double arabicId = pericope.getArabicText().getId();
		//			
		//			System.out.println(arabicId + ": " + arabicText);
		//		}

		//mb.viewAnalysisGr();
		//		List<IndexEntry> index = mb.getIndexItemSet().getFormIndexListAr();
		//		
		//		for (IndexEntry indexEntry : index) {
		//			
		//			System.out.println(indexEntry.getValue() +" : "+ indexEntry.getFreq());

		//		}

	}

}
