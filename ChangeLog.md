# September 2011. Version 1.1.7 #
  * Working offline or in bad connection conditions is implemented. Now User doesn't have to wait for the "Command execution on a Server" after updating his status (i.e. sending a tweet) and may continue reading and writing tweets, making them Favorite etc. while AndTweet stores all the commands in the queue and retries delivery upon every automatic update and also during any manual command (Reload or Status update). User is being notified about number of messages currently in the Output queue (If Notifications are being enabled). Queues of unsent commands persist in the phone's memory. Even after shutdown for Android v.1.6 and up.
  * Changes in the application permissions. Two (location) permissions were removed, one permission was added to help asynchronous message delivery (ACCESS\_NETWORK\_STATE).
  * Improved user experience during initial configuration till first tweets loading.
  * "Verify" preference moved to the top of the "User Credentials" preference's section, because User can really start from tapping it (typing Username is not required for OAuth)!
  * Widget shows last time when data was successfully downloaded from the server, not when there was last attempt to do this (maybe it failed...)
  * New Preference "Minimum logging level" added to allow changing Logging level from within the application Preferences.
  * Code optimizations and bugs fixes...

# February 2011. Version 1.0.5 #
  * "Favorites" list added, favorites are protected from automatic deletion
  * Click on Widget opens the newest timeline in this order: messages, mentions, tweets
  * Maximum History time (to store tweets) is 1 year now
  * oAuth on by default
  * "Automatic updates" setting is not being turned off by the System