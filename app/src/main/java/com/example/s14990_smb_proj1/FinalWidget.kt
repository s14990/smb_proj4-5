package com.example.s14990_smb_proj1

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast

/**
 * Implementation of App Widget functionality.
 */

val NAME = "com.example.s14990_smb_proj1"

var current_img=R.drawable.p1;
val img1 = R.drawable.p1;
val img2 = R.drawable.p2;
class FinalWidget : AppWidgetProvider() {
    override fun onUpdate(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?){

        if(intent?.action == context?.getString(R.string.action1)){
            Toast.makeText(context, "ChangeImage", Toast.LENGTH_SHORT).show()
            if(current_img == img1){
                current_img=img2;
            }
            else{
                current_img=img1;
            }
            val views = RemoteViews(context?.packageName, R.layout.final_widget)
            views.setImageViewResource(R.id.imageView,R.drawable.p2)

            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisAppWidgetComponentName = ComponentName(context!!.packageName, javaClass.name)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName)

            /*
            for(widget_id in appWidgetIds){
                appWidgetManager.updateAppWidget(appWidgetIds.last(), views)
            }
            */

            onUpdate(context,appWidgetManager,appWidgetIds)

        }
        super.onReceive(context, intent)

    }
}

internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.final_widget)

    val intentW = Intent(Intent.ACTION_VIEW)
    intentW.data = Uri.parse("https://www.google.com")

    val pendingIW = PendingIntent.getActivity(
            context,
            0,
            intentW,
            PendingIntent.FLAG_UPDATE_CURRENT
    )

    views.setOnClickPendingIntent(R.id.link_button, pendingIW)

    val intentA1 = Intent(context.getString(R.string.action1))
    intentA1.component = ComponentName(context, FinalWidget::class.java)

    val pendingA1 = PendingIntent.getBroadcast(
            context,
            0,
            intentA1,
            PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.action_1, pendingA1)

    views.setImageViewResource(R.id.imageView, current_img)
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}