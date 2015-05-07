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

import android.app.backup.FileBackupHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseBackupHelper extends FileBackupHelper {

    public DatabaseBackupHelper(Context ctx, String database) {
        super(ctx, ctx.getDatabasePath(database).getAbsolutePath());
    }

    public DatabaseBackupHelper(Context ctx, SQLiteDatabase database) {
        super(ctx, database.getPath());
    }

    public DatabaseBackupHelper(Context ctx, SQLiteOpenHelper helper) {
        this(ctx, helper.getReadableDatabase().getPath());
    }


}