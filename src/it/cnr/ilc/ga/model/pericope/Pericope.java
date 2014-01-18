package it.cnr.ilc.ga.model.pericope;

import it.cnr.ilc.ga.model.comment.PericopeComments;

public class Pericope {
	
	private double id;
	
	private Text greekText;
	private Text arabicText;
	
	private PericopeComments comments;
	
	public Pericope() {
		
		// constructor
		
	}
	
	/**
	 * @return the id
	 */
	public double getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(double id) {
		this.id = id;
	}
	/**
	 * @return the greekText
	 */
	public Text getGreekText() {
		return greekText;
	}
	/**
	 * @param greekText the greekText to set
	 */
	public void setGreekText(Text greekText) {
		this.greekText = greekText;
	}
	/**
	 * @return the arabicText
	 */
	public Text getArabicText() {
		return arabicText;
	}
	/**
	 * @param arabicText the arabicText to set
	 */
	public void setArabicText(Text arabicText) {
		this.arabicText = arabicText;
	}

	/**
	 * @return the comments
	 */
	public PericopeComments getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(PericopeComments comments) {
		this.comments = comments;
	}
	
	

}
