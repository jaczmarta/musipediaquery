package info.textgrid.lab.noteeditor.batchinstall;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This is an adapter for adding icons and Text into a listview
 * @author julian
 *
 */
@SuppressWarnings("unchecked")
public class IconStringArrayAdapter extends ArrayAdapter {
	private ImageView imageView;
	private TextView textView;
	private PackageManager pm;
	private List<ApplicationInfo> objectList = new ArrayList<ApplicationInfo>();

	public IconStringArrayAdapter(Context context, int textViewResourceId,
			List<ApplicationInfo> objects) {
		super(context, textViewResourceId, objects);
		this.pm = context.getPackageManager();
		this.objectList = objects;
	}

	public int getCount() {
		return this.objectList.size();
	}

	public ApplicationInfo getItem(int index) {
		return this.objectList.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			// ROW INFLATION of layout entry
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.resultlist_entry, parent, false);
		}

		// Get item
		ApplicationInfo appInfo = getItem(position);

		// Get reference to TextView - country_name
		textView = (TextView) row.findViewById(R.id.text1);

		// Get reference to ImageView
		imageView = (ImageView) row.findViewById(R.id.icon);

		// Set country name
		textView.setText( pm.getApplicationLabel(appInfo));

		imageView.setImageDrawable(pm.getApplicationIcon(appInfo));
		return row;
	}

}
