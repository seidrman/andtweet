/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xorcode.andtweet.appwidget;

import java.text.ChoiceFormat;
import java.text.MessageFormat;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.xorcode.andtweet.R;
import com.xorcode.andtweet.AndTweetService;
import static com.xorcode.andtweet.AndTweetService.*;

/**
 * A widget provider.  We have a string that we pull from a preference in order to show
 * the configuration settings and the current time when the widget was updated.  We also
 * register a BroadcastReceiver for time-changed and timezone-changed broadcasts, and
 * update then too.
 *
 * <p>See also the following files:
 * <ul>
 *   <li>AndTweetAppWidgetConfigure.java</li>
 *   <li>AndTweetBroadcastReceiver.java</li>
 *   <li>res/layout/appwidget_configure.xml</li>
 *   <li>res/layout/appwidget_provider.xml</li>
 *   <li>res/xml/appwidget_provider.xml</li>
 * </ul>
 */
/**
 * @author yvolk@yurivolkov.com
 * 
 */
public class AndTweetAppWidgetProvider extends AppWidgetProvider {
	// log tag
	private static final String TAG = AndTweetAppWidgetProvider.class
			.getSimpleName();
	private int msgType = AndTweetService.NOTIFY_INVALID;
	private int numTweets = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive; intent=" + intent);
		boolean done = false;
		String action = intent.getAction();

