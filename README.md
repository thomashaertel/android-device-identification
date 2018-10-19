# Android Device Identification
[![Download](https://api.bintray.com/packages/thomashaertel/maven/android-device-identification/images/download.svg) ](https://bintray.com/thomashaertel/maven/android-device-identification/_latestVersion)
[![Build Status](https://travis-ci.org/thomashaertel/android-device-identification.svg?branch=master)](https://travis-ci.org/thomashaertel/android-device-identification)

Library for unique identification of an Android device. Provides also a backup solution

## Overview
TBD

## Release Notes:
0.0.2

* A secure preference store is used for holding the device specific data.
* Backup manager helper added for backing up preferences and sqlite databases.

0.0.1

* Initial import to github. I've added several ways to identify an android device.
* device specific data is written to shared preferences.

## Usage

To make use of the device identification you only need to add a `DeviceIdentityProvider` to your activity:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ...

    identityProvider = DeviceIdentityProvider.getInstance(this);

    // force backup for new device immediately
    if (identityProvider.isNewDevice()) {
        BackupManager backupManager = new BackupManager(this);
        backupManager.dataChanged();
    }

    ...
}
```

If you want to automatically backup your application data to android backup service you need to add some snippets to the applications manifest an activity:
 
```xml
<application android:backupAgent="com.thomashaertel.device.backup.SimpleBackupAgent" android:allowBackup="true" ...>
    <meta-data android:name="com.google.android.backup.api_key" android:value="your-api-key" />
    ...
</application>
```

```java
@Override
protected void onStop() {
    // allow backup on authorized devices only
    if (identityProvider.isAuthorizedDevice()) {
        BackupManager backupManager = new BackupManager(this);
        backupManager.dataChanged();
    }

    super.onStop();
}
```

## Building
### Gradle

#### From Bintray

Add maven central to your `build.gradle`:

```groovy
buildscript {
  repositories {
    jcenter()
  }
}

repositories {
  jcenter()
}
```

#### From maven central

Add maven central to your `build.gradle`:

```groovy
buildscript {
  repositories {
    mavenCentral()
  }
}

repositories {
  mavenCentral()
}
```

Then declare android-device-identification within your dependencies:

```groovy
dependencies {
  ...
  compile('com.thomashaertel:android-device-identification :0.0.2@aar') {
  }
  ...
}
```

### Maven

#### From maven central

To use android-device-identification within your maven build simply add

```xml
<dependency>
  <artifactId>android-device-identification </artifactId>
  <version>${android-device-identification .version}</version>
  <groupId>com.thomashaertel</groupId>
</dependency>
```

to your pom.xml

If you also want the sources or javadoc add the respective classifier

```xml
  <classifier>sources</classifier>
```

or

```xml
  <classifier>javadoc</classifier>
```
to the dependency.

## Contributing
For making contributions please send me pull requests, but also bugs and enhancement requests are welcome. Although no guarantees on when I can review them.

## License

* [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
