package com.thomashaertel.device.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

/**
 * To use this class it is necessary to signup to google's backup service
 * see http://developer.android.com/google/backup/signup.html
 */
public class SimpleBackupAgent extends BackupAgentHelper {
    // The name of the SharedPreferences file
    public static final String PREFS = "user_preferences";

    // A key to uniquely identify the set of backup data
    public static final String PREFS_BACKUP_KEY = "prefs";

    // Allocate a helper and add it to the backup agent
    @Override
    public void onCreate() {
        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, PREFS);
        addHelper(PREFS_BACKUP_KEY, helper);
    }
}