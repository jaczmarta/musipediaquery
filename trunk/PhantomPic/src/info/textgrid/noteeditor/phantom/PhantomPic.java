package info.textgrid.noteeditor.phantom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

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
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PhantomPic extends Activity implements OnGesturePerformedListener {

	/**
	 * // how many elements are within a category
	 * FIXME: dynamic lists instead of fixed arrays
	 */
	protected static final int ELEMENTS_PER_CATEGORY = 10; 

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

	private String storagePath = Environment.getExternalStorageDirectory()
			.toString();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initValues();
		paintBitmapImage();
		mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!mLibrary.load()) {
			finish();
		}
		GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
		gestures.setGestureColor(0xFF5500EE);
		gestures.addOnGesturePerformedListener(this);

	}

	private void initValues() {
		this.editText = (EditText) findViewById(R.id.EditText01);
		this.editText.setText("Phantom");
//		this.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//		    @Override
//		    public void onFocusChange(View v, boolean hasFocus) {
//		        if (!hasFocus) {
//		        	paintBitmapImage();
//		        }
//		    }
//		});
//		this.editText.addTextChangedListener(new TextWatcher() {			
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//			}	
//			
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//			}	
//			
//			@Override
//			public void afterTextChanged(Editable s) {
//				paintBitmapImage();				
//			}
//		});

		this.categoryArray = getResources().getStringArray(
				R.array.category_array);
		this.elementArray = new int[ELEMENTS_PER_CATEGORY];
		Arrays.fill(this.elementArray, 1);
	}

	private void paintBitmapImage() {
		icon = Bitmap.createBitmap(480, 600, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(icon);
		Paint paintStyle = new Paint();
		paintStyle.setColor(0xBB000000);
		paintStyle.setDither(true);
		paintStyle.setFilterBitmap(true);
		paintStyle.setTextSize(30);
		paintStyle.setTextAlign(Paint.Align.CENTER);

		float startX = -50f;
		float startY = -30f;
		canvas.drawARGB(250, 210, 210, 210); // fill background first
		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.background), startX, startY, paintStyle);
		canvas.drawBitmap(getElementInCategory0(), startX, startY, paintStyle);
		// canvas.drawBitmap(getElementInCategory1(), startX, startY,
		// paintStyle); //TODO
		canvas.drawBitmap(getElementInCategory2(), startX, startY, paintStyle);
		// canvas.drawBitmap(getElementInCategory3(), startX, startY,
		// paintStyle); //TODO
		canvas.drawBitmap(getElementInCategory4(), startX, startY, paintStyle);
		canvas.drawBitmap(getElementInCategory5(), startX, startY, paintStyle);
		canvas.drawBitmap(getElementInCategory6(), startX, startY, paintStyle);
		canvas.drawBitmap(getElementInCategory7(), startX, startY, paintStyle);
//		canvas.drawBitmap(getElementInCategory8(), startX, startY, paintStyle); TODO

		if (this.editText.getText().length() > 0){
			canvas.drawRect(canvas.getWidth() / 3 - 2, (canvas.getHeight() * 9) / 10 - 2, canvas.getWidth() / 3 * 2 + 2, canvas.getHeight() + 2, paintStyle);
			paintStyle.setColor(0xFFFFFFFF);
			canvas.drawRect(canvas.getWidth() / 3 + 2, ((canvas.getHeight() * 9) / 10) + 2, canvas.getWidth() / 3 * 2 - 2, canvas.getHeight() - 2, paintStyle);
			paintStyle.setColor(0xBB333333);
			canvas.drawText(this.editText.getText().toString(),
					canvas.getWidth() / 2, (canvas.getHeight() * 19) / 20,
					paintStyle);
			
			}
		//TODO use the given maximal screenwidth dynamically, height in 3:4
