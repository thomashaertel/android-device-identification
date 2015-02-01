# Android Device Identification
[![Download](https://api.bintray.com/packages/thomashaertel/maven/android-device-identification/images/download.svg) ](https://bintray.com/thomashaertel/maven/android-device-identification/_latestVersion)
[![Build Status](https://travis-ci.org/thomashaertel/android-device-identification.svg?branch=master)](https://travis-ci.org/thomashaertel/android-device-identification)

Library for unique identification of an Android device. Provides also a backup solution

## Overview
TBD

## Usage
TBD


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
  compile('com.thomashaertel:android-device-identification :0.0.1@aar') {
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

## License

* [The Apache Software License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)