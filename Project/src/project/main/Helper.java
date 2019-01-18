package project.main;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Helper {

	public static void load(String httpUrl, String language, String currencyCode) throws MalformedURLException {

		Document doc = null;
		Document doc2 = null;
		int count = 0;
		double[] arr = new double[2];
		int innerIf = 0;
		int innerCounter = 0;

		/**
		 * Simple language settings
		 */
		File file = new File("../resources");
		URL[] urls = { file.toURI().toURL() };

		ClassLoader loader = new URLClassLoader(urls);
		ResourceBundle resources = ResourceBundle.getBundle("Messages", new Locale(language, language), loader);

		try {
			/**
			 * Getting data from url 
			 * and getting data from local xml (getting currency names)
			 */
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new URL(httpUrl).openStream());
			doc2 = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new File("../resources/" + language + ".xml"));

		} catch (SAXException | IOException | ParserConfigurationException e) {
			System.out.println("Something went wrong");
			e.printStackTrace();
		}

		NodeList xmlNodes = doc.getElementsByTagName("FxRate");
		NodeList nodes = doc2.getElementsByTagName("item");

		Map<String, String> zem = new HashMap<>();

		/**
		 * here I put all currency codes in map as a key
		 * and value is currency name
		 */
		
		while (nodes.getLength() > count) {
			Element err2 = (Element) nodes.item(count);
			zem.put(test2(err2, "valiutos_kodas", 0), test2(err2, "pavadinimas", 0));
			count++;
		}

		count = 0;

		/**
		 * this while cycle is for printing information
		 */
		
		while (xmlNodes.getLength() > count) {
			Element ele = (Element) xmlNodes.item(count);
			System.out.println(resources.getString("message.date") + test2(ele, "Dt", 0));
			System.out.println(resources.getString("message.currency") + zem.get(currencyCode.toUpperCase()));
			System.out.println(resources.getString("message.proportion") + test2(ele, "Amt", 1));
			
			/**
			 * this while cycle is for taking 1st day and 2nd day proportions
			 * and calculate currency change
			 */
			while (xmlNodes.getLength() > innerCounter) {
				Element elez = (Element) xmlNodes.item(innerCounter);

				if (innerIf == 1) {
					arr[1] = Double.parseDouble(test2(elez, "Amt", 1));
					System.out.printf("%s %.4f %s", resources.getString("message.change"),
							(arr[0] - arr[1]) / arr[1] * 100, "%\n\n");
					innerIf = 0;
					break;
				} else {
					arr[0] = Double.parseDouble(test2(elez, "Amt", 1));
					innerIf++;
				}
				innerCounter++;
			}
			count++;
			/**
			 * this is where I break cycle earlier
			 * and do not show that extra day
			 */
			if (xmlNodes.getLength() - 2 < count) {
				break;
			}
		}
	}

	private static String test2(Element element, String text, int num) {
		return element.getElementsByTagName(text).item(num).getTextContent();
	}
}
