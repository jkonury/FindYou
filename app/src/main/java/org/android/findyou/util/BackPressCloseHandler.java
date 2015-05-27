package org.android.findyou.util;

import android.app.Activity;
import android.widget.Toast;

import org.android.findyou.R;

public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public boolean onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return true;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
            return false;
        }

        return false;
    }

    private void showGuide() {
        toast = Toast.makeText(activity, activity.getResources().getText(R.string.exit_app), Toast.LENGTH_SHORT);
        toast.show();
    }

}