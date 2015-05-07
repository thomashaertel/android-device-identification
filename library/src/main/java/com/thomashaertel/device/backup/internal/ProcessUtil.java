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
package com.thomashaertel.device.backup.internal;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public final class ProcessUtil {

    private ProcessUtil() {
    }

    public static String getProcessName(Context ctx) {
        String processName = getProcessNameFromActivityManager(ctx);

        if ("".equals(processName)) {
            processName = getProcessNameFromFilesystem();
        }

        return processName;
    }

    public static String getProcessNameFromActivityManager(Context ctx) {
        String processName = "";
        int pid = android.os.Process.myPid();

        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                processName = processInfo.processName;
                break;
            }
        }

        return processName;
    }

    public static String getProcessNameFromFilesystem() {
        StringBuilder sb = new StringBuilder();
        BufferedReader cmdlineReader = null;

        try {
            cmdlineReader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/" + android.os.Process.myPid() + "/cmdline"), "iso-8859-1"));
            int c;
            while ((c = cmdlineReader.read()) > 0) {
                sb.append((char) c);
            }
            return sb.toString();
        } catch (IOException e) {
        } finally {
            if (cmdlineReader != null) {
                try {
                    cmdlineReader.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }
}
