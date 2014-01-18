package it.cnr.ilc.ga.model.pericope;

import it.cnr.ilc.ga.model.analysis.Analysis;

public class Text {
	
	public static enum LangType {GREEK, ARABIC}
	
	private LangType langType;
	
	private String content;
	
	private double id;
	private int offset;
	
	private String reference;
	private Analysis analysis = null;
	
	public Text() {
		// constructor
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content.replaceAll("¶", "<br />");
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the langType
	 */
	public LangType getLangType() {
		return langType;
	}

	/**
	 * @param langType the langType to set
	 */
	public void setLangType(LangType langType) {
		this.langType = langType;
	}

	public void setAnalysis(Analysis analysis) {
		this.analysis = analysis;
	}

	public Analysis getAnalysis() {
		return analysis;
	}

		
}
