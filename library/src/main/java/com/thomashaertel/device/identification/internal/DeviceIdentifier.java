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
package com.thomashaertel.device.identification.internal;

import android.Manifest.permission;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.thomashaertel.device.identification.internal.util.HashUtil;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public final class DeviceIdentifier {

    public static final String UNKNOWN = "unknown";
    private static final String PROPERTY_SERIAL_NO = "ro.serialno";

    /**
     * see http://code.google.com/p/android/issues/detail?id=10603
     */
    private static final String ANDROID_ID_BUG_MSG = "The device suffers from "
            + "the Android ID bug - its ID is the emulator ID : "
            + IDs.BUGGY_ANDROID_ID;
    private static volatile String deviceId; // volatile needed - see EJ item 71
    private static volatile String uuid; // volatile needed - see EJ item 71
    private static volatile String md5hash; // volatile needed - see EJ item 71

    private DeviceIdentifier() {
    }
    // need lazy initialization to get a context

    /**
     * Returns a unique identifier for this device. The first (in the order the
     * enums constants as defined in the IDs enum) non null identifier is
     * returned or a DeviceIDException is thrown. A DeviceIDException is also
     * thrown if ignoreBuggyAndroidID is false and the device has the Android ID
     * bug
     *
     * @param ctx                  an Android constant (to retrieve system services)
     * @param ignoreBuggyAndroidID if false, on a device with the android ID bug, the buggy
     *                             android ID is not returned instead a DeviceIDException is
     *                             thrown
     * @return a *device* ID - null is never returned, instead a DeviceIDException is thrown
     * @throws DeviceIDException if none of the enum methods manages to return a device ID
     */
    public static String getDeviceIdentifier(Context ctx, boolean ignoreBuggyAndroidID) throws DeviceIDException {
        String result = deviceId;

        if (result == null) {
            synchronized (DeviceIdentifier.class) {
                result = deviceId;

                if (result == null) {
                    for (IDs id : IDs.values()) {
                        try {
                            result = deviceId = id.getId(ctx);
                        } catch (DeviceIDNotUniqueException e) {
                            if (!ignoreBuggyAndroidID)
                                throw new DeviceIDException(e);
                        }

                        if (result != null) return result;
                    }

                    throw new DeviceIDException();
                }
            }
        }

        return result;
    }

    /**
     * Returns a UUID specific to the device. There are possibly some instances where this does
     * not work e.g. in the emulator or if there is no SIM in the phone. Then a DeviceIDException
     * is thrown. A DeviceIDException is also thrown if ignoreBuggyAndroidID is false and the device
     * has the Android ID bug. If ignoreBuggyAndroidID is true and the device has the Android ID bug
     * a pseudo ID is taken instead.
     *
     * @param ctx                  an Android constant (to retrieve system services)
     * @param ignoreBuggyAndroidID if false, on a device with the android ID bug, the buggy
     *                             android ID is not returned instead a DeviceIDException is
     *                             thrown
     * @return a *device* ID - null is never returned, instead a DeviceIDException is thrown
     * @throws DeviceIDException if none of the enum methods manages to return a device ID
     * see http://stackoverflow.com/questions/4402262/device-identifier-of-android-emulator
     */
    public static String getDeviceIdentifierUuid(Context ctx, boolean ignoreBuggyAndroidID) throws DeviceIDException {
        String result = uuid;

        if (result == null) {
            synchronized (DeviceIdentifier.class) {
                result = uuid;

                if (result == null) {
                    final String deviceId = IDs.IMSI.getId(ctx);
                    final String serialNo = IDs.SIM_SERIAL_NO.getId(ctx);

                    UUID deviceUuid = null;

                    // compute least significant bits for uuid
                    final long lsb = ((long) deviceId.hashCode() << 32) | serialNo.hashCode();

                    try {
                        final String androidId = IDs.ANDROID_ID.getId(ctx);
                        deviceUuid = new UUID(androidId.hashCode(), lsb);
                    } catch (DeviceIDNotUniqueException e) {
                        if (!ignoreBuggyAndroidID)
                            throw new DeviceIDException(e);
                        final String androidId = getPseudoDeviceId();
                        deviceUuid = new UUID(androidId.hashCode(), lsb);
                    }

                    result = uuid = deviceUuid.toString();
                }
            }
        }
        return result;
    }

    /**
     * Returns a UUID specific to the device. There are possibly some instances where this does
     * not work e.g. in the emulator or if there is no SIM in the phone. Then a DeviceIDException
     * is thrown. A DeviceIDException is also thrown if ignoreBuggyAndroidID is false and the device
     * has the Android ID bug. If ignoreBuggyAndroidID is true and the device has the Android ID bug
     * a pseudo ID is taken instead.
     *
     * @param ctx                  an Android constant (to retrieve system services)
     * @param ignoreBuggyAndroidID if false, on a device with the android ID bug, the buggy
     *                             android ID is not returned instead a DeviceIDException is
     *                             thrown
     * @return a *device* ID - null is never returned, instead a DeviceIDException is thrown
     * @throws DeviceIDException if none of the enum methods manages to return a device ID
     * see http://www.pocketmagic.net/android-unique-device-id/
     */
    public static String getDeviceIdentifierMd5(Context ctx, boolean ignoreBuggyAndroidID) throws DeviceIDException {
        String result = md5hash;

        if (result == null) {
            synchronized (DeviceIdentifier.class) {
                result = md5hash;

                if (result == null) {
                    StringBuilder sb = new StringBuilder();
                    for (IDs id : IDs.values()) {
                        try {
                            sb.append(id.getId(ctx));
                        } catch (DeviceIDNotUniqueException e) {
                            if (!ignoreBuggyAndroidID)
                                throw new DeviceIDException(e);
                        }

                        try {
                            result = md5hash = HashUtil.computeHashMD5(sb.toString());
                        } catch (NoSuchAlgorithmException e) {
                            throw new DeviceIDException(e);
                        }

                        if (result != null) return result;
                    }

                    throw new DeviceIDException();
                }
            }
        }
        return result;
    }


    /**
     * Returns the IMEI for GSM and the MEID or ESN for CDMA phones.
     *
     * @param ctx an Android constant (to retrieve system services)
     * @return the IMEI or MEID - null is never returned, instead a DeviceIDException is thrown
     * @throws DeviceIDException if none of the enum methods manages to return a device ID
     */
    public static synchronized String getImei(Context ctx) throws DeviceIDException {
        return IDs.IMEI.getId(ctx);
    }

    /**
     * Returns the IMSI of a phone.
     *
     * @param ctx an Android constant (to retrieve system services)
     * @return the IMSI - null is never returned, instead a DeviceIDException is thrown
     * @throws DeviceIDException if none of the enum methods manages to return a device ID
     */
    public static synchronized String getImsi(Context ctx) throws DeviceIDException {
        return IDs.IMSI.getId(ctx);
    }

    /**
     * Returns the sim card serial number of a phone.
     *
     * @param ctx an Android constant (to retrieve system services)
     * @return the sim serial number - null is never returned, instead a DeviceIDException is thrown
     * @throws DeviceIDException if none of the enum methods manages to return a device ID
     */
    public static synchronized String getSimSerialNo(Context ctx) throws DeviceIDException {
        return IDs.SIM_SERIAL_NO.getId(ctx);
    }

    /**
     * Returns the serial number of a device.
     *
     * @param ctx an Android constant (to retrieve system services)
     * @return the device serial number - null is never returned, instead a DeviceIDException is thrown
     * @throws DeviceIDException if none of the enum methods manages to return a device ID
     */
    public static synchronized String getSerialNo(Context ctx) throws DeviceIDException {
        return IDs.SERIAL_NO.getId(ctx);
    }

    /**
     * Returns the unique DeviceID
     * Works for Android 2.2 and above, but buggy on some devices
     *
     * @param ctx an Android constant (to retrieve system services)
     * @return the android id - null is never returned, instead a DeviceIDException is thrown
     * @throws DeviceIDException if none of the enum methods manages to return a device ID*
     */
    public static synchronized String getAndroidId(Context ctx) throws DeviceIDException {
        return IDs.ANDROID_ID.getId(ctx);
    }

    /**
     * Returns the unique wifi hardware address
     * Works only if wifi module is present
     *
     * @param ctx an Android constant (to retrieve system services)
     * @return the wifi MAC address - null is never returned, instead a DeviceIDException is thrown
     * @throws DeviceIDException if none of the enum methods manages to return a device ID*
     */
    public static synchronized String getWifiId(Context ctx) throws DeviceIDException {
        return IDs.WIFI_MAC.getId(ctx);
    }

    /**
     * Returns the unique bluetooth hardware address
     * Works only if bluetooth module is present
     *
     * @param ctx an Android constant (to retrieve system services)
     * @return the wifi MAC address - null is never returned, instead a DeviceIDException is thrown
     * @throws DeviceIDException if none of the enum methods manages to return a device ID*
     */
    public static synchronized String getBluetoothId(Context ctx) throws DeviceIDException {
        return IDs.BLUETOOTH_MAC.getId(ctx);
    }

    /**
     * Returns a generated unique pseudo ID from android.os.Build Constants
     * Works for all devices and Android versions
     *
     * @return a pseudo id - null is never returned
     * see http://www.pocketmagic.net/2011/02/android-unique-device-id/
     */
    public static String getPseudoDeviceId() {
        return "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
    }


    private static void assertPermission(Context ctx, String perm) {
        final int checkPermission = ctx.getPackageManager().checkPermission(
                perm, ctx.getPackageName());
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Permission " + perm + " is required");
        }
    }

    private static enum IDs {
        IMEI {
            /**
             * getDeviceId() function Returns the unique device ID.
             * for example,the IMEI for GSM and the MEID or ESN for CDMA phones.
             */
            @Override
            String getId(Context ctx) {
                final TelephonyManager tm = (TelephonyManager) ctx
                        .getSystemService(Context.TELEPHONY_SERVICE);
                if (tm == null) {
                    w("Telephony Manager not available");
                    return null;
                }
                assertPermission(ctx, permission.READ_PHONE_STATE);
                return tm.getDeviceId();
            }
        },
        IMSI {
            /**
             * getSubscriberId() function Returns the unique subscriber ID,
             * for example, the IMSI for a GSM phone.
             */
            @Override
            String getId(Context ctx) {
                final TelephonyManager tm = (TelephonyManager) ctx
                        .getSystemService(Context.TELEPHONY_SERVICE);
                if (tm == null) {
                    w("Telephony Manager not available");
                    return null;
                }
                assertPermission(ctx, permission.READ_PHONE_STATE);
                return tm.getSubscriberId();
            }
        },
        SIM_SERIAL_NO {
            /**
             * getSimSerialNumber() function Returns the unique sim card ID,
             * for example, the SIM Card ID for a GSM phone.
             */
            @Override
            String getId(Context ctx) {
                final TelephonyManager tm = (TelephonyManager) ctx
                        .getSystemService(Context.TELEPHONY_SERVICE);
                if (tm == null) {
                    w("Telephony Manager not available");
                    return null;
                }
                assertPermission(ctx, permission.READ_PHONE_STATE);
                return tm.getSimSerialNumber();
            }
        },
        SERIAL_NO {
            /**
             * System Property ro.serialno returns the serial number as unique number
             * Works for Android 2.3 and above
             */
            @Override
            String getId(Context ctx) {
                // no permission needed !
                return SystemPropertiesProxy.get(ctx, PROPERTY_SERIAL_NO, UNKNOWN);
            }
        },
        ANDROID_ID {
            /**
             * Settings.Secure.ANDROID_ID returns the unique DeviceID
             * Works for Android 2.2 and above
             */
            @Override
            String getId(Context ctx) throws DeviceIDException {
                // no permission needed !
                final String androidId = Secure.getString(
                        ctx.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
                if (BUGGY_ANDROID_ID.equals(androidId)) {
                    e(ANDROID_ID_BUG_MSG);
                    throw new DeviceIDNotUniqueException();
                }
                return androidId;
            }
        },
        WIFI_MAC {
            /**
             * getMacAddress() returns the unique wifi hardware address
             * Works only if wifi module is present
             */
            @Override
            String getId(Context ctx) {
                WifiManager wm = (WifiManager) ctx
                        .getSystemService(Context.WIFI_SERVICE);
                if (wm == null) {
                    w("Wifi Manager not available");
                    return null;
                }
                assertPermission(ctx, permission.ACCESS_WIFI_STATE);
                // I guess getMacAddress() has no java doc !!!
                return wm.getConnectionInfo().getMacAddress();
            }
        },
        BLUETOOTH_MAC {
            /**
             * getMacAddress() returns the unique bluetooth hardware address
             * Works only if bluetooth module is present
             */
            @Override
            String getId(Context ctx) {
                BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
                if (ba == null) {
                    w("Bluetooth Adapter not available");
                    return null;
                }
                assertPermission(ctx, permission.BLUETOOTH);
                return ba.getAddress();
            }
        },
        PSEUDO_ID {
            /**
             * returns a generated pseudo unique ID
             * Works for all devices and Android versions
             */
            @Override
            String getId(Context ctx) {
                return getPseudoDeviceId();
            }
        };

        static final String BUGGY_ANDROID_ID = "9774d56d682e549c";
        private final static String TAG = IDs.class.getSimpleName();

        private static void w(String msg) {
            Log.w(TAG, msg);
        }

        private static void e(String msg) {
            Log.e(TAG, msg);
        }

        abstract String getId(Context ctx) throws DeviceIDException;
    }

    // =========================================================================
    // Exceptions
    // =========================================================================
    public static class DeviceIDException extends Exception {

        private static final long serialVersionUID = -8083699995384519417L;
        private static final String NO_ANDROID_ID = "Could not retrieve a device ID";

        public DeviceIDException(Throwable throwable) {
            super(NO_ANDROID_ID, throwable);
        }

        public DeviceIDException(String detailMessage) {
            super(detailMessage);
        }

        public DeviceIDException() {
            super(NO_ANDROID_ID);
        }
    }

    public static final class DeviceIDNotUniqueException extends
            DeviceIDException {

        private static final long serialVersionUID = -8940090896069484955L;

        public DeviceIDNotUniqueException() {
            super(ANDROID_ID_BUG_MSG);
        }
    }
}