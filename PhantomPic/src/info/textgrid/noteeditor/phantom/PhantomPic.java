package info.textgrid.noteeditor.phantom;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

public class PhantomPic extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Bitmap icon = BitmapFactory.decodeResource(getResources(),
				R.drawable.logo_eule_128x128);
		float maxRelation = metrics.widthPixels / icon.getWidth();
		Bitmap scaledIcon = Bitmap.createScaledBitmap(icon, (int)(maxRelation * icon.getWidth()), (int)(maxRelation * icon.getHeight()), true);
		Canvas canvas = new Canvas(scaledIcon);
		Paint photoPaint = new Paint();
		photoPaint.setColor(0xFFAAAAAA);
		photoPaint.setDither(true);
		photoPaint.setFilterBitmap(true);
		photoPaint.setTextSize(12);

		canvas.drawText("hello", 1f, 2f, photoPaint);
		canvas.drawLine(0f, 0f, scaledIcon.getWidth(), scaledIcon.getHeight(), photoPaint);
		

		ImageView image = (ImageView) findViewById(R.id.test_image);
		image.setImageBitmap(scaledIcon);

		TextView line = (TextView) findViewById(R.id.TextView01);
		line.setText(Integer.toString(icon.getWidth()) + " x "
				+ Integer.toString(icon.getHeight()));
	}
}