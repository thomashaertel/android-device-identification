package com.thomashaertel.device.identification;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.thomashaertel.device.backup.SimpleBackupAgent;

import java.util.UUID;

public class UserIdentifier {

    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private static String uniqueID = null;

    private UserIdentifier() {
    }

    public static String getUserID(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(SimpleBackupAgent.PREFS, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();

                //backup the changes
                BackupManager mBackupManager = new BackupManager(context);
                mBackupManager.dataChanged();
            }
        }

        return uniqueID;
    }
}
