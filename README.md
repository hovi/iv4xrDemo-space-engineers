<img src="./docs/iv4xr_logo_1200dpi.png" width="20%"> Space Engineers Demo

This is a demo for the [iv4XR testing framework](https://github.com/iv4xr-project/aplib), demonstrating that iv4XR test agents can control a game called _Space Engineers_ to perform some testing tasks. This repository is a fork of the [*Lab Recruits* demo](https://github.com/iv4xr-project/iv4xrDemo).

It is not intended for general uses yet, other than as a testing project for the development of the [Space Engineers iv4XR plugin](https://github.com/iv4xr-project/iv4xr-se-plugin). For more details please refer to the plugin repository README. 

<img src="./docs/SE-sotf1.png" width="100%">

# Setup

## Requirements

Project requires [aplib](https://github.com/iv4xr-project/aplib) dependency. First build aplib and install it to maven local.
Other possibility at the moment is to change dependency do jitpack:

```
implementation 'com.github.iv4xr-project:aplib:v1.2.0'
```

## Clone repo:

```
git clone git@github.com:iv4xr-project/iv4xrDemo-space-engineers.git
```

## Running unit tests

We are using gradle as build system. To build and run unit tests, run:

```
./gradlew :cleanJvmTest :jvmTest --tests "spaceEngineers.mock.*"
```

## Running iv4xr tests

Require Space Engineers running with iv4xr plugin enabled.


```
./gradlew :cleanJvmTest :jvmTest --tests "spaceEngineers.iv4xr.*"
```


## Running BDD feature tests

Tests scenarios require Space Engineers running with iv4xr plugin enabled.

So far we run BDD tests from Idea.

* Right-click .feature file in Jetbrains IDEA (in src/jvmTest/resources/features) and select "Run". 