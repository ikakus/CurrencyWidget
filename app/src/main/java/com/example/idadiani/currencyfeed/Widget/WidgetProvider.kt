package com.example.idadiani.currencyfeed.Widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import com.example.idadiani.currencyfeed.Classes.RemoteFetchService
import com.example.idadiani.currencyfeed.R
import java.util.*

/**
 * Created by i.dadiani on 2/26/2016.
 */
class WidgetProvider : AppWidgetProvider() {
    private val SYNC_CLICKED: String = "automaticWidgetSyncButtonClick"

    companion object {
        val DATA_FETCHED = "com.example.idadiani.currencyfeed.DATA_FETCHED"
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == DATA_FETCHED) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val remoteViews = updateWidgetListView(context, ComponentName(context.packageName, WidgetProvider::class.java.name).hashCode())
            appWidgetManager.updateAppWidget(ComponentName(context.packageName, WidgetProvider::class.java.name), remoteViews);

        } else if (intent.action == SYNC_CLICKED) {
            Log.i("WidgetProvider ", "Refresh clicked")
            val serviceIntent = Intent(context, RemoteFetchService::class.java)
            context.startService(serviceIntent)
        }
    }

    private fun updateWidgetListView(context: Context, appWidgetId: Int): RemoteViews {

        // which layout to show on widget
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)

        // RemoteViews Service needed to provide adapter for ListView
        val svcIntent = Intent(context, WidgetService::class.java)
        // passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        // setting a unique Uri to the intent
        // don't know its purpose to me right now
        svcIntent.data = Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME))
        // setting adapter to listview of the widget

        //        val appWidgetManager = AppWidgetManager.getInstance(context)
        //        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetListView);
        val appWidgetManager = AppWidgetManager.getInstance(context)
        var appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context.packageName, WidgetProvider::class.java.name))
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetListView);

        remoteViews.setRemoteAdapter(R.id.widgetListView, svcIntent)

        remoteViews.setOnClickPendingIntent(R.id.main_layout, getPendingSelfIntent(context, SYNC_CLICKED));

        var date = Date().toString()
        remoteViews.setTextViewText(R.id.date, date)

        // setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.widgetListView, R.id.empty_view)
        return remoteViews
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        for (i in appWidgetIds.indices) {
            val serviceIntent = Intent(context, RemoteFetchService::class.java)
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i])
            context.startService(serviceIntent)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    fun getPendingSelfIntent(context: Context, action: String): PendingIntent {
        var intent = Intent(context, this.javaClass);
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }


}