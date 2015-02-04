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