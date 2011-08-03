package info.textgrid.lab.noteeditor.batchinstall;

import java.util.Comparator;

import android.content.pm.ApplicationInfo;

/**
 * Compares two AppInfos for sortability by their packagename alphabet position
 * @author julian
 *
 */
public class ApplicationInfoComparator implements Comparator<ApplicationInfo> {

	@Override
	public int compare(ApplicationInfo appInfo1, ApplicationInfo appInfo2) {
		   if (appInfo1.packageName == null && appInfo2.packageName == null) {
			      return 0;
			    }
			    if (appInfo1.packageName == null) {
			      return 1;
			    }
			    if (appInfo2.packageName == null) {
			      return -1;
			    }
			    return (appInfo1.packageName).compareTo(appInfo2.packageName);
			  }
}
