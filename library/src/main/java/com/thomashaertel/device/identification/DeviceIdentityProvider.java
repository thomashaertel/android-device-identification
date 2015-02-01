package com.thomashaertel.device.identification;

import android.content.Context;
import android.text.TextUtils;

import com.thomashaertel.device.identification.internal.DeviceIdentifier;

/**
 * Created by Haertel on 31.01.2015.
 */
public class DeviceIdentityProvider {

    public static final String DEVICE_ID_KEY = "DEVICE_ID_KEY";

    private static DeviceIdentityProvider instance;
    private Context mContext;
    private KeyValueStore mIdentityStore;

    private DeviceIdentityProvider(Context ctx) {
        this.mContext = ctx;

        init();
    }

    public static synchronized DeviceIdentityProvider getInstance(Context ctx) {
        if (instance == null) {
            instance = new DeviceIdentityProvider(ctx);
        }

        return instance;
    }

    private void init() {
        // initialize identity provider and check if device id already exists
        if (mIdentityStore.contains(DEVICE_ID_KEY)) {
            if (TextUtils.isEmpty(mIdentityStore.get(DEVICE_ID_KEY))) {
                // generate new device id
                mIdentityStore.put(DEVICE_ID_KEY, generateDeviceId());
            }
        }
    }

    public String getDeviceId() {
        return mIdentityStore.get(DEVICE_ID_KEY);
    }

    public boolean validateDeviceId(final String deviceId) {
        String storedDeviceId = getDeviceId();

        return storedDeviceId != null && storedDeviceId.equals(deviceId);
    }

    public boolean isAuthorizedDevice() {
        return validateDeviceId(generateDeviceId());
    }

    public String generateDeviceId() {
        // try to generate a device id, if it fails generate the pseudo one to be fault tolerant
        try {
            return DeviceIdentifier.getDeviceIdentifier(mContext, true);
        } catch (DeviceIdentifier.DeviceIDException e) {
            return DeviceIdentifier.getPseudoDeviceId();
        }
    }

    public KeyValueStore getIdentityStore() {
        return mIdentityStore;
    }

    public void setIdentityStore(KeyValueStore identityStore) {
        this.mIdentityStore = identityStore;
    }
}
