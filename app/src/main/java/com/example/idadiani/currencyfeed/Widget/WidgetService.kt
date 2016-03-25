package com.example.idadiani.currencyfeed.Widget

import android.content.Intent
import android.widget.RemoteViewsService

/**
 * Created by i.dadiani on 2/26/2016.
 */

class WidgetService() : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory? {
        return ListViewProvider(this.applicationContext, intent)
    }

}