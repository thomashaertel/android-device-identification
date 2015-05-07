/*
 * Copyright (c) 2015. Thomas Haertel
 *
 * Licensed under Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.thomashaertel.device.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;

/**
 * To use this class it is necessary to signup to google's backup service
 * see http://developer.android.com/google/backup/signup.html
 */
public class SimpleBackupAgent extends BackupAgentHelper {
    // The name of the SharedPreferences file
    public static final String PREFS = "user_preferences";

    // A key to uniquely identify the set of backup data
    public static final String PREFS_BACKUP_KEY = "user_prefs";

    // Allocate a helper and add it to the backup agent
    @Override
    public void onCreate() {
        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, PREFS);
        addHelper(PREFS_BACKUP_KEY, helper);
    }
}