package com.xorcode.andtweet.appwidget;

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
 * A widget provider. If uses AndTweetAppWidgetData to store preferences and to
 * accumulate data (notifications...) received.
 * 
 * <p>
 * See also the following files:
 * <ul>
 * <li>AndTweetAppWidgetData.java</li>
 * <li>AndTweetAppWidgetConfigure.java</li>
 * <li>res/layout/appwidget_configure.xml</li>
 * <li>res/layout/appwidget.xml</li>
 * <li>res/xml/appwidget_provider.xml</li>
 * </ul>
 * 
 * @author yvolk (Yuri Volkov), http://yurivolkov.com
 */
public class AndTweetAppWidgetProvider extends AppWidgetProvider {
	// log tag
	private static final String TAG = AndTweetAppWidgetProvider.class
			.getSimpleName();

	private int msgType = AndTweetService.NOTIFY_INVALID;
	private int numSomethingReceived = 0;

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
				numSomethingReceived = extras
						.getInt(AndTweetService.EXTRA_NUMTWEETS);
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
		} else if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			Log.d(TAG, "Action APPWIDGET_DELETED was received");
			Bundle extras = intent.getExtras();
			if (extras != null) {
				int[] appWidgetIds = extras
						.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
				if (appWidgetIds != null && appWidgetIds.length > 0) {
					onDeleted(context, appWidgetIds);
					done = true;
				} else {
					// For some reason this is required for Android v.1.5
					int appWidgetId = extras
					.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
					if (appWidgetId != 0) {
						int[] appWidgetIds2 = {appWidgetId};
						onDeleted(context, appWidgetIds2);
						done = true;
					}
				}
			} 
			if (!done) {
				Log.d(TAG, "Deletion was not done, extras='" + extras.toString() + "'");
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
		// When the user deletes the widget, delete all preferences associated
		// with it.
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			new AndTweetAppWidgetData(context, appWidgetIds[i]).delete();
		}
	}

	@Override
	public void onEnabled(Context context) {
		Log.d(TAG, "onEnabled");
	}

	@Override
	public void onDisabled(Context context) {
		Log.d(TAG, "onDisabled");

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

		// TODO:
		// see /ApiDemos/src/com/example/android/apis/app/AlarmController.java
		// on how to implement AlarmManager...

		AndTweetAppWidgetData data = new AndTweetAppWidgetData(context,
				appWidgetId);
		data.load();

		// Calculate new values
		switch (msgType) {
		case NOTIFY_REPLIES:
			data.numMentions += numSomethingReceived;
			break;

		case NOTIFY_DIRECT_MESSAGE:
			data.numMessages += numSomethingReceived;
			break;

		case NOTIFY_TIMELINE:
			data.numTweets += numSomethingReceived;
			break;

		case NOTIFY_CLEAR:
			data.numMentions = 0;
			data.numMessages = 0;
			data.numTweets = 0;
			break;

		default:
			// change nothing
		}

		// TODO: Widget design...
		String widgetTitle = "";
		String widgetText = "";
		
		widgetTitle = data.titlePref;
		boolean isFound = false;

		if (data.numMentions > 0) {
			isFound = true;
			widgetText += (widgetText.length()>0 ? "; " : "")
					+ formatMessage(context,
							R.string.notification_new_mention_format,
							data.numMentions,
							R.string.notification_mention_singular,
							R.string.notification_mention_plural);
		}
		if (data.numMessages > 0) {
			isFound = true;
			widgetText += (widgetText.length()>0 ? "; " : "")
					+ formatMessage(context,
							R.string.notification_new_message_format,
							data.numMessages,
							R.string.notification_message_singular,
							R.string.notification_message_plural);
		}
		if (data.numTweets > 0) {
			isFound = true;
			widgetText += (widgetText.length()>0 ? "; " : "")
					+ formatMessage(context,
							R.string.notification_new_tweet_format,
							data.numTweets,
							R.string.notification_tweet_singular,
							R.string.notification_tweet_plural);
		}
		if (!isFound) {
			widgetText += (widgetText.length()>0 ? "; " : "")
			+ context.getText(R.string.notification_clear);
		}

		Log.d(TAG, "updateAppWidget aMessage=\"" + widgetText + "\"");

		// Construct the RemoteViews object. It takes the package name (in our
		// case, it's our
		// package, but it needs this because on the other side it's the widget
		// host inflating
		// the layout from our package).
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.appwidget);
		
		if (widgetTitle.length()==0) {
			views.setViewVisibility(R.id.appwidget_title, android.view.View.GONE);
		} else {
			views.setTextViewText(R.id.appwidget_title, widgetTitle);
		}
		views.setTextViewText(R.id.appwidget_text, widgetText);

		// When user clicks on widget, launch main AndTweet activity
		// Intent defineIntent = new Intent(android.content.Intent.ACTION_MAIN);
		Intent defineIntent = new Intent(context,
				com.xorcode.andtweet.TweetListActivity.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(context,
				0 /* no requestCode */, defineIntent, 0 /* no flags */);
		views.setOnClickPendingIntent(R.id.widget, pendingIntent);

		// Tell the widget manager
		if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			// TODO: Is this right?
			// All instances will be updated
			appWidgetManager.updateAppWidget(new ComponentName(context, this
					.getClass()), views);
		} else {
			data.save();
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

	private String formatMessage(Context context, int messageFormat,
			int numSomething, int singular, int plural) {
		String aMessage = "";
		java.text.MessageFormat form = new java.text.MessageFormat(context
				.getText(messageFormat).toString());
		Object[] formArgs = new Object[] { numSomething };
		double[] tweetLimits = { 1, 2 };
		String[] tweetPart = { context.getText(singular).toString(),
				context.getText(plural).toString() };
		java.text.ChoiceFormat tweetForm = new java.text.ChoiceFormat(
				tweetLimits, tweetPart);
		form.setFormatByArgumentIndex(0, tweetForm);
		aMessage = form.format(formArgs);
		return aMessage;
	}
}