		if (AndTweetService.ACTION_APPWIDGET_UPDATE.equals(action)) {
			Log.d(TAG, "Intent from AndTweetService received!");
			Bundle extras = intent.getExtras();
			if (extras != null) {
				msgType = extras.getInt(AndTweetService.EXTRA_MSGTYPE);
				numTweets = extras.getInt(AndTweetService.EXTRA_NUMTWEETS);
				int[] appWidgetIds = extras
						.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
				if (appWidgetIds == null || appWidgetIds.length == 0) {
					/**
					 * Update All AndTweet AppWidgets
					 * */
					appWidgetIds = AppWidgetManager
							.getInstance(context)
							.getAppWidgetIds(
									new ComponentName(context, this.getClass()));
				}
				if (appWidgetIds != null && appWidgetIds.length > 0) {
					onUpdate(context, AppWidgetManager.getInstance(context),
							appWidgetIds);
					done = true;
				}
			}
			if (!done) {
				// This will effectively reset the Widget view
				updateAppWidget(context, AppWidgetManager.getInstance(context),
						AppWidgetManager.INVALID_APPWIDGET_ID);
				done = true;
			}
		}
		if (!done) {
			super.onReceive(context, intent);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.d(TAG, "onUpdate");
		// For each widget that needs an update, get the text that we should
		// display:
		// - Create a RemoteViews object for it
		// - Set the text in the RemoteViews object
		// - Tell the AppWidgetManager to show that views object for the widget.
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.d(TAG, "onDeleted");
		// When the user deletes the widget, delete the preference associated
		// with it.
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			AndTweetAppWidgetConfigure
					.deleteTitlePref(context, appWidgetIds[i]);
		}
	}

	@Override
	public void onEnabled(Context context) {
		Log.d(TAG, "onEnabled");
		// TODO: Delete these lines...
		// When the first widget is created, register for the TIMEZONE_CHANGED
		// and TIME_CHANGED
		// broadcasts. We don't want to be listening for these if nobody has our
		// widget active.
		// This setting is sticky across reboots, but that doesn't matter,
		// because this will
		// be called after boot if there is a widget instance for this provider.
		// PackageManager pm = context.getPackageManager();
		// pm.setComponentEnabledSetting(
		// new ComponentName("com.xorcode.andtweet",
		// ".appwidget.AndTweetBroadcastReceiver"),
		// PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
		// PackageManager.DONT_KILL_APP);

		// This doesn't work:
		// context.registerReceiver(this,
		// new
		// android.content.IntentFilter(android.content.Intent.ACTION_TIMEZONE_CHANGED));

		// See /ApiDemos/src/com/example/android/apis/app/AlarmController.java
		// on how to implement AlarmManager...
	}

	@Override
	public void onDisabled(Context context) {
		Log.d(TAG, "onDisabled");

		// TODO: Delete these lines...
		// When the first widget is created, stop listening for the
		// TIMEZONE_CHANGED and
		// TIME_CHANGED broadcasts.
		// PackageManager pm = context.getPackageManager();
		// pm.setComponentEnabledSetting(
		// new ComponentName("com.xorcode.andtweet",
		// ".appwidget.AndTweetBroadcastReceiver"),
		// PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
		// PackageManager.DONT_KILL_APP);

	}

	/**
	 * Update the AppWidget view (i.e. on the Home screen)
	 * 
	 * @param context
	 * @param appWidgetManager
	 * @param appWidgetId
	 *            Id of the The AppWidget instance which view should be updated
	 */
	void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
			int appWidgetId) {
		Log.d(TAG, "updateAppWidget appWidgetId=" + appWidgetId);

		String titlePref;
		if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			titlePref = "";
		} else {
			titlePref = AndTweetAppWidgetConfigure.loadTitlePref(context,
					appWidgetId);
		}

		// TODO: Widget design...
		int messageTitle = 0;
		int messageFormat = 0;
		int singular = 0;
		int plural = 0;
		String aMessage = "";

		switch (msgType) {
		case NOTIFY_REPLIES:
			messageFormat = R.string.notification_new_mention_format;
			singular = R.string.notification_mention_singular;
			plural = R.string.notification_mention_plural;
			messageTitle = R.string.notification_title_mentions;
			break;

		case NOTIFY_DIRECT_MESSAGE:
			messageFormat = R.string.notification_new_message_format;
			singular = R.string.notification_message_singular;
			plural = R.string.notification_message_plural;
			messageTitle = R.string.notification_title_messages;
			break;

		case NOTIFY_TIMELINE:
			messageFormat = R.string.notification_new_tweet_format;
			singular = R.string.notification_tweet_singular;
			plural = R.string.notification_tweet_plural;
			messageTitle = R.string.notification_title;
			break;

		case NOTIFY_CLEAR:
		default:
			singular = R.string.notification_clear;
			break;
		}

		// Set up the message
		if (messageFormat == 0) {
			aMessage = context.getText(singular).toString();
		} else {
			MessageFormat form = new MessageFormat(context.getText(
					messageFormat).toString());
			Object[] formArgs = new Object[] { numTweets };
			double[] tweetLimits = { 1, 2 };
			String[] tweetPart = { context.getText(singular).toString(),
					context.getText(plural).toString() };
			ChoiceFormat tweetForm = new ChoiceFormat(tweetLimits, tweetPart);
			form.setFormatByArgumentIndex(0, tweetForm);
			aMessage = titlePref + " " + form.format(formArgs) + " "
			// + context.getText(messageTitle)
			;
		}

		Log.d(TAG, "updateAppWidget aMessage=\"" + aMessage + "\"");

		// Getting the string this way allows the string to be localized. The
		// format
		// string is filled in using java.util.Formatter-style format strings.
		CharSequence text = aMessage;

		// Construct the RemoteViews object. It takes the package name (in our
		// case, it's our
		// package, but it needs this because on the other side it's the widget
		// host inflating
		// the layout from our package).
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.appwidget_provider);
		views.setTextViewText(R.id.appwidget_text, text);

		// When user clicks on widget, launch main AndTweet activity
		// Intent defineIntent = new Intent(android.content.Intent.ACTION_MAIN);
		Intent defineIntent = new Intent(context,
				com.xorcode.andtweet.TweetListActivity.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(context,
				0 /* no requestCode */, defineIntent, 0 /* no flags */);
		views.setOnClickPendingIntent(R.id.widget, pendingIntent);

		// Tell the widget manager
		if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			appWidgetManager.updateAppWidget(new ComponentName(context, this
					.getClass()), views);
		} else {
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
}
