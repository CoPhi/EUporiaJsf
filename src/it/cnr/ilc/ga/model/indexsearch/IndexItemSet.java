/**
 * 
 */
package it.cnr.ilc.ga.model.indexsearch;

import it.cnr.ilc.ga.model.analysis.Analysis;
import it.cnr.ilc.ga.model.analysis.Form;
import it.cnr.ilc.ga.model.analysis.Lemma;
import it.cnr.ilc.ga.model.analysis.LinguisticUnit;
import it.cnr.ilc.ga.model.analysis.Root;
import it.cnr.ilc.ga.model.pericope.Pericope;
import it.cnr.ilc.ga.model.pericope.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.ibm.icu.text.Collator;
import com.ibm.icu.util.ULocale;

/**
 * @author Emiliano
 * 
 * This class contains the lists of all linguistic units, forms, lemmas and roots appearing in the pericopeSet of reference
 *
 */
public class IndexItemSet{

	private ArrayList<LinguisticUnit> linguisticUnitSetAr;
	private ArrayList<LinguisticUnit> linguisticUnitSetGr;
	private TreeMap<String,Integer> formSetAr;
	private TreeMap<String,Integer> formSetGr;
	private TreeMap<String,Integer> lemmaSetAr;
	private TreeMap<String,Integer> lemmaSetGr;
	private TreeMap<String,Integer> rootSet;
	
	private ArrayList<IndexEntry> formIndexListAr;
	private ArrayList<IndexEntry> formIndexListGr;
	private ArrayList<IndexEntry> lemmaIndexListAr;
	private ArrayList<IndexEntry> lemmaIndexListGr;
	private ArrayList<IndexEntry> rootIndexList;
	
	
	/**
	 * 
	 */
	public IndexItemSet() {
	
		linguisticUnitSetAr = new ArrayList<LinguisticUnit>(10000); 
		linguisticUnitSetGr = new ArrayList<LinguisticUnit>(10000); 
		formSetAr = new TreeMap<String, Integer>(
			Collator.getInstance(new ULocale("ar"))	
			);
		formSetGr = new TreeMap<String, Integer>();
		lemmaSetAr = new TreeMap<String, Integer>();
		lemmaSetGr = new TreeMap<String, Integer>(
				Collator.getInstance(new ULocale("grc")));
		rootSet = new TreeMap<String, Integer>();
		
		formIndexListAr = new ArrayList<IndexEntry>();
		formIndexListGr = new ArrayList<IndexEntry>();
		lemmaIndexListAr = new ArrayList<IndexEntry>();
		lemmaIndexListGr = new ArrayList<IndexEntry>();
		rootIndexList = new ArrayList<IndexEntry>();
		
		
	}



	public void addLinguisticUnitAr(LinguisticUnit unit) {
		linguisticUnitSetAr.add(unit);
	}
	
	public void addLinguisticUnitGr(LinguisticUnit unit) {
		linguisticUnitSetGr.add(unit);
	}
	
	
	public void buildIndexes(PericopeSet pSet) {
		LinkedHashMap<String, Pericope> pericopeSet = pSet.getPericopeSet();
		Text arabicText;
		Text greekText;
		Form form;
		Lemma lemma;
		Root root;
		String formValue;
		String lemmaValue;
		String rootValue;

		Analysis analysisAr;
		Analysis analysisGr;

		List<LinguisticUnit> luListAr;
		List<LinguisticUnit> luListGr;
		int value = 0;
		Pattern stripPunct = Pattern.compile("[\\[\u0640\u0660\u066c\u066d\u0021\u003A\u060c\u061b\u061f]");
		//for every pericope of the pericopeSet...
		for (Pericope pericope : pericopeSet.values()) {
			arabicText = pericope.getArabicText();
			analysisAr = arabicText.getAnalysis();
			luListAr = analysisAr.getAnalysis();
			
			//for every linguistic unit belonging to the analyzed arabic text of the current pericope...
			
			for (LinguisticUnit lu : luListAr) {
				if (lu != null) {
					addLinguisticUnitAr(lu);
					//insert the form in the formSetAr, if already existing, increment its value (representing the frequency) by 1
					form = lu.getForm(); 
					formValue = form.getValue();
					if (formValue.length() >0) {
						Matcher mat = stripPunct.matcher(formValue);

						formValue = mat.replaceAll("");

						/*int i = formValue.indexOf('،');
					if (i!=-1){
						System.out.println("tolgo la virgola");
						formValue = formValue.substring(0, i);

					}*/
						if (!formSetAr.containsKey(formValue)) {
							formSetAr.put(formValue, new Integer(1));
						}
						else {
							value = (formSetAr.get(formValue)).intValue();
							value++;
							formSetAr.put(formValue, new Integer(value));
						}
					}
					//insert the lemma in the lemmaSetAr, if already existing, increment its value (representing the frequency) by 1 
					lemma = lu.getLemma();
					lemmaValue = lemma.getValue();
					if (!lemmaSetAr.containsKey(lemmaValue)) {
						lemmaSetAr.put(lemmaValue, new Integer(1));
					}
					else {
						value = (lemmaSetAr.get(lemmaValue)).intValue();
						value++;
						lemmaSetAr.put(lemmaValue, new Integer(value));
					}
					//insert the root in the rootSet, if already existing, increment its value (representing the frequency) by 1
					root = lu.getRoot();
					rootValue = root.getValue();
					if (!rootSet.containsKey(rootValue)) {
						rootSet.put(rootValue, new Integer(1));
					}
					else {
						value = (rootSet.get(rootValue)).intValue();
						value++;
						rootSet.put(rootValue, new Integer(value));
					}
				}
			}
			
			greekText = pericope.getGreekText();
			analysisGr = greekText.getAnalysis();
			luListGr = analysisGr.getAnalysis();
			
			//for every linguistic unit belonging to the analyzed greek text of the current pericope... 
			for (LinguisticUnit lu : luListGr) {
				if (lu != null) {
					addLinguisticUnitGr(lu);
					//insert the form in the formSetGr, if already existing, increment its value by one (the frequency)
					form = lu.getForm();
					formValue = form.getValue();

					if (!formSetGr.containsKey(formValue)) {
						formSetGr.put(formValue, new Integer(1));
					}
					else {
						value = (formSetGr.get(formValue)).intValue();
						value++;
						formSetGr.put(formValue, new Integer (value));
					}
					
					//insert the lemma in the lemmaSetGr, if already existing, increment its value by one (the frequency)
					lemma = lu.getLemma();
					lemmaValue = lemma.getValue().split(" ")[0];
					if(lemmaValue.charAt(0)!='*'){
						if (!lemmaSetGr.containsKey(lemmaValue)) {
							lemmaSetGr.put(lemmaValue, new Integer(1));
						}
						else {
							value = (lemmaSetGr.get(lemmaValue)).intValue();
							value++;
							lemmaSetGr.put(lemmaValue, new Integer(value));
						}
					}

				}
			}
		}	
		
		//System.err.println("linguisticUnitSetAr.size() --> "+linguisticUnitSetAr.size());
		//System.err.println("linguisticUnitSetGr.size() --> "+linguisticUnitSetGr.size());
		
		//System.err.println("formSetAr.size() --> "+formSetAr.size());
		//System.err.println("formSetGr.size() --> "+formSetGr.size());
		
		
		//populate the ordered list of forms, lemmas and roots
		//arabic
		formIndexListAr = treeMap2Array (formSetAr, false);
		lemmaIndexListAr = treeMap2Array(lemmaSetAr, true);
		rootIndexList = treeMap2Array(rootSet, true);
//		for (String entry : formSetAr.descendingKeySet()) {
//			Integer avalue = formSetAr.get(entry);
//	        //System.err.println(entry + "\t" + value);
//			formIndexListAr.add(new IndexEntry(entry, avalue.intValue()));
//	    }
//		for (String entry : lemmaSetAr.descendingKeySet()) {
//			Integer avalue = lemmaSetAr.get(entry);
//	        //System.err.println(entry + "\t" + value);
//			lemmaIndexListAr.add(new IndexEntry(entry, avalue.intValue()));
//	    }
//		for (String entry : rootSet.descendingKeySet()) {
//			Integer avalue = rootIndexList.get(entry);
//	        //System.err.println(entry + "\t" + value);
//			rootIndexList.add(new IndexEntry(entry, avalue.intValue()));
//	    }
		
		//greek
		
		formIndexListGr = treeMap2Array(formSetGr, true);
		lemmaIndexListGr = treeMap2Array(lemmaSetGr, false);
//		for (String entry : formSetGr.descendingKeySet()) {
//			Integer avalue = formSetGr.get(entry);
//	        //System.err.println(entry + "\t" + value);
//			formIndexListGr.add(new IndexEntry(entry, avalue.intValue()));
//	    }
//		for (String entry : lemmaSetGr.descendingKeySet()) {
//			Integer avalue = lemmaSetGr.get(entry);
//	        //System.err.println(entry + "\t" + value);
//			lemmaIndexListGr.add(new IndexEntry(entry, avalue.intValue()));
//	    }
		
	}


	public ArrayList<IndexEntry> treeMap2Array(TreeMap<String,Integer> tm, boolean reverse) {
		ArrayList<IndexEntry> al = new ArrayList<IndexEntry>();
		Set<String> ns; 
		if (reverse) {
			ns = tm.descendingKeySet();
		} else{
			ns = tm.keySet();
		}
		for (String entry : ns) {
			Integer avalue = tm.get(entry);
	        //System.err.println(entry + "\t" + value);
			al.add(new IndexEntry(entry, avalue.intValue()));
	    }
		return al;
	}

	public ArrayList<LinguisticUnit> getLinguisticUnitSet() {
		return linguisticUnitSetAr;
	}




	public void setLinguisticUnitSet(ArrayList<LinguisticUnit> linguisticUnitSet) {
		this.linguisticUnitSetAr = linguisticUnitSet;
	}




	public ArrayList<LinguisticUnit> getLinguisticUnitSetAr() {
		return linguisticUnitSetAr;
	}




	public void setLinguisticUnitSetAr(ArrayList<LinguisticUnit> linguisticUnitSetAr) {
		this.linguisticUnitSetAr = linguisticUnitSetAr;
	}




	public ArrayList<LinguisticUnit> getLinguisticUnitSetGr() {
		return linguisticUnitSetGr;
	}




	public void setLinguisticUnitSetGr(ArrayList<LinguisticUnit> linguisticUnitSetGr) {
		this.linguisticUnitSetGr = linguisticUnitSetGr;
	}




	private TreeMap<String, Integer> getFormSetAr() {
		return formSetAr;
	}




	private void setFormSetAr(TreeMap<String, Integer> formSetAr) {
		this.formSetAr = formSetAr;
	}




	private TreeMap<String, Integer> getFormSetGr() {
		return formSetGr;
	}




	private void setFormSetGr(TreeMap<String, Integer> formSetGr) {
		this.formSetGr = formSetGr;
	}




	private TreeMap<String, Integer> getLemmaSetAr() {
		return lemmaSetAr;
	}




	private void setLemmaSetAr(TreeMap<String, Integer> lemmaSetAr) {
		this.lemmaSetAr = lemmaSetAr;
	}




	private TreeMap<String, Integer> getLemmaSetGr() {
		return lemmaSetGr;
	}




	private void setLemmaSetGr(TreeMap<String, Integer> lemmaSetGr) {
		this.lemmaSetGr = lemmaSetGr;
	}




	private TreeMap<String, Integer> getRootSet() {
		return rootSet;
	}




	private void setRootSet(TreeMap<String, Integer> rootSet) {
		this.rootSet = rootSet;
	}




	public ArrayList<IndexEntry> getFormIndexListAr() {
		return formIndexListAr;
	}




	public void setFormIndexListAr(ArrayList<IndexEntry> formIndexListAr) {
		this.formIndexListAr = formIndexListAr;
	}




	public ArrayList<IndexEntry> getFormIndexListGr() {
		return formIndexListGr;
	}




	public void setFormIndexListGr(ArrayList<IndexEntry> formIndexListGr) {
		this.formIndexListGr = formIndexListGr;
	}




	public ArrayList<IndexEntry> getLemmaIndexListAr() {
		return lemmaIndexListAr;
	}




	public void setLemmaIndexListAr(ArrayList<IndexEntry> lemmaIndexListAr) {
		this.lemmaIndexListAr = lemmaIndexListAr;
	}




	public ArrayList<IndexEntry> getLemmaIndexListGr() {
		return lemmaIndexListGr;
	}




	public void setLemmaIndexListGr(ArrayList<IndexEntry> lemmaIndexListGr) {
		this.lemmaIndexListGr = lemmaIndexListGr;
	}




	public ArrayList<IndexEntry> getRootIndexList() {
		return rootIndexList;
	}




	public void setRootIndexList(ArrayList<IndexEntry> rootIndexList) {
		this.rootIndexList = rootIndexList;
	}


	
	
}
