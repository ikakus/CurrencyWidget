package com.example.idadiani.currencyfeed.Widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.idadiani.currencyfeed.Classes.RemoteFetchService
import com.example.idadiani.currencyfeed.R

/**
 * Created by i.dadiani on 3/11/2016.
 */
class ListViewProvider(context: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {
    private var context: Context? = null
    private val appWidgetId: Int

    init {
        this.context = context
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
    }

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int {
        return RemoteFetchService.listItemList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    /*
    *Similar to getView of Adapter where instead of View
    *we return RemoteViews
    *
    */
    override fun getViewAt(position: Int): RemoteViews {
        val remoteView = RemoteViews(context!!.packageName, R.layout.listview_row_item)
        val listItem = RemoteFetchService.listItemList[position]
        remoteView.setTextViewText(R.id.text, listItem.data?.elementAt(0) + " " + listItem.data?.elementAt(1) + " " + listItem.data?.elementAt(2) + " " + listItem.data?.elementAt(3))
        return remoteView
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

}
