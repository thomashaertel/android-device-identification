package com.thomashaertel.device.backup;

import android.app.backup.BackupDataInputStream;
import android.app.backup.BackupDataOutput;
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

/**
 * Created by Haertel on 01.02.2015.
 */
public class CarefulDatabaseBackupHelper extends DatabaseBackupHelper {

    private Context mContext;

    public CarefulDatabaseBackupHelper(Context ctx, String database) {
        super(ctx, database);

        this.mContext = ctx;
    }

    public CarefulDatabaseBackupHelper(Context ctx, SQLiteDatabase database) {
        super(ctx, database);

        this.mContext = ctx;
    }

    public CarefulDatabaseBackupHelper(Context ctx, SQLiteOpenHelper helper) {
        super(ctx, helper);

        this.mContext = ctx;
    }

    @Override
    public void performBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) {
        stopContentProvider();
        super.performBackup(oldState, data, newState);
    }

    @Override
    public void restoreEntity(BackupDataInputStream data) {
        stopContentProvider();
        super.restoreEntity(data);
    }

    private void stopContentProvider() {
        List<ProviderInfo> providers = BackupUtil.findContentProviders(mContext);

        for (ProviderInfo providerInfo : providers) {
            ContentResolver resolver = mContext.getContentResolver();
            ContentProviderClient client = resolver.acquireContentProviderClient(providerInfo.authority);
            ContentProvider provider = client.getLocalContentProvider();

            if (provider instanceof BackupableContentProvider) {
                ((BackupableContentProvider) provider).closeDatabase();
            }

            client.release();
        }
    }
}
