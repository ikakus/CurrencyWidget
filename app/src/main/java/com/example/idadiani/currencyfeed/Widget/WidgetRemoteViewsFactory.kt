package com.example.idadiani.currencyfeed.Widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.idadiani.currencyfeed.Classes.Parser
import com.example.idadiani.currencyfeed.R

/**
 * Created by i.dadiani on 2/26/2016.
 */
class WidgetRemoteViewsFactory(context: Context, intent: Intent, widgetList: MutableList<Parser.Record>) : RemoteViewsService.RemoteViewsFactory {


    private var context: Context? = null
    private val appWidgetId: Int

    private var widgetList: MutableList<Parser.Record> = mutableListOf()
        set(value) {
            widgetList = value
        }

    init {
        this.context = context
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        Log.d("AppWidgetId", appWidgetId.toString())
        this.widgetList = widgetList
    }

    private fun updateWidgetListView() {
    }

    override fun getCount(): Int {
        return widgetList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // TODO Auto-generated method stub
    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewAt(position: Int): RemoteViews {
        Log.d("WidgetCreatingView", "WidgetCreatingView")
        val remoteView = RemoteViews(context!!.packageName, R.layout.listview_row_item)


        Log.d("Loading", widgetList[position].toString())
        remoteView.setTextViewText(R.id.text, widgetList[position].toString())

        return remoteView
    }

    // TODO Auto-generated method stub


    override fun hasStableIds(): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun onCreate() {
        // TODO Auto-generated method stub
        updateWidgetListView()
    }

    override fun onDataSetChanged() {
        // TODO Auto-generated method stub
        updateWidgetListView()
    }

    override fun onDestroy() {
        // TODO Auto-generated method stub
        widgetList.clear()
    }

    override fun getViewTypeCount(): Int {
        return 1;
    }

}