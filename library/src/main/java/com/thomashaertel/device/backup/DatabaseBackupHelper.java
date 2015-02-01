package com.thomashaertel.device.backup;

import android.app.backup.BackupDataInputStream;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.ParcelFileDescriptor;

import com.thomashaertel.device.backup.internal.BackupUtil;
import com.thomashaertel.device.contentprovider.BackupableContentProvider;

import java.util.List;

public class DatabaseBackupHelper extends FileBackupHelper {

    public DatabaseBackupHelper(Context ctx, String database) {
        super(ctx, ctx.getDatabasePath(database).getAbsolutePath());
    }

    public DatabaseBackupHelper(Context ctx, SQLiteDatabase database) {
        super(ctx, database.getPath());
    }

    public DatabaseBackupHelper(Context ctx, SQLiteOpenHelper helper) {
        this(ctx, helper.getDatabaseName());
    }


}