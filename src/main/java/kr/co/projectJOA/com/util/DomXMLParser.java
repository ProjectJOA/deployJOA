/**
 * 
 */
package kr.co.projectJOA.com.util;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList; 
import org.w3c.dom.Node; 
import org.w3c.dom.Element;

/**
 * @author Administrator
 *
 */
public class DomXMLParser {

	/**
	 * build file parser
	 * @param buildFileNm
	 * @param startTagNm
	 * @return
	 */
	public HashMap<String,String> getBuildProperties(String buildFileRealPath) throws Exception{
		
		HashMap<String,String> resultMap = new HashMap<String,String>();
		File fXmlFile = new File(buildFileRealPath);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); 
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize(); 
 
		NodeList nList = doc.getElementsByTagName("property"); 

		for (int temp = 0; temp < nList.getLength(); temp++) { 
			Node nNode = nList.item(temp); 
			System.out.println("\nCurrent Element :" + nNode.getNodeName()+"/nNode.getNodeType()="+nNode.getNodeType()+"/Node.ELEMENT_NODE="+Node.ELEMENT_NODE); 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
				Element eElement = (Element) nNode; 
				resultMap.put(eElement.getAttribute("name"), eElement.getAttribute("value"));
			} 
		}
		
		return resultMap;
	}
}
