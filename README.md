# LiFxAmbientLights

My very first Java app, an ambient light for LiFx.  Fair warning the code is extremely sloppy, i did this app purely as a learning project and had absolutely no Java experience before i attempted this.

### Basic Info

Download the JAR:
https://github.com/Kussie/LiFxAmbientLights/raw/master/jar/Lights.jar


To Run:
java -jar Lights.jar api_token bulb_selector screen_width screen_height

For example:
java -jar Lights.jar api_token all 2560 1440

Get a token here:
https://cloud.lifx.com/settings

Selectors:
"all" - Update all bulbs
"group:name" - where name is the label of a group.

For more: http://api.developer.lifx.com/docs/selectors

### Todo
 - [ ] Use local WiFi instead of HTTP API
 - [ ] Figure out why the colour flickers when changing sometimes
 - [ ] Vastly improve code quality
 - [ ] Learn more Java
 - [ ] Get light settings before starting and revert to them when exiting app
 - [ ] Make a GUI to configure the options rather then passing them as arguments via console
 - [ ] Add Brightness option


### License

This application is open-sourced software licensed under the [MIT license](http://opensource.org/licenses/MIT)