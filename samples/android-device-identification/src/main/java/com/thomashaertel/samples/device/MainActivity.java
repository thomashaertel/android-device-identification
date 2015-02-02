package com.thomashaertel.samples.device;

import android.app.backup.BackupManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thomashaertel.device.identification.DeviceIdentityProvider;

import java.text.SimpleDateFormat;

public class MainActivity extends ActionBarActivity {

    private static SimpleDateFormat formatter = new SimpleDateFormat();
    
    private DeviceIdentityProvider identityProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        identityProvider = DeviceIdentityProvider.getInstance(this);

        // force backup for new device immediately
        if (identityProvider.isNewDevice()) {
            BackupManager backupManager = new BackupManager(this);
            backupManager.dataChanged();
        }
        
        initView();
    }

    private void initView() {
        TextView info = (TextView) findViewById(R.id.textDeviceInfo);

        final String newDevice = identityProvider.isNewDevice() ? "Yes" : "No";
        info.append("New Device: " + newDevice + "\n");
        info.append("DeviceId: " + identityProvider.getDeviceId() + "\n");
        info.append("DeviceId created: " + formatter.format(identityProvider.getDeviceIdCreated()) + "\n");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        // allow backup authorized devices only
        if (identityProvider.isAuthorizedDevice()) {
            BackupManager backupManager = new BackupManager(this);
            backupManager.dataChanged();
        }

        super.onStop();
    }
}