//		DisplayMetrics metrics = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(metrics);
//		float maxRelation = metrics.widthPixels / icon.getWidth();
//		Bitmap scaledIcon = Bitmap.createScaledBitmap(icon,
//				(int) (maxRelation * icon.getWidth()),
//				(int) (maxRelation * icon.getHeight()), true);

		ImageView image = (ImageView) findViewById(R.id.ImageView01);
		image.setImageBitmap(icon);
	}

	/**
	 * <item>Hair</item>0 <item>Eyebrows</item>1 <item>Ears</item>2
	 * <item>Glasses</item>3 <item>Eyes</item>4 <item>Nose</item>5
	 * <item>Moustache</item>6 <item>Mouth</item>7 <item>Chin</item>8 hair
	 * 
	 * @return
	 */
	private Bitmap getElementInCategory0() {
		switch (elementArray[0]) {
		case 1:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.hair01);
		case 2:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.hair02);
		case 3:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.hair03);
		case 4:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.hair04);
		case 5:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.hair05);
		case 6:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.hair06);
		case 7:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.hair07);
		case 8:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.hair08);
		default:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.background);
		}
	}

	// private Bitmap getElementInCategory1() {
	// switch (elementArray[1]) {
	// case 1:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes01);
	// case 2:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes02);
	// case 3:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes03);
	// case 4:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes04);
	// case 5:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes04);
	// case 6:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes06);
	// case 7:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes07);
	// case 8:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes08);
	// case 9:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes09);
	// default:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.background);
	// }
	// }

	private Bitmap getElementInCategory2() {
		switch (elementArray[2]) {
		case 1:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.ears01);
		case 2:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.ears02);
		case 3:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.ears03);
		case 4:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.ears04);
		case 5:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.ears05);
			// case 6:
			// return BitmapFactory.decodeResource(getResources(),
			// R.drawable.hair06);
			// case 7:
			// return BitmapFactory.decodeResource(getResources(),
			// R.drawable.hair07);
			// case 8:
			// return BitmapFactory.decodeResource(getResources(),
			// R.drawable.hair08);
		default:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.background);
		}
	}

	// private Bitmap getElementInCategory3() {
	// switch (elementArray[3]) {
	// case 1:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes01);
	// case 2:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes02);
	// case 3:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes03);
	// case 4:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes04);
	// case 5:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes04);
	// case 6:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes06);
	// case 7:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes07);
	// case 8:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes08);
	// case 9:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.eyes09);
	// default:
	// return BitmapFactory.decodeResource(getResources(),
	// R.drawable.background);
	// }
	// }

	private Bitmap getElementInCategory4() {
		switch (elementArray[4]) {
		case 1:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.eyes01);
		case 2:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.eyes02);
		case 3:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.eyes03);
		case 4:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.eyes04);
		case 5:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.eyes04);
		case 6:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.eyes06);
		case 7:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.eyes07);
		case 8:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.eyes08);
		case 9:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.eyes09);
		default:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.background);
		}
	}

	private Bitmap getElementInCategory5() {
		switch (elementArray[5]) {
		case 1:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.nose01);
		case 2:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.nose02);
		case 3:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.nose03);
			// case 4:
			// return BitmapFactory.decodeResource(getResources(),
			// R.drawable.hair04);
			// case 5:
			// return BitmapFactory.decodeResource(getResources(),
			// R.drawable.hair05);
			// case 6:
			// return BitmapFactory.decodeResource(getResources(),
			// R.drawable.hair06);
			// case 7:
			// return BitmapFactory.decodeResource(getResources(),
			// R.drawable.hair07);
			// case 8:
			// return BitmapFactory.decodeResource(getResources(),
			// R.drawable.hair08);
		default:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.background);
		}
	}

	private Bitmap getElementInCategory6() {
		switch (elementArray[6]) {
		case 1:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.moustache01);
		case 2:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.moustache02);
		case 3:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.moustache03);
		case 4:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.moustache04);
		case 5:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.moustache05);
			// case 6:
			// return BitmapFactory.decodeResource(getResources(),
			// R.drawable.eyes06);
			// case 7:
			// return BitmapFactory.decodeResource(getResources(),
			// R.drawable.eyes07);
			// case 8:
			// return BitmapFactory.decodeResource(getResources(),
			// R.drawable.eyes08);
			// case 9:
			// return BitmapFactory.decodeResource(getResources(),
			// R.drawable.eyes09);
		default:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.background);
		}
	}

	private Bitmap getElementInCategory7() {
		switch (elementArray[7]) {
		case 1:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.mouth01);
		case 2:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.mouth02);
		case 3:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.mouth03);
		case 4:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.mouth04);
		case 5:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.mouth05);
		case 6:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.mouth06);
		case 7:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.mouth07);
			// case 8:
			// return BitmapFactory.decodeResource(getResources(),
			// R.drawable.eyes08);
			// case 9:
			// return BitmapFactory.decodeResource(getResources(),
			// R.drawable.eyes09);
		default:
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.background);
		}
	}

	private void handleGesture(String gestureString) {
		if (gestureString.equals(GESTURE_UP)
				|| gestureString.equals(GESTURE_DOWN)) {
			// switch category
			if (gestureString.equals(GESTURE_DOWN)) {
				this.categoryPointer++;
				if (this.categoryPointer > this.categoryArray.length - 1)
					this.categoryPointer = 0;
			} else {
				this.categoryPointer--;
				if (this.categoryPointer < 0)
					this.categoryPointer = this.categoryArray.length - 1;
			}
		} else if (gestureString.equals(GESTURE_REDOALL) || gestureString.equals(GESTURE_XPOINT)) {
			// reset all
			initValues();
		} else if (gestureString.equals(GESTURE_RIGHT)
				|| gestureString.equals(GESTURE_LEFT)) {
			// increase element in this category
			if (gestureString.equals(GESTURE_RIGHT)) {
				this.elementArray[this.categoryPointer]++;
				if (this.elementArray[this.categoryPointer] > this.elementArray.length)
					this.elementArray[this.categoryPointer] = 0;
			} else {
				this.elementArray[this.categoryPointer]--;
				if (this.elementArray[this.categoryPointer] < 0)
					this.elementArray[this.categoryPointer] = this.elementArray.length;
			}
		} else if (gestureString.equals(GESTURE_BOXALL)) {
			savePhantomPic();			
		}
		paintBitmapImage();
		Toast.makeText(
				this,
				"Gesture: " + gestureString + "\nCategory: "
						+ this.categoryArray[this.categoryPointer]
						+ "\nElement:"
						+ this.elementArray[this.categoryPointer],
				Toast.LENGTH_SHORT).show();
	}

	private void savePhantomPic() {
		String filename = this.editText.getText().toString() + "[at"
				+ SystemClock.currentThreadTimeMillis() + "].png";
		try {
			OutputStream fOut = null;
			File file = new File(storagePath, filename);
			fOut = new FileOutputStream(file);
			this.icon.compress(Bitmap.CompressFormat.PNG, 85, fOut);
			fOut.flush();
			fOut.close();
			MediaStore.Images.Media.insertImage(getContentResolver(), file
					.getAbsolutePath(), file.getName(), file.getName());
		} catch (FileNotFoundException e) {
			Toast.makeText(this,
					"Exception at save: File not found" + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(this,
					"Exception at save: IO stream error" + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
		Toast.makeText(this, "Saved to: " + storagePath + "/" + filename,
				Toast.LENGTH_SHORT).show();
	}

	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
		// We want at least one prediction
		if (predictions.size() > 0) {
			Prediction prediction = predictions.get(0);
			// We want at least some confidence in the result
			if (prediction.score > 1.0) {
				handleGesture(prediction.name);
			} else {
				Toast.makeText(this, "unknown gesture", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menusave:
			savePhantomPic();
			return (true);

		case R.id.menuabout:
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
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
}