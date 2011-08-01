package info.textgrid.lab.noteeditor.batchinstall;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BatchInstallReader extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final PackageManager pm = getPackageManager();
		final List<ApplicationInfo> appList = pm.getInstalledApplications(0);
		final ArrayList<String> packageStringList = new ArrayList<String>();
		for (int i = 0; i < appList.size(); i++) {
			ApplicationInfo appInfo = appList.get(i);
			String packageName = appInfo.packageName;
			packageStringList.add(packageName);
		}

		final ListView displayList = (ListView) findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.resultlist_entry, packageStringList);
		displayList.setAdapter(adapter);
		displayList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						BatchInstallReader.this);
				adb.setTitle("Entry");
				adb.setMessage("Package Name="
						+ packageStringList.get(position));
				adb.setPositiveButton("Ok", null);
				adb.setIcon(pm.getApplicationIcon(appList.get(position)));
				adb.show();
			}
		});

		Button upButton = (Button) findViewById(R.id.Button01);
		upButton.setText("writeXmlList");
		upButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				writeListToXml(packageStringList);
			}
		});
		
		TextView textView = (TextView) findViewById(R.id.TextView01);
		textView.setText("List of all " +  appList.size() + " installed Apps");

	}

	private void writeListToXml(List<String> list) {
		Date current = new Date();
		File newxmlfile = new File(Environment.getExternalStorageDirectory()
				+ "/installedAppsList.xml" + current.getDay() + ""
				+ current.getMonth() + "" + current.getYear() + ".xml");
		try {
			newxmlfile.createNewFile();
			// we have to bind the new file with a FileOutputStream
			FileOutputStream fileos = new FileOutputStream(newxmlfile);
			// we create a XmlSerializer in order to write xml data
			XmlSerializer serializer = Xml.newSerializer();
			// we set the FileOutputStream as output for the serializer, using
			// UTF-8 encoding
			serializer.setOutput(fileos, "UTF-8");
			// Write <?xml declaration with encoding (if encoding not null) and
			// standalone flag (if standalone not null)
			serializer.startDocument(null, Boolean.valueOf(true));
			// set indentation option
			serializer.setFeature(
					"http://xmlpull.org/v1/doc/features.html#indent-output",
					true);
			// start a tag called "root"
			serializer.startTag(null, "rootOfInstalledApps");
			for (int i = 0; i < list.size(); i++) {
				serializer.startTag(null, "App" + i);
				serializer.attribute(null, "packageName", list.get(i));
				String linkTest = "https://market.android.com/search?q=pname:"
						+ list.get(i);
				serializer.text(linkTest);

				serializer.endTag(null, "App" + i);
			}
			serializer.endTag(null, "rootOfInstalledApps");
			serializer.endDocument();
			// write xml data into the FileOutputStream
			serializer.flush();
			// finally we close the file stream
			fileos.close();
		} catch (FileNotFoundException e) {
			Log.e("FileNotFoundException", "can't create FileOutputStream");
		} catch (IOException e) {
			Log.e("IOException", "exception in createNewFile() method");
		} catch (Exception e) {
			Log.e("Exception", "error occurred while creating xml file");
		}
	}
}