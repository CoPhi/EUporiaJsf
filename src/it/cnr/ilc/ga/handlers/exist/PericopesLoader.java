package it.cnr.ilc.ga.handlers.exist;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import it.cnr.ilc.ga.model.analysis.Analysis;
import it.cnr.ilc.ga.model.analysis.Form;
import it.cnr.ilc.ga.model.analysis.Lemma;
import it.cnr.ilc.ga.model.analysis.LinguisticUnit;
import it.cnr.ilc.ga.model.analysis.PartOfSpeech;
import it.cnr.ilc.ga.model.analysis.Root;
import it.cnr.ilc.ga.model.comment.PericopeComments;
import it.cnr.ilc.ga.model.indexsearch.PericopeSet;
import it.cnr.ilc.ga.model.pericope.Pericope;
import it.cnr.ilc.ga.model.pericope.Text;
import it.cnr.ilc.ga.model.pericope.Text.LangType;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.*;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import java.util.logging.Logger;

public class PericopesLoader {

	private static Logger logger = Logger.getLogger("GAlogger"); 

	final int ID=0;
	final int PERICOPE_AR=1; 
	final int PERICOPE_GR=2; 
	final int ID_AR=3;
	final int ID_GR=4;
	final int INFO_AR=5; 
	final int INFO_GR=6; 
	final int NOTA=7;
	final int ANALYSIS_AR=8;
	final int ANALYSIS_GR=9;

	protected final String driver = "org.exist.xmldb.DatabaseImpl";
	private Collection root = null;
	//	private PericopeSet pericopeSet;

	List<Element> arabicWords = null;
	List<Element> greekWords = null;

	public boolean connect() {

		boolean ret = false;
		try {
			logger.info("connect to XML DB");

			Class<?> c = Class.forName(driver);
			Database db = (Database)c.newInstance();
			DatabaseManager.registerDatabase(db);
			root = DatabaseManager.getCollection("");
			if (null != root) {
				ret = true;
				logger.info("Connection to DB done");
			} else {
				logger.severe("Connection to DB failed");
				throw new XMLDBException();
			}


		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			logger.severe("Error connecting DB " + ex.getMessage());
		} catch (InstantiationException ex) {
			ex.printStackTrace();
			logger.severe("Error connecting DB " + ex.getMessage());
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
			logger.severe("Error connecting DB " + ex.getMessage());
		} catch (XMLDBException ex) {
			ex.printStackTrace();
			logger.severe("Error connecting DB " + ex.getMessage());
		}
		return ret;
	}

	public PericopesLoader() {
		logger.info("in pericope Loader");
	}

	private Analysis createAnalysis (List<Element> words, String lang) {

		Analysis analysis = new Analysis();

		for(Element w : words){
			LinguisticUnit lu = new LinguisticUnit();
			Form luForm = new Form();
			Lemma luLemma = new Lemma();
			PartOfSpeech luPOS = new PartOfSpeech();

			if ("ar".equals(lang)) {
				Root arluRoot = new Root();
				String root = w.getAttributeValue("root");
				String voc = w.getAttributeValue("voc");
				String br = w.getAttributeValue("BR");
				lu.setLanguage(Text.LangType.ARABIC);
				luForm.setExtendedValue(voc);
				root = root.replaceAll("#", "");
				arluRoot.setValue(root);
				lu.setRoot(arluRoot);

			} else { //greek
				String bibref = w.getAttributeValue("bibref");
				String ucform = w.getAttributeValue("ucform");
				luForm.setExtendedValue(ucform);
				lu.setLanguage(Text.LangType.GREEK);
			}

			String prog = w.getAttributeValue("prog");
			String id = w.getAttributeValue("id");
			String start = w.getAttributeValue("start");
			String end = w.getAttributeValue("end");
			String form = w.getAttributeValue("form");
			String lemma = w.getAttributeValue("lemma");
			String pos = w.getAttributeValue("pos");

			lu.setId(id);
			lu.setPositionStart(Integer.parseInt(start, 10));
			lu.setPositionEnd(Integer.parseInt(end, 10));

			luForm.setValue(form);
			lu.setForm(luForm);

			luLemma.setValue(lemma);
			lu.setLemma(luLemma);

			luPOS.setValue(pos);
			lu.setPos(luPOS);

			analysis.addLinguisticUnit(lu);
		}

		return analysis;


	}



