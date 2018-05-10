package fifthgen.com.brotherandroidprinter.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import fifthgen.com.brotherandroidprinter.ui.fragment.AsyncResponse;

public class PrinterHelper implements AsyncResponse {

    private final WeakReference<Activity> activityReference;

    public PrinterHelper(Activity activity) {
        this.activityReference = new WeakReference<>(activity);
    }

    public void print(String[] text) {
        Activity activity = activityReference.get();

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

        String ip = sharedPref.getString(PreferenceKeys.IP, "");
        String mac = sharedPref.getString(PreferenceKeys.MAC, "");
        int labelNameIndex = sharedPref.getInt(PreferenceKeys.LABEL_NAME_INDEX, 0);

        if (ip.isEmpty()) {
            Toast.makeText(activity, "Invalid IP address.", Toast.LENGTH_SHORT).show();
        } else if (labelNameIndex == 0) {
            Toast.makeText(activity, "Invalid label type.", Toast.LENGTH_SHORT).show();
        } else {
            PrinterTask printerTask = new PrinterTask(this, ip, mac, labelNameIndex);
            printerTask.execute(text);
        }
    }

    @Override
    public void onProcessCompleted(String message) {
        Toast.makeText(activityReference.get(), message, Toast.LENGTH_LONG).show();
    }
}
