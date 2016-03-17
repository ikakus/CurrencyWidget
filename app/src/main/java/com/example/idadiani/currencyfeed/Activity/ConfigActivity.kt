package com.example.idadiani.currencyfeed.Activity

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener

import com.example.idadiani.currencyfeed.Classes.RemoteFetchService
import com.example.idadiani.currencyfeed.R

class ConfigActivity : Activity(), OnClickListener {

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.configactivity)

        assignAppWidgetId()
        findViewById(R.id.widgetStartButton).setOnClickListener(this)
    }

    /**
     * Widget configuration activity,always receives appwidget Id appWidget Id =
     * unique id that identifies your widget analogy : same as setting view id
     * via @+id/viewname on layout but appwidget id is assigned by the system
     * itself
     */
    private fun assignAppWidgetId() {
        val extras = intent.extras
        if (extras != null)
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.widgetStartButton)
            startWidget()
    }

    /**
     * This method right now displays the widget and starts a Service to fetch
     * remote data from Server
     */
    private fun startWidget() {

        // this intent is essential to show the widget
        // if this intent is not included,you can't show
        // widget on homescreen
        val intent = Intent()
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(Activity.RESULT_OK, intent)

        // start your service
        // to fetch data from web
        val serviceIntent = Intent(this, RemoteFetchService::class.java)
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        startService(serviceIntent)

        // finish this activity
        this.finish()

    }

}
