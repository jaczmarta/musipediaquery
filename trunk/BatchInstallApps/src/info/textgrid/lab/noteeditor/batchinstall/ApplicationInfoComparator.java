package info.textgrid.lab.noteeditor.batchinstall;

import java.util.Comparator;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Compares two AppInfos for sortability by their packagename alphabet position
 * @author julian
 *
 */
public class ApplicationInfoComparator implements Comparator<ApplicationInfo> {
	
	private PackageManager pm;

	public ApplicationInfoComparator(PackageManager pm) {
		this.pm = pm;
	}

	@Override
	public int compare(ApplicationInfo appInfo1, ApplicationInfo appInfo2) {
		String appInfo1Label = (String) pm.getApplicationLabel(appInfo1);
		String appInfo2Label = (String) pm.getApplicationLabel(appInfo2);
		   if (appInfo1Label == null && appInfo2Label == null) {
			      return 0;
			    }
			    if (appInfo1Label == null) {
			      return 1;
			    }
			    if (appInfo2Label == null) {
			      return -1;
			    }
			    return (appInfo1Label).compareTo(appInfo2Label);
			  }
}
