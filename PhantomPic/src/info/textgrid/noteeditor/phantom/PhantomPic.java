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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PhantomPic extends Activity implements OnGesturePerformedListener {

	private GestureLibrary mLibrary;

	private Bitmap icon;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		paintBitmapImage("Fire");

		mLibrary = GestureLibraries.fromRawResource(this, R.raw.spells);
		if (!mLibrary.load()) {
			finish();
		}

		GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
		gestures.setGestureColor(0xFF5500EE);
		gestures.addOnGesturePerformedListener(this);
	}

	private void paintBitmapImage(String type) {
		// DisplayMetrics metrics = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(metrics);

			icon = Bitmap.createBitmap(300, 400, Bitmap.Config.ARGB_8888);

		// float maxRelation = metrics.widthPixels / icon.getWidth();
		// Bitmap scaledIcon = Bitmap.createScaledBitmap(icon, (int)(maxRelation
		// * icon.getWidth()), (int)(maxRelation * icon.getHeight()), true);
		Canvas canvas = new Canvas(icon);
		Paint photoPaint = new Paint();
		photoPaint.setColor(0xFFAAAAAA);
		photoPaint.setDither(true);
		photoPaint.setFilterBitmap(true);

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
		
		canvas.drawARGB(250, 250, 40, 250);
		canvas.drawBitmap(inlay, 30f, 40f, photoPaint);		
		photoPaint.setTextSize(24);
		canvas.drawText(type, 24f, 48f, photoPaint);
		// canvas.drawLine(0f, 0f, icon.getWidth(), icon.getHeight(),
		// photoPaint);

		ImageView image = (ImageView) findViewById(R.id.test_image);
		image.setImageBitmap(icon);

		TextView line = (TextView) findViewById(R.id.TextView01);
		line.setText(type + ": " +Integer.toString(icon.getWidth()) + " x "
				+ Integer.toString(icon.getHeight()));
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
}