# fablab-android [![Build Status](https://travis-ci.org/FAU-Inf2/fablab-android.svg?branch=master)](https://travis-ci.org/FAU-Inf2/fablab-android)

fablab-android is an Android application that uses a [Dropwizard](http://www.dropwizard.io) based [REST-server](https://github.com/FAU-Inf2/fablab-server).

## Build

To build the app, run:

	./gradlew build

This will also integrate the [fablab-common](https://github.com/FAU-Inf2/fablab-common) repository that contains interfaces and entities used for both the server and the Android application.

To make use of the JaxRs interfaces in the Android application, apply the following gradle task:

	./gradlew jaxRs2Retrofit-debug

This will generate a java-gen folder which contains the transformed JaxRs interfaces from the fablab-common repository. These interfaces can be used by Retrofit for implementing a REST-client.


## Libraries
fablab-android uses the following libraries and software:
* [Retrofit](http://square.github.io/retrofit/) License: [Apache License Version 2.0](http://square.github.io/retrofit/#license)
* [JaxRs2Retrofit] (https://github.com/Maddoc42/JaxRs2Retrofit) License: [Apache License Version 2.0](https://github.com/Maddoc42/JaxRs2Retrofit#license)
* [OkHttp](http://square.github.io/okhttp/) License [Apache License Version 2.0](http://square.github.io/okhttp/#license)
* [okio](https://github.com/square/okio) License [Apache License Version 2.0](https://github.com/square/okio/blob/master/LICENSE.txt)
* [zxing](https://github.com/zxing/zxing) License [Apache License Version 2.0](https://github.com/zxing/zxing/wiki/License-Questions)

## Licence

## Contact
Feel free to contact us: fablab@i2.cs.fau.de

