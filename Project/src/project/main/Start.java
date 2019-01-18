package project.main;

import java.io.Console;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

/**
 * Program which allows you to get
 * currency change during specific period of time
 * @author Edgaras
 *
 */
public class Start {

	public static void main(String[] args) {

		Console console = System.console();
		String language = console.readLine("Enter eu/lt: ");
		String currencyCode = console.readLine("Enter currency code: ");
		String dateFrom = console.readLine("Enter date from: ");
		String dateTo = console.readLine("Enter date to: ");
		
		/**
		 * To get currency change I need 2 days proportions
		 * and to get last day change I take 1 day extra then user asked
		 * and then I can get both days proportions and can calculate currency change
		 * then I break while cycle early to now show that extra day.
		 * You will see it in Helper class 
		 */
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(dateFrom, formatter).minusDays(1);

		String httpUrl = "http://old.lb.lt/webservices/fxrates/FxRates.asmx/getFxRatesForCurrency?tp=" 
		+ language + "&ccy=" + currencyCode + "&dtFrom=" + localDate + "&dtTo=" + dateTo;

		try {
			Helper.load(httpUrl, language, currencyCode);
		} catch (MalformedURLException e) {
			System.out.println("Something went wrong");
			e.printStackTrace();
		}

	}
}
