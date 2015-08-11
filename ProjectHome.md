**Note**: On June 12, 2013 AndTweet stopped working due to the recent changes in Twitter.com (read more here: [API v1 Retirement is Complete - Use API v1.1](https://dev.twitter.com/blog/api-v1-is-retired)).
AndStatus application (and the Open Source project with the same name) continues development of AndTweet since December 2011. Multiple accounts support, Following a User, Conversation view and Direct message sending are there already. Switch and move forward!
More info at [AndStatus wiki](https://github.com/andstatus/andstatus/wiki).

## Overview of AndTweet ##
AndTweet application is being designed to be a light-weight open Twitter alternative for Android. It allows you to read tweets, send Status updates, create Favorites etc. even when you are offline. Optimized for fast operation using both Touch and Keyboard.

AndTweet is an open project:
  * Open for user's ideas. Please share your ideas with community and vote for implementation of the best ideas at the [AndTweet Ideas Forum](http://andtweet.uservoice.com/forums/22553-general).
  * Open Source, open for contributing developers. If you're a developer, AndTweet project will allow you to implement easily your unique Twitter client features, because you can build on the working code. Please read the [DeveloperFAQ](http://code.google.com/p/andtweet/wiki/DeveloperFAQ) page for more info.
  * Multilingual, translated into 3 languages already (English, German, [Russian](http://yurivolkov.com/Android/AndTweet/index_ru.html)). New translations are welcomed!
See the ScreenShots to help you convince yourself that it's really worth to be installed on your Android device.

In December 2011 [AndStatus](https://github.com/andstatus/andstatus/wiki) project forked from AndTweet. The new project was created specifically as a community initiative, not associated with any person, commercial entity and not tied to Twitter only. See http://andstatus.org

## Current version ##
September 2011. Version 1.1.7
  * Working offline or in bad connection conditions is implemented. Now User doesn't have to wait for the "Command execution on a Server" after updating his status (i.e. sending a tweet) and may continue reading and writing tweets, making them Favorite etc. while AndTweet stores all the commands in the queue and retries delivery upon every automatic update and also during any manual command (Reload or Status update). User is being notified about number of messages currently in the Output queue (If Notifications are being enabled). Queues of unsent commands persist in the phone's memory. Even after shutdown for Android v.1.6 and up.
  * Changes in the application permissions. Two (location) permissions were removed, one permission was added to help asynchronous message delivery (ACCESS\_NETWORK\_STATE).
  * Improved user experience during initial configuration till first tweets loading.
  * "Verify" preference moved to the top of the "User Credentials" preference's section, because User can really start from tapping it (typing Username is not required for OAuth)!
  * Widget shows last time when data was successfully downloaded from the server, not when there was last attempt to do this (maybe it failed...)
  * New Preference "Minimum logging level" added to allow changing Logging level from within the application Preferences.
  * Code optimizations and bugs fixed...
## Previous version ##
February 2011. Version 1.0.5
  * "Favorites" list added, favorites are protected from automatic deletion
  * Click on Widget opens the newest timeline in this order: messages, mentions, tweets
  * Maximum History time (to store tweets) is 1 year now
  * oAuth on by default
  * "Automatic updates" setting is not being turned off by the System
## Ongoing work on the project ##
AndTweet is being actively developed. Please see the "Updates" link above to be sure :-)

What will be next? - We need to think...

## AndTweet project stats by Ohloh.net ##
&lt;wiki:gadget url="http://www.ohloh.net/p/309265/widgets/project\_basic\_stats.xml" height="350"  border="0" /&gt;