Feel free to ask any questions in the [AndTweet Google Group](http://groups.google.com/group/andtweet)!
## Contribute to the AndTweet Project ##
Have you been thinking about coding your own features for AndTweet and want to know how best to proceed?

With a few easy steps you can start developing AndTweet features:

> ### 1. [Install Eclipse](http://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/heliosr) ###
  * Some general installation...
  * Integrate Project issues published at http://code.google.com/p/andtweet/issues/list with Eclipse Mylin Plugin using Google code connector.
Update site for Google code connector for Eclipse Mylin plugin:
```
http://knittig.de/googlecode-mylyn-connector/update/
```
Install the MercurialEclipse (was: HgEclipse) plugin for Eclipse using the following update site:
```
http://cbes.javaforge.com/update
```
> ### 2. [Download Android SDK](http://developer.android.com/sdk/index.html) ###
> ### 3. [Install Android SDK](http://developer.android.com/sdk/installing.html) ###
  * To Configure Eclipse's "Java Code Formatter" to use Android coding rules,
    1. Download the formatting file from [here](http://geobeagle.googlecode.com/files/android-formatting.xml)
    1. Then go to 'Preferences > Java > Code Style > Formatter > Import...' and navigate to the file you just downloaded.
  * To Configure Eclipse to organize imports according to Android rules,
    1. Download the importorder file from [here](http://geobeagle.googlecode.com/files/android.importorder)
    1. avigate to 'Preferences > Java > Code Style > Organize Imports > Import ...' and navigate to the file you just downloaded
> ### 4. [Check out the source](http://code.google.com/p/andtweet/source/checkout) ###

Get a local copy of the AndTweet _Mercurial_ repository with this command:
```
hg clone https://andtweet.googlecode.com/hg/ andtweet 
```
To understand how Mercurial works please read the book "[Mercurial: The Definitive Guide](http://mercurial.selenic.com/wiki/MercurialBook)" by Bryan O'Sullivan.


> ### 5. Configure your copy of the AntTweet project for OAuth ###
> > 5.1. Register your "AndTweet-yoursuffix" application
Please see the screenshot below for the  settings of your application:

![http://andtweet.googlecode.com/hg/res-src/AndTweet-yvolk_settings.png](http://andtweet.googlecode.com/hg/res-src/AndTweet-yvolk_settings.png)


> 5.2. Insert your application's private keys to the project:
See "[/src/com/xorcode/andtweet/net/OAuthKeys.java](http://code.google.com/p/andtweet/source/browse/src/com/xorcode/andtweet/net/OAuthKeys.java)" file for more information.
## Debugging ##
> ### Turning debugging info on and off ###
To turn debugging **on** you should:
    * Start shell on your PC, connected to the device
```
sdk/tools/adb -d shell
```
(to start a shell for emulator replace -d with -e in the example above)
or install e.g. "Better Terminal Emulator Magic" on your device and launch it
    * Run the command:
```
setprop log.tag.!AndTweet VERBOSE
```
    * Optionally Check debug level:
```
getprop log.tag.AndTweet
```
To turn debugging **off** (only ERROR level messages will be logged):
    * Same as above, the command is: setprop log.tag.AndTweet ERROR

> ### Restarting your device ###
    * Start shell:
```
android-sdk-windows\tools>adb -d shell
```
    * Stop device:
```
stop
```
    * Start device:
```
start
```