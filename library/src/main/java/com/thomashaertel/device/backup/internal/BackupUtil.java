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

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.os.Process;

import java.util.List;

public final class BackupUtil {
    
    private BackupUtil() {}

    public static List<ProviderInfo> findContentProviders(Context ctx) {
        String processName = ProcessUtil.getProcessName(ctx);
        List<ProviderInfo> providers = ctx.getPackageManager().queryContentProviders(processName, Process.myUid(), 0);
        
        return providers;
    }
}
