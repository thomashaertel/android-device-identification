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

import android.content.Context;
import android.text.TextUtils;

import com.thomashaertel.device.identification.internal.DeviceIdentifier;

import java.util.Date;

public class DeviceIdentityProvider {

    public static final String DEVICE_ID_KEY = "deviceId";
    public static final String DEVICE_ID_TIMESTAMP = "deviceIdTimestamp";

    private static DeviceIdentityProvider instance;
    private Context mContext;
    private KeyValueStore mIdentityStore;

    private boolean mNewDevice;

    private DeviceIdentityProvider(Context ctx) {
        this.mContext = ctx;
        this.mIdentityStore = new SharedPreferencesStore(ctx, true);

        init();
    }

    public static synchronized DeviceIdentityProvider getInstance(Context ctx) {
        if (instance == null) {
            instance = new DeviceIdentityProvider(ctx);
        }

        return instance;
    }

    private void init() {
        mNewDevice = false;

        // initialize identity provider and check if device id already exists
        if (!mIdentityStore.contains(DEVICE_ID_KEY) || TextUtils.isEmpty(mIdentityStore.get(DEVICE_ID_KEY))) {
            // generate new device id
            mIdentityStore.put(DEVICE_ID_KEY, generateDeviceId());
            mIdentityStore.put(DEVICE_ID_TIMESTAMP, String.valueOf(new Date().getTime()));
            mNewDevice = true;
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
        init();
    }

    public boolean isNewDevice() {
        return mNewDevice;
    }

    public Date getDeviceIdCreated() {
        return new Date(Long.parseLong(getIdentityStore().get(DEVICE_ID_TIMESTAMP)));
    }
}
