package info.textgrid.noteeditor.musipediaquery;

import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainTab extends TabActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// read preferences on bootup
		MusiPediaPreferences.readPersistentState(this);

		TabHost host = getTabHost();

		TabSpec contourTabSpec = host.newTabSpec("Contour").setIndicator(
				"ParsonsCode").setContent(
				new Intent(this, ParsonCodeFrontend.class));
		host.addTab(contourTabSpec);
		TabSpec rythmTabSpec = host.newTabSpec("Rythm").setIndicator(
				"RythmCode").setContent(
				new Intent(this, RythmQueryFrontend.class));
		host.addTab(rythmTabSpec);
//		TabSpec shakeTabSpec = host.newTabSpec("Shake").setIndicator(
//				"ShakeRythm").setContent(
//				new Intent(this, RythmShakeFrontend.class));
//		host.addTab(shakeTabSpec);
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
		menu.add(Menu.NONE, ConstantsCollection.INFO_ID, Menu.NONE, "About")
				.setIcon(R.drawable.dialog48).setAlphabeticShortcut('c');
		return (super.onCreateOptionsMenu(menu));
	}
}