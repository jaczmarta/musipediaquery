package info.textgrid.noteeditor.musipediaquery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class MusiPediaPreferences extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);

	}
	
	@Override
	public void onPause() {
		super.onPause();
		readPersistentState(this);
		
	}

	protected static void readPersistentState(Context context) {
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(context);
		//TODO: convert booleans for categories into capital letters for query - preferably at soap query runtime
		ConstantsCollection.CAT_CLAS = new Boolean(prefs.getBoolean("checkboxClassic", true));
		ConstantsCollection.CAT_POP = new Boolean(prefs.getBoolean("checkboxPopular", true));
		ConstantsCollection.CAT_FOLK = new Boolean(prefs.getBoolean("checkboxFolkSongs", true));
		ConstantsCollection.CAT_HNC = new Boolean(prefs.getBoolean("checkboxHymnsCarols", true));
		ConstantsCollection.CAT_NANT = new Boolean(prefs.getBoolean("checkboxAnthems", true));
		
		ConstantsCollection.LOGIN = prefs.getString("usernamePrefString", "deathfireburn");
		ConstantsCollection.PASSWORD = prefs.getString("passwordPrefString", "karlstein");
		 //result list size is between 1 and 50 matches
		ConstantsCollection.RESULT_COUNT = Math.max(Math.min(Integer.parseInt(prefs.getString("resultSize", "10")),50),1);
		
		Log.v("Login", ConstantsCollection.LOGIN, null);
		Log.v("Pass", ConstantsCollection.PASSWORD, null);
		Log.v("Count", ConstantsCollection.RESULT_COUNT+"", null);
	}

}
