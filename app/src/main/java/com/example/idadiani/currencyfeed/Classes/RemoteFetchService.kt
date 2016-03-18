package com.example.idadiani.currencyfeed.Classes

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.idadiani.currencyfeed.Interfaces.LoadingDoneListener
import com.example.idadiani.currencyfeed.Widget.WidgetProvider
import java.util.*

/**
 * Created by i.dadiani on 3/17/2016.
 */
class RemoteFetchService : Service(), LoadingDoneListener {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    companion object {
        var listItemList: ArrayList<Parser.Record> = ArrayList()
    }


    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    /*
     * Retrieve appwidget id from intent it is needed to update widget later
     * initialize our AQuery class
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID))
            appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID)

        fetchDataFromWeb()
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * method which fetches data(json) from web aquery takes params
     * remoteJsonUrl = from where data to be fetched String.class = return
     * format of data once fetched i.e. in which format the fetched data be
     * returned AjaxCallback = class to notify with data once it is fetched
     */
    private fun fetchDataFromWeb() {
        val parser = Parser()
        parser.parse(this)
        Log.i("RemoteFetchService ", "Parser started")
    }


    /**
     * Method which sends broadcast to WidgetProvider
     * so that widget is notified to do necessary action
     * and here action == WidgetProvider.DATA_FETCHED
     */
    private fun populateWidget() {

        val widgetUpdateIntent = Intent()
        widgetUpdateIntent.action = WidgetProvider.DATA_FETCHED
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        sendBroadcast(widgetUpdateIntent)

        this.stopSelf()
    }


    override fun loaded(records: MutableList<Parser.Record>) {
        listItemList = records as ArrayList<Parser.Record>
        populateWidget()
    }


}
