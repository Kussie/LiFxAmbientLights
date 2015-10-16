# LiFxAmbientLights

My very first java app, an ambient light for LiFx.

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