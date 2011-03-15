package info.textgrid.noteeditor.phantom;

import java.util.ArrayList;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PhantomPic extends Activity implements OnGesturePerformedListener {

	protected static final int SAVE_ID = Menu.FIRST + 2; // Menu: edit
															// preferences
	protected static final int INFO_ID = Menu.FIRST + 4; // Menu: close program

	protected static final int ELEMENTS_PER_CATEGORY = 10; // how many elements
															// within a category

	private static final String GESTURE_UP = "Up";
	private static final String GESTURE_DOWN = "Down";
	private static final String GESTURE_LEFT = "Left";
	private static final String GESTURE_RIGHT = "Right";
	private static final String GESTURE_REDOALL = "RedoAll";
	private static final String GESTURE_BOXALL = "BoxItAll";
	private static final String GESTURE_XPOINT = "XMarksThePoint";

	private GestureLibrary mLibrary;

	private Bitmap icon;

	private String[] categoryArray;

	private int[] elementArray;

	private int categoryPointer = 0;
	
	private EditText editText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		init();
		paintBitmapImage();

		mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!mLibrary.load()) {
			finish();
		}
		GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
		gestures.setGestureColor(0xFF5500EE);
		gestures.addOnGesturePerformedListener(this);

	}

	private void init() {
		this.editText = (EditText) findViewById(R.id.EditText01);
		this.editText.setText("");
		this.categoryArray = getResources().getStringArray(
				R.array.category_array);
		this.elementArray = new int[ELEMENTS_PER_CATEGORY];
	}

	private void paintBitmapImage() {
		// TODO: use the given maximal screenwidth dynamically, height in 3:4
		icon = Bitmap.createBitmap(480, 640, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(icon);
		Paint paintStyle = new Paint();
		paintStyle.setColor(0xFFEE0022);
		paintStyle.setDither(true);
		paintStyle.setFilterBitmap(true);
		paintStyle.setTextSize(30);
		paintStyle.setTextAlign(Paint.Align.CENTER);
//		Bitmap inlay;
//		if (this.elementArray[this.categoryPointer] > 0) {
//			inlay = BitmapFactory.decodeResource(getResources(),
//					R.drawable.logo_eule_buch);
//		} else {
//			inlay = BitmapFactory.decodeResource(getResources(),
//					R.drawable.logo_eule_wink);
//		}

//		canvas.drawBitmap(inlay, 30f, 40f, paintStyle);
//		canvas = addCategoryInlays(canvas, paintStyle);
		
		canvas.drawARGB(250, 70, 70, 70); //fill background first
		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.background01), 0f, 0f, paintStyle);
//		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.nose01), 0f, 0f, paintStyle);
		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.eyes01), 0f, 0f, paintStyle);
		paintStyle.setColor(0xFFEE0022);
		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.moustache01), 0f, 0f, paintStyle);
		
		if(this.editText.getText().length() > 0)
			canvas.drawText("Name:" + this.editText.getText().toString(), canvas.getWidth()/2, 380f,
				paintStyle);

		ImageView image = (ImageView) findViewById(R.id.test_image);
		image.setImageBitmap(icon);
	}

	private void handleGesture(String gestureString) {
		if (gestureString.equals(GESTURE_UP)
				|| gestureString.equals(GESTURE_DOWN)) {
			// switch category
			if (gestureString.equals(GESTURE_DOWN)) {
				this.categoryPointer++;
				if(this.categoryPointer > this.categoryArray.length - 1)
					this.categoryPointer = 0;
			} else {
				this.categoryPointer--;
				if(this.categoryPointer < 0)
					this.categoryPointer = this.categoryArray.length - 1;
			}
		} else if (gestureString.equals(GESTURE_REDOALL)) {
			// reset category
			this.categoryPointer = 0;
		} else if (gestureString.equals(GESTURE_RIGHT)
				|| gestureString.equals(GESTURE_LEFT)) {
			// increase element in this category
			if (gestureString.equals(GESTURE_RIGHT)) {
				this.elementArray[this.categoryPointer]++;
				if(this.elementArray[this.categoryPointer] > this.elementArray.length)
						this.elementArray[this.categoryPointer] = 0;
			} else {
				this.elementArray[this.categoryPointer]--;
				if(this.elementArray[this.categoryPointer] < 0)
					this.elementArray[this.categoryPointer] = this.elementArray.length;
			}
		} else if (gestureString.equals(GESTURE_XPOINT) || gestureString.equals(GESTURE_BOXALL)) {
			// delete this element, reset to 0;
			this.elementArray[this.categoryPointer] = 0;
		}
		paintBitmapImage();
		Toast.makeText(this, "Gesture: " + gestureString + "\nCategory: " + this.categoryArray[this.categoryPointer] + "\nElement:" + this.elementArray[this.categoryPointer], Toast.LENGTH_SHORT)
		.show();
	}

	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
		// We want at least one prediction
		if (predictions.size() > 0) {
			Prediction prediction = predictions.get(0);
			// We want at least some confidence in the result
			if (prediction.score > 1.0) {
				// Show the spell
//				Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT)
//						.show();
				handleGesture(prediction.name);
			} else {
				Toast.makeText(this, "???", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SAVE_ID:// TODO
			Toast.makeText(this, "TODO: saving to file", Toast.LENGTH_LONG)
					.show();
			return (true);

		case INFO_ID:
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
		menu.add(Menu.NONE, SAVE_ID, Menu.NONE, "Preferences").setIcon(
				R.drawable.preferences48).setAlphabeticShortcut('e');
		menu.add(Menu.NONE, INFO_ID, Menu.NONE, "About").setIcon(
				R.drawable.dialog48).setAlphabeticShortcut('c');
		return (super.onCreateOptionsMenu(menu));
	}
}