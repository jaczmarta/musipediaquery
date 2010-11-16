package info.textgrid.noteeditor.musipediaquery;

import java.util.ArrayList;

import android.view.Menu;

public class ConstantsCollection {

	protected static final String SOAP2_METHOD_NAME = "search";
	protected static final String SOAP2_ACTION = "http://www.musipedia.org/#search";
	protected static final String SOAP2_NAMESPACE = "http://www.musipedia.org/";
	protected static final String SOAP2_URL = "http://www.musipedia.org/soap/index.php";
	
	protected static final int EDIT_ID = Menu.FIRST + 2; //Menu: edit preferences
	protected static final int INFO_ID = Menu.FIRST + 4; //Menu: close program

	protected static int RESULT_COUNT = 10;

	protected static String LOGIN = "deathfireburn";
	protected static String PASSWORD = "karlstein";
	protected static Boolean CAT_CLAS = true;
	protected static Boolean CAT_POP = true;
	protected static Boolean CAT_FOLK = true;
	protected static Boolean CAT_HNC = true;
	protected static Boolean CAT_NANT = true;

	private static ArrayList<String> queryResultList = new ArrayList<String>();

	public static ArrayList<String> getQueryResultList() {
		return queryResultList;
	}
	
	public enum SoapErrorCode {NO_ERROR, NO_MATCH, INVALID_AUTH}
	protected static SoapErrorCode lastError = SoapErrorCode.NO_ERROR;
}
