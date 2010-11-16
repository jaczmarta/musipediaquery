package info.textgrid.noteeditor.musipediaquery;

import info.textgrid.noteeditor.musipediaquery.ConstantsCollection.SoapErrorCode;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

@SuppressWarnings("deprecation")
public class RythmQueryFrontend extends Activity implements SensorListener {
	
	// For shake motion detection.
	private SensorManager sensorMgr;
	private long lastUpdate = -1;
	private float x, y, z;
	private float last_x, last_y, last_z;
	private static final int SHAKE_THRESHOLD = 800;

	private TextView introTextView;
	
	private EditText queryEditText;

	private Button tapButton, cancelButton, sendButton;

	private ToggleButton startStopToggleButton;

	private long tapTimeStart;

	private int[] tapTimeArray = new int[MAX_TAP_COUNT];

	private int tapTimePointer;

	public static int MAX_TAP_COUNT = 20;

	private boolean currentlyTapping = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_rythm);
		initVisuals();
		// start motion detection
		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
		boolean accelSupported = sensorMgr.registerListener(this,
				SensorManager.SENSOR_ACCELEROMETER,
				SensorManager.SENSOR_DELAY_GAME);

		if (!accelSupported) {
			// on accelerometer on this device
			sensorMgr.unregisterListener(this,
					SensorManager.SENSOR_ACCELEROMETER);
		}
	}

	private void initVisuals() {
		this.introTextView = (TextView) findViewById(R.id.TextView01);
		this.introTextView.setText("Rythm times:");
		this.queryEditText = (EditText) findViewById(R.id.EditText01);
		this.queryEditText.setEnabled(false);
		this.queryEditText.setText("");
		this.tapButton = (Button) findViewById(R.id.Button01);
		this.tapButton.setText("Tap");
		this.tapButton.setEnabled(false);
		this.cancelButton = (Button) findViewById(R.id.Button02);
		this.cancelButton.setText("Clear");
		this.sendButton = (Button) findViewById(R.id.Button03);
		this.sendButton.setText("Send Query");
		this.startStopToggleButton = (ToggleButton) findViewById(R.id.ToggleButton01);
		this.startStopToggleButton.setText("Start Tapping or Shaking");
	}
	
	protected void onPause() {
		if (sensorMgr != null) {
			sensorMgr.unregisterListener(this,
					SensorManager.SENSOR_ACCELEROMETER);
			sensorMgr = null;
		}
		super.onPause();
	}

	public void onAccuracyChanged(int arg0, int arg1) {
		//empty stub
	}

	public void onSensorChanged(int sensor, float[] values) {
		if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
			long curTime = System.currentTimeMillis();
			// only allow one update every 100ms.
			if ((curTime - lastUpdate) > 100) {
				long diffTime = (curTime - lastUpdate);
				lastUpdate = curTime;

				x = values[SensorManager.DATA_X];
				y = values[SensorManager.DATA_Y];
				z = values[SensorManager.DATA_Z];

				float speed = Math.abs(x + y + z - last_x - last_y - last_z)
						/ diffTime * 10000;
				if (speed > SHAKE_THRESHOLD) {
					// yes, this is a shake action! Do something about it!
					enterTapTime();
					lastUpdate += 300;
				}
				last_x = x;
				last_y = y;
				last_z = z;
			}
		}
	}

	// This method is called at button click because we assigned the name to the
	// "On Click property" of the button
	public void myClickHandler(View view) {
		switch (view.getId()) {
		case R.id.Button01:
			enterTapTime();
			break;
		case R.id.Button02:
			this.queryEditText.getText().clear();
			setTapMode(false);
			break;
		case R.id.Button03:
			sendRQ();
			break;
		case R.id.ToggleButton01:
			setTapMode(!currentlyTapping);
		}
	}

	private void sendRQ() {
		final ProgressDialog dialog = ProgressDialog.show(this,
				"Search by Rythm", "Contacting musipedia server...", true);
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				dialog.dismiss();
			}
		};
		final Thread checkUpdate = new Thread() {
			public void run() {
				setTapMode(false);
				String soapQueryResult = SoapAdapter.soapSend(queryEditText
						.getText().toString());
				handleResultList(soapQueryResult);
				handler.sendEmptyMessage(0);
			}
		};
		checkUpdate.start();
	}
	
	private void handleResultList(String queryResult) {
		ConstantsCollection.getQueryResultList().clear();
		if (queryResult
				.startsWith("SearchResStruct{message=No matching items found")) {
			Log.v("ParsonCodeFrontend:handleResultList",
					"No matching items found!", null);
			ConstantsCollection.lastError=SoapErrorCode.NO_MATCH;

		} else {
			if (queryResult
					.contains("invalid username-password combination or invalid hash")) {
				Log.v("ParsonCodeFrontend:handleResultList",
						"invalid username-password combination or invalid hash!", null);
				ConstantsCollection.lastError=SoapErrorCode.INVALID_AUTH;

			} else {
				String[] queryResultArray = queryResult.split("Item");
				String currentEntry;
				String newEntry;
				for (int i = 1; i < queryResultArray.length; i++) {
					newEntry = new String();
					currentEntry = queryResultArray[i];
					newEntry = currentEntry.substring(1,
							currentEntry.lastIndexOf("}") - 1).trim();
					ConstantsCollection.getQueryResultList().add(newEntry);
				}
				ConstantsCollection.lastError=SoapErrorCode.NO_ERROR;
			}
		}
		// open the new intent
		showMusipediaResultList();
	}

	private void showMusipediaResultList() {
		Intent intent = new Intent(RythmQueryFrontend.this,
				MusipediaResultSingleList.class);
		Bundle b = new Bundle();
		b.putString("query", queryEditText.getText().toString());
		b.putSerializable("queryResultList", ConstantsCollection
				.getQueryResultList());
		intent.putExtras(b);
		startActivity(intent);
//		 finish();
	}

	private void startTapTimer() {
		tapTimeStart = (System.currentTimeMillis());
		tapTimePointer = 0;
		this.queryEditText.getText().clear();
	}

	private void setTapMode(boolean newTapMode) {
		if (currentlyTapping != newTapMode) {
			currentlyTapping = newTapMode;
			this.startStopToggleButton.setChecked(currentlyTapping);
			this.tapButton.setEnabled(currentlyTapping);
			if (currentlyTapping) {
				this.startStopToggleButton.setText("Stop Tapping or Shaking");
				startTapTimer();
			} else {
				this.startStopToggleButton.setText("Start Tapping or Shaking");
			}
		}
	}

	private void enterTapTime() {
		if (tapTimePointer < MAX_TAP_COUNT - 1) {
			int number = (int) ((System.currentTimeMillis() - tapTimeStart) / 100);
			int seconds = number / 10;
			int tenthseconds = number % 10;
			tapTimeArray[tapTimePointer] = number;
			if (this.queryEditText.getText().length() > 0) {
				this.queryEditText.getText().append(
						"," + seconds + "." + tenthseconds);
			} else {
				this.queryEditText.getText().append(
						"" + seconds + "." + tenthseconds);
			}
			tapTimePointer++;
		}
	}

}
