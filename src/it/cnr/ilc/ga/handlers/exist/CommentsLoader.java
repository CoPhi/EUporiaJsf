/**
 * 
 */
package it.cnr.ilc.ga.handlers.exist;

import it.cnr.ilc.ga.model.comment.Comment;
import it.cnr.ilc.ga.model.comment.PericopeComments;
import it.cnr.ilc.ga.model.indexsearch.PericopeSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXHandler;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

/**
 * @author Angelo Del Grosso
 *
 */
public class CommentsLoader {

	private static Logger logger = Logger.getLogger("GAlogger"); 

	protected final String driver = "org.exist.xmldb.DatabaseImpl";
	private Collection root = null;

	/**
	 * 
	 */
	public CommentsLoader() {
		logger.info("in comments Loader");
	}

	public boolean connect() {
		try {

			Class<?> c = Class.forName(driver);
			Database db = (Database)c.newInstance();
			DatabaseManager.registerDatabase(db);
			root = DatabaseManager.getCollection("");
			return true;

		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (XMLDBException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	//	private Refs[] getRefs(Document document) {
	//		Element commentNode = document.getRootElement();
	//		Element refsNode = commentNode.getChild("refs");
	//		List<Element> refs2selection = refsNode.getChildren();
	//
	//	}
	//	
	//	private void getVers() {
	//		
	//	}
	//	private void getSelections() {
	//		
	//	}
	//	private void getTexts() {
	//		
	//	}
	public boolean load(LinkedHashMap<String, PericopeComments> lhm) throws Exception{

		//collegarsi alla collection dei commenti di exist
		connect();
		
		logger.info("load comments");
		
		for(String resource: root.listResources()){
			//i++;
			XMLResource xr=(XMLResource) root.getResource(resource);
			logger.fine("loading - " + resource);
			SAXHandler saxhandler = new SAXHandler();
			//			//System.err.println("dopo 1");

			//			System.out.println(xr.getContent()); 
			xr.getContentAsSAX(saxhandler);
			Document document=saxhandler.getDocument();

			Element commentNode = document.getRootElement();
			Element refsNode = commentNode.getChild("refs");
			Element versNode = commentNode.getChild("vers");
			Element selsNode = commentNode.getChild("selections");
			Element textsNode = commentNode.getChild("texts");

			logger.fine(commentNode.toString());
			logger.fine(refsNode.toString());
			logger.fine(versNode.toString());
			logger.fine(selsNode.toString());
			logger.fine(textsNode.toString());

			List<Element> refs2selection = refsNode.getChildren();
			List<Element> vers = versNode.getChildren();
			List<Element> sels = selsNode.getChildren();
			List<Element> texts = textsNode.getChildren();

			logger.fine(refs2selection.toString());
			logger.fine(vers.toString());
			logger.fine(selsNode.toString());
			logger.fine(textsNode.toString());

			String idTarget = commentNode.getAttributeValue("id"); 
			idTarget = idTarget.substring(0,idTarget.indexOf('_'));
			/*
			 * TODO OCCHIO all'idTarget dipendente dal fatto che l'id del commento corrisponde alla pericope direttamente!
			 */
			PericopeComments pc = lhm.get(idTarget);
			if(pc==null){

				pc = new PericopeComments(idTarget);
				lhm.put(idTarget, pc);
			}
			logger.fine(idTarget);

			Comment comment = new Comment();

			int currVersion = vers.size();
			Element currentComment = vers.get(currVersion-1);
			logger.fine(currentComment.toString());

			String commentType = currentComment.getChild("comment-type").getAttributeValue("type"); 
			//currentComment.getChildTextNormalize("comment-type");
			logger.fine(commentType);
			comment.setType(commentType);

			String commentText = currentComment.getChild("text-ref").getAttributeValue("text-id");
			//currentComment.getChildTextNormalize("text-ref");
			commentText = texts.get(Integer.parseInt(commentText)).getChildText("rich");
			logger.fine(commentText);
			comment.setCommentText(commentText);

			int refs2selSize = refs2selection.size();
			ArrayList[] bounds = new ArrayList[2];
			for (int i = 0; i < refs2selSize; i++) {
				Element selection1Node = sels.get(Integer.parseInt(refs2selection.get(i).getAttributeValue("select-name") ));
				List<Element> selects = selection1Node.getChildren();
				Iterator<Element> ie = selects.iterator();
				if (ie.hasNext()){
					Element e = ie.next();
					Integer start = new Integer(e.getChild("start").getAttributeValue("x")); 
					Integer end = new Integer(e.getChild("end").getAttributeValue("x"));
					bounds[i] = new ArrayList (2);
					bounds[i].add(start);
					bounds[i].add(end);
					
				}
			}

			comment.setGreekSelectedBound(bounds[0]);
			comment.setArabicSelectedBound(bounds[1]);

			comment.setIdComment(commentNode.getAttributeValue("id"));

			pc.addComment(comment);

		}
		
		logger.info("Comments loaded");

		return true;
	}

	public static void main(String[] args){
		try{
			PericopeSet pericopeSet = new PericopeSet();
			LinkedHashMap<String, PericopeComments> lhm = new LinkedHashMap<String, PericopeComments>(1000);
			PericopesLoader pl = new PericopesLoader();
			pl.load(pericopeSet);
			CommentsLoader cl = new CommentsLoader();
			cl.load(lhm);
		
		}catch (Exception e) {
			e.printStackTrace();
		}

		//		try{
		//		CommentsLoader cl = new CommentsLoader();
		//		System.err.println(cl.connect());
		//		System.out.println(cl.root.getName());
		//		}catch (Exception e) {
		//			// TODO: handle exception
		//		}
	}
}
