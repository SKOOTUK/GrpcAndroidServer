# GrpcAndroidServer
Server for GrcpAndroid project


## How to run:

 - Enter project directory and run:
   `./gradlew installDist`
   this will generate your server in /build/install/GrpcServer/bin/ directory
 - run the server using command:
   `./build/install/GrpcServer/bin/grpc-server`

## Where is the android client:

https://github.com/Aetherna/GrpcAndroid


## Changelog

- hello world server - basic request / response example
- server streaming - sending updates to a client every few seconds
- client streaming - listening to client location updates
