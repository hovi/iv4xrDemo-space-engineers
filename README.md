<img src="./docs/iv4xr_logo_1200dpi.png" width="20%"> Space Engineers Demo

This is a demo for the [iv4XR testing framework](https://github.com/iv4xr-project/aplib), demonstrating that iv4XR test agents can control [_Space Engineers_](https://www.spaceengineersgame.com/) (a game by [Keen Software House](https://www.keenswh.com/)) to perform some testing tasks. This repository started as a fork of the [*Lab Recruits* demo](https://github.com/iv4xr-project/iv4xrDemo), but has been significantly modified since.

It is not intended for general uses, other than as a testing project for the development of the [Space Engineers iv4XR plugin](https://github.com/iv4xr-project/iv4xr-se-plugin). For more details, please refer to the plugin repository README. 

<img src="./docs/SE-sotf1.png" width="100%">

# Setup

## Requirements

Project requires [aplib](https://github.com/iv4xr-project/aplib) dependency. First build `aplib` and install it to Maven local.
Other possibility at the moment is to change dependency do JitPack:

```
implementation 'com.github.iv4xr-project:aplib:v1.2.0'
```

## Clone repo:

```
git clone git@github.com:iv4xr-project/iv4xrDemo-space-engineers.git
```

## Running unit tests

We are using Gradle as the build system. To build and run unit tests, run:

```
./gradlew :cleanJvmTest :jvmTest --tests "spaceEngineers.mock.*"
```

## Running iv4xr tests

The tests require Space Engineers running with the iv4XR plugin enabled.


```
./gradlew :cleanJvmTest :jvmTest --tests "spaceEngineers.iv4xr.*"
```


## Running BDD feature tests

Tests scenarios also require Space Engineers running with the iv4XR plugin enabled.

For now we run BDD tests from Idea.

* Right-click .feature file in Jetbrains IDEA (in src/jvmTest/resources/features) and select "Run". 