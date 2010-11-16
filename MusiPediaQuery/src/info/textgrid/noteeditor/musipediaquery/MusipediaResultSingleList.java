package info.textgrid.noteeditor.musipediaquery;

import info.textgrid.noteeditor.musipediaquery.ConstantsCollection.SoapErrorCode;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MusipediaResultSingleList extends Activity {
	
	private static String INVALID_USER = "invalid username or password";

	private static String hitComposers[] = new String[ConstantsCollection.RESULT_COUNT];

	private static String hitTitles[] = new String[ConstantsCollection.RESULT_COUNT];

	private static String hitURLs[] = new String[ConstantsCollection.RESULT_COUNT];

	private static String hitIDNumbers[] = new String[ConstantsCollection.RESULT_COUNT];

	private static String hitDistances[] = new String[ConstantsCollection.RESULT_COUNT];

	private static String hitProperties[][] = new String[ConstantsCollection.RESULT_COUNT][11];

	private static String contextMenuEntries[] = { "Open Details in Browser",
			"Show Sheetmusic in Browser" };

	protected List<String> resultArrayList = new ArrayList<String>();

	protected ArrayList<String> queryResultList;

	/** Called when the activity is first created. */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean userInvalid = false;
		Bundle b = getIntent().getExtras();
		Object bundleTransferString = b.get("query");
		if (bundleTransferString instanceof String) {
			setTitle("Query Results for: " + (String) bundleTransferString);
		}
		Object bundleTransferList = b.get("queryResultList");
		if (bundleTransferList instanceof ArrayList) {
			queryResultList = (ArrayList<String>) bundleTransferList;
			if (ConstantsCollection.lastError == SoapErrorCode.INVALID_AUTH) {
				queryResultList.clear();
				resultArrayList.add(INVALID_USER);
				userInvalid = true;
			}
		}
		for (int i = 0; i < ConstantsCollection.RESULT_COUNT
				&& i < queryResultList.size() && !userInvalid; i++) {
			String musipediaEntry = queryResultList.get(i);
			// unescape HTML symbols as &auml; because of semicolon disturbing
			// split algorithm
			musipediaEntry = StringUtils.unescapeHTML(musipediaEntry, 0);
			hitProperties[i] = musipediaEntry.split(";");
			hitURLs[i] = hitProperties[i][0].substring(4).trim();
			hitIDNumbers[i] = hitProperties[i][1].substring(4).trim();
			hitDistances[i] = hitProperties[i][2].substring(10).trim();
			hitComposers[i] = hitProperties[i][3].substring(10).trim();
			hitTitles[i] = hitProperties[i][4].substring(7).trim();
			resultArrayList.add((i + 1) + ". " + hitComposers[i] + "-"
					+ hitTitles[i]);
		}
		setContentView(R.layout.resultlist);
		final ListView list = (ListView) findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.resultlist_entry, resultArrayList);
		list.setAdapter(adapter);
		//Single click on a list entry is handled here via generic AlertDialog
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						MusipediaResultSingleList.this);
				adb.setTitle("Properties of Entry " + hitIDNumbers[position]);
				adb.setMessage("Composer=" + hitComposers[position] + "\n\n"
						+ "Title=" + hitTitles[position] + "\n\n" + "Distance="
						+ hitDistances[position]);
				adb.setPositiveButton("Ok", null);
				adb.setIcon(R.drawable.dialog48);
				adb.show();
			}
		});
		registerForContextMenu(list);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.list) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle("Operations for Entry " + hitIDNumbers[info.position]);
			for (int i = 0; i < contextMenuEntries.length; i++) {
				menu.add(Menu.NONE, i, i, contextMenuEntries[i]);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		// non-empty default, but certainly never called
		String linkedSiteUrlString = "http://www.musipedia.org/";
		switch (menuItemIndex) {
		case 0:
			// file details
			linkedSiteUrlString = hitURLs[info.position];
			break;

		case 1:
			// sheet music: width is 400px
			linkedSiteUrlString = "http://www.musipedia.org/thumbnail/"
					+ hitIDNumbers[info.position] + "-400.png";
			break;
		}
		// String menuItemName = contextMenuEntries[menuItemIndex];
		// String infoSiteLink = hitURLs[info.position];

		// TextView text = (TextView)findViewById(R.id.footer);
		// text.setText(String.format("Selected %s for item %s", menuItemName,
		// listItemName));
		// text.setText("linking to:" + linkedSiteUrlString);
		launchBrowser(linkedSiteUrlString);
		return true;
	}

	private void launchBrowser(String urlString) {
		// Set browser launch mode
		Intent intent = new Intent();
		intent.setClassName("com.android.browser",
				"com.android.browser.BrowserActivity");
		// to launch home page, make sure that data Uri is null.
		Uri data = Uri.parse(urlString);
		intent.setData(data);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// start browser activity
		startActivity(intent);
		// a small delay, let the browser start, before processing the next
		// command.
		// this is good for scenarios where a related DISPLAY TEXT command is
		// followed immediately.
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ConstantsCollection.EDIT_ID:
			startActivity(new Intent(this, MusiPediaPreferences.class));
			return (true);

		case ConstantsCollection.INFO_ID:
			try {
				AlertDialogBuilder.createAboutDialog(this).show();
			} catch (NameNotFoundException e) {
				Log.v("AlertDialog", e.toString());
			}
			return (true);
		}

		return (super.onOptionsItemSelected(item));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, ConstantsCollection.EDIT_ID, Menu.NONE,
				"Preferences").setIcon(R.drawable.preferences48)
				.setAlphabeticShortcut('e');
		menu.add(Menu.NONE, ConstantsCollection.INFO_ID, Menu.NONE, "Info")
				.setIcon(R.drawable.dialog48).setAlphabeticShortcut('c');
		return (super.onCreateOptionsMenu(menu));
	}

}
