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