	private Pericope loadPericope (String resource) throws XMLDBException {

		logger.fine("loading pericope " + resource);
		XMLResource xr = (XMLResource)root.getResource(resource);
		logger.fine("resource loaded");
		SAXHandler saxhandler = new SAXHandler();
		xr.getContentAsSAX(saxhandler);
		logger.fine("Created SAX parser");
		Document document=saxhandler.getDocument();
		Element addNode=document.getRootElement();
		Element docNode=addNode.getChild("doc");
		logger.fine("Created DOC node");

		List<Element> fields=docNode.getChildren();

		String pericopeIdStr = fields.get(ID).getTextNormalize();
		if ( -1 == pericopeIdStr.indexOf('.')) {
			pericopeIdStr =  pericopeIdStr + ".0";
		}
		double pericopeId = Double.parseDouble(pericopeIdStr);
		double arabicTextId = Double.parseDouble(fields.get(ID_AR).getTextNormalize());
		double greekTextId = Double.parseDouble(fields.get(ID_GR).getTextNormalize());
		String arabicTextPericope = fields.get(PERICOPE_AR).getTextNormalize();
		String greekTextPericope = fields.get(PERICOPE_GR).getTextNormalize();
		String arabicTextInfo = fields.get(INFO_AR).getTextNormalize();
		String greekTextInfo = fields.get(INFO_GR).getTextNormalize();
		String pericopeNota = fields.get(NOTA).getTextNormalize();
		//System.err.println("dopo 3");
		logger.fine("Loaded some from pericope");

		try {
			arabicWords = fields.get(ANALYSIS_AR).getChildren();
		} catch(Exception ex) {
			arabicWords=new ArrayList<Element>();
			logger.fine("No analysis for arabic pericope");
		}

		try {
			greekWords = fields.get(ANALYSIS_GR).getChildren();
		} catch(Exception ex) {
			greekWords=new ArrayList<Element>();
			logger.fine("No analysis for greek pericope");
		}


		Pericope pericope=new Pericope();
		pericope.setId(pericopeId);
		Text arabicText = new Text();
		Text greekText = new Text();

		Analysis arabicAnalysis = createAnalysis(arabicWords,"ar");
		Analysis greekAnalysis = createAnalysis(greekWords,"gr");


		arabicText.setLangType(LangType.ARABIC);
		arabicText.setContent(arabicTextPericope);
		arabicText.setId(arabicTextId);
		arabicText.setOffset(0);
		arabicText.setReference(arabicTextInfo);

		greekText.setLangType(LangType.GREEK);
		greekText.setContent(greekTextPericope);
		greekText.setId(greekTextId);
		greekText.setOffset(0);
		greekText.setReference(greekTextInfo);

		/*	
	  		Analysis arabicAnalysis = new Analysis();
		Analysis greekAnalysis = new Analysis();
	for(Element w : arabicWords){
			LinguisticUnit arlu = new LinguisticUnit();
			String prog = w.getAttributeValue("prog");
			String id = w.getAttributeValue("id");
			String start = w.getAttributeValue("start");
			String end = w.getAttributeValue("end");
			String form = w.getAttributeValue("form");
			String br = w.getAttributeValue("BR");
			String lemma = w.getAttributeValue("lemma");
			String root = w.getAttributeValue("root");
			String pos = w.getAttributeValue("pos");
			String voc = w.getAttributeValue("voc");

			arlu.setLanguage(Text.LangType.ARABIC);
			arlu.setId(id);
			arlu.setPositionStart(Integer.parseInt(start, 10));
			arlu.setPositionEnd(Integer.parseInt(end, 10));

			Form arluForm = new Form();
			Lemma arluLemma = new Lemma();
			Root arluRoot = new Root();
			PartOfSpeech arluPOS = new PartOfSpeech();

			arluForm.setValue(form);
			arluForm.setExtendedValue(voc);
			arlu.setForm(arluForm);


			arluLemma.setValue(lemma);
			arlu.setLemma(arluLemma);

			arluPOS.setValue(pos);
			arlu.setPos(arluPOS);

			root = root.replaceAll("#", "");
			arluRoot.setValue(root);
			arlu.setRoot(arluRoot);

			arabicAnalysis.addLinguisticUnit(arlu);

		}
		logger.fine("Loaded analysis from arabic pericope");

		for(Element w : greekWords){
			LinguisticUnit grlu = new LinguisticUnit();
			String prog = w.getAttributeValue("prog");
			String id = w.getAttributeValue("id");
			String bibref = w.getAttributeValue("bibref");
			String ucform = w.getAttributeValue("ucform");
			String start = w.getAttributeValue("start");
			String end = w.getAttributeValue("end");
			String form = w.getAttributeValue("form");
			String lemma = w.getAttributeValue("lemma");
			String pos = w.getAttributeValue("pos");

			grlu.setLanguage(Text.LangType.GREEK);
			grlu.setId(id);
			grlu.setPositionStart(Integer.parseInt(start, 10));
			grlu.setPositionEnd(Integer.parseInt(end, 10));

			Form grluForm = new Form();
			Lemma grluLemma = new Lemma();
			PartOfSpeech grluPOS = new PartOfSpeech();

			grluForm.setValue(form);
			grluForm.setExtendedValue(ucform);
			grlu.setForm(grluForm);

			grluLemma.setValue(lemma);
			grlu.setLemma(grluLemma);

			grluPOS.setValue(pos);
			grlu.setPos(grluPOS);

			greekAnalysis.addLinguisticUnit(grlu);

		}*/
		logger.fine("Loaded analysis from greek pericope");

		arabicText.setAnalysis(arabicAnalysis);
		greekText.setAnalysis(greekAnalysis);

		pericope.setArabicText(arabicText);
		pericope.setGreekText(greekText);

		return pericope;
	}


	public boolean  updatePericopeSet (PericopeSet pericopeSet, String resource) {

		boolean ret = false;
		logger.info("reload pericope " + resource);
		
		try {
			Pericope pericope = loadPericope(resource);
			pericopeSet.addPericope(resource, pericope);
			ret = true;
		} catch (XMLDBException e) {
			logger.severe(e.toString());
			ret = false;
		}

		return ret;
		
	}
	
	
	public LinkedHashMap<String, PericopeComments> load(PericopeSet pericopeSet) throws Exception{

		connect();
		//this.pericopeSet = pericopeSet;

		LinkedHashMap<String, PericopeComments> lhm = new LinkedHashMap<String, PericopeComments>(10000);
		int i = 0;
		for(String resource: root.listResources()){
			i++;
			logger.fine("loading pericope, n. " + i + ", its name is " + resource);

			Pericope pericope = loadPericope(resource);
			pericopeSet.addPericope(resource, pericope);

			lhm.put(Double.toString(pericope.getId()), null);//perche' null???

		}

		//TODO DISCONNECT Exist
		return lhm ;

	}

	public static void main(String[] args){
		try{
			PericopesLoader pl=new PericopesLoader();
			//System.err.println(pl.connect());
			PericopeSet pset = new PericopeSet();

			LinkedHashMap<String, PericopeComments> ris=pl.load(pset);
			logger.fine("ris is empty? " + (ris.size() == 0));
			//			for (Pericope p : pset.getPericopeSet()) {
			//				try {
			//					System.out.println(p.getArabicText().getAnalysis().getAnalysis().get(0).getLemma().getValue());
			//				}catch(Exception ex){
			//					//
			//				}
			//			}
		}catch(Exception ex){
			ex.printStackTrace(System.err);
		}
	}
}
