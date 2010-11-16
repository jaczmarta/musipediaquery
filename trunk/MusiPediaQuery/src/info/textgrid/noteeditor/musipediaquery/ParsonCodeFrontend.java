package info.textgrid.noteeditor.musipediaquery;

import info.textgrid.noteeditor.musipediaquery.ConstantsCollection.SoapErrorCode;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ParsonCodeFrontend extends ExpandableListActivity {

	private Button upButton, repeatButton, downButton, backspaceButton,
			submitButton;

	private TextView introTextView, upTextView, repeatTextView, downTextView;

	private EditText parsonsCodeEditText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initVisuals();
	}

	private void addNote(String nextNote) {
		parsonsCodeEditText.getText().append(nextNote);
		if (backspaceButton.isEnabled() == false) {
			backspaceButton.setEnabled(true);
		}
		submitButton.setTextColor(android.graphics.Color.BLACK);
	}

	private void removeLastNote(boolean longClick) {
		if (parsonsCodeEditText.getText().length() > 1) {
			if (longClick) {
				parsonsCodeEditText.setText("*");
				backspaceButton.cancelLongPress();
				backspaceButton.setEnabled(false);
			} else {
				parsonsCodeEditText.setText(parsonsCodeEditText.getText()
						.subSequence(0,
								parsonsCodeEditText.getText().length() - 1));
			}
			if (parsonsCodeEditText.getText().length() == 1) {
				backspaceButton.setEnabled(false);
			}
		} else {
			backspaceButton.setEnabled(false);
		}
		submitButton.setTextColor(android.graphics.Color.BLACK);
	}

	private void sendRQ() {
		final ProgressDialog dialog = ProgressDialog.show(this,
				"Search by Contour", "Contacting musipedia server...", true);
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
					dialog.dismiss();
			}
		};
		final Thread checkUpdate = new Thread() {
			public void run() {
				String soapQueryResult = SoapAdapter
						.soapSend(parsonsCodeEditText.getText().toString()
								.substring(1));
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
		Intent intent = new Intent(ParsonCodeFrontend.this,
				MusipediaResultSingleList.class);
		Bundle b = new Bundle();
		b.putString("query", parsonsCodeEditText.getText().toString());
		b.putSerializable("queryResultList", ConstantsCollection
				.getQueryResultList());
		intent.putExtras(b);
		startActivity(intent);
		// finish();
	}

	private void initVisuals() {
		parsonsCodeEditText = (EditText) findViewById(R.id.EditText01); // R.id.EditText01
		parsonsCodeEditText.setEnabled(false);
		parsonsCodeEditText.setText("*");
		upButton = (Button) findViewById(R.id.Button01);
		upButton.setText("Up");
		upButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addNote("U");
			}
		});
		repeatButton = (Button) findViewById(R.id.Button02);
		repeatButton.setText("Repeat");
		repeatButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addNote("R");
			}
		});
		downButton = (Button) findViewById(R.id.Button03);
		downButton.setText("Down");
		downButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addNote("D");
			}
		});
		backspaceButton = (Button) findViewById(R.id.Button04);
		backspaceButton.setText("Backspace");
		backspaceButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				removeLastNote(false);
			}
		});
		backspaceButton.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				removeLastNote(true);
				return true;
			}
		});
		submitButton = (Button) findViewById(R.id.Button05);
		submitButton.setText("Submit Query");
		submitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendRQ();
			}
		});

		introTextView = (TextView) findViewById(R.id.TextView01);
		introTextView.setText("Parsons Code:");
		upTextView = (TextView) findViewById(R.id.TextView02);
		upTextView.setText("Next note Up:");
		repeatTextView = (TextView) findViewById(R.id.TextView03);
		repeatTextView.setText("Next note Repeat:");
		downTextView = (TextView) findViewById(R.id.TextView04);
		downTextView.setText("Next note Down:");
	}
}