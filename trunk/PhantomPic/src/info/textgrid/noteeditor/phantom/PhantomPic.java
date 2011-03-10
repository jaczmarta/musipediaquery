package info.textgrid.noteeditor.phantom;

import java.util.ArrayList;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class PhantomPic extends Activity implements OnGesturePerformedListener {

	private GestureLibrary mLibrary;

	private Bitmap icon;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		paintBitmapImage("Fire Spell");

		mLibrary = GestureLibraries.fromRawResource(this, R.raw.spells);
		if (!mLibrary.load()) {
			finish();
		}
		GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
		gestures.setGestureColor(0xFF5500EE);
		gestures.addOnGesturePerformedListener(this);
		
		Spinner categorySpinner = (Spinner) findViewById(R.id.Spinner01);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.category_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    categorySpinner.setAdapter(adapter);
	    categorySpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
	}

	private void paintBitmapImage(String type) {
		icon = Bitmap.createBitmap(300, 400, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(icon);
		Paint photoPaint = new Paint();
		photoPaint.setColor(0xFF5500EE);
		photoPaint.setDither(true);
		photoPaint.setFilterBitmap(true);
		photoPaint.setTextSize(24);

		Bitmap inlay;
		if (type.startsWith("Fire")) {
			inlay = BitmapFactory.decodeResource(getResources(),
					R.drawable.logo_eule_128x128);
		} else {
			if (type.startsWith("Ice")) {
				inlay = BitmapFactory.decodeResource(getResources(),
						R.drawable.logo_eule_buch);
			} else {
				inlay = BitmapFactory.decodeResource(getResources(),
						R.drawable.logo_eule_wink);
			}
		}
		
		canvas.drawARGB(250, 120, 120, 120);
		canvas.drawText(type, 12f, 380f, photoPaint);
		canvas.drawBitmap(inlay, 30f, 40f, photoPaint);		

		ImageView image = (ImageView) findViewById(R.id.test_image);
		image.setImageBitmap(icon);
	}

	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
		// We want at least one prediction
		if (predictions.size() > 0) {
			Prediction prediction = predictions.get(0);
			// We want at least some confidence in the result
			if (prediction.score > 1.0) {
				// Show the spell
				Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT)
						.show();
				paintBitmapImage(prediction.name);
			} else {
				Toast.makeText(this, "???", Toast.LENGTH_SHORT)
				.show();
			}
		}
	}
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
	      Toast.makeText(parent.getContext(), "The category is " +
	          parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	      // Do nothing.
	    }
	}
}