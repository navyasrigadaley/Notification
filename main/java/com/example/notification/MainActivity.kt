package com.example.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.NotificationManager
import android.app.NotificationChannel
import androidx.core.app.NotificationCompat
import android.content.Intent
import android.app.PendingIntent
//import android.R

import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.content.BroadcastReceiver
import android.content.Context

//import android.R

import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.Button


//import android.R


class MainActivity : AppCompatActivity() {

    private var mNotifyManager: NotificationManager? = null
    private val mReceiver: NotificationReceiver = NotificationReceiver()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        createNotificationChannel()

        registerReceiver(mReceiver,
            IntentFilter(ACTION_UPDATE_NOTIFICATION))


       val button_notify = findViewById<Button>(R.id.notify)
        button_notify.setOnClickListener(View.OnClickListener {
            sendNotification()
        })
       val button_update = findViewById<Button>(R.id.update) as Button
        button_update!!.setOnClickListener {
            updateNotification()
        }
       val button_cancel = findViewById<Button>(R.id.cancel) as Button
        button_cancel!!.setOnClickListener {
            cancelNotification()
        }

        setNotificationButtonState(true, false, false)
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }


    fun createNotificationChannel() {


        mNotifyManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_channel_description)
            mNotifyManager!!.createNotificationChannel(notificationChannel)
        }
    }


    fun sendNotification() {

        val updateIntent = Intent(ACTION_UPDATE_NOTIFICATION)
        val updatePendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT)

        val notifyBuilder = notificationBuilder

        notifyBuilder.addAction(R.drawable.ic_update,getString(R.string.update), updatePendingIntent)


        mNotifyManager!!.notify(NOTIFICATION_ID, notifyBuilder.build())


        setNotificationButtonState(false, true, true)
    }
    private val notificationBuilder: NotificationCompat.Builder
        private get() {
            val notificationIntent = Intent(this, MainActivity::class.java)
            val notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)


            return NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_android)
                .setAutoCancel(true).setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
        }

    fun updateNotification() {

        val androidImage = BitmapFactory.decodeResource(resources, R.drawable.mascot_1)


        val notifyBuilder = notificationBuilder

        notifyBuilder.setStyle(NotificationCompat.BigPictureStyle()
            .bigPicture(androidImage)
            .setBigContentTitle(getString(R.string.notification_updated)))


        mNotifyManager!!.notify(NOTIFICATION_ID, notifyBuilder.build())


        setNotificationButtonState(false, false, true)
    }


    fun cancelNotification() {
        mNotifyManager!!.cancel(NOTIFICATION_ID)


        setNotificationButtonState(true, false, false)
    }


    fun setNotificationButtonState(
        isNotifyEnabled: Boolean?,
        isUpdateEnabled: Boolean?,
        isCancelEnabled: Boolean?, )
    {
        val button_notify = findViewById<Button>(R.id.notify)
        val button_update = findViewById<Button>(R.id.update)
        val button_cancel = findViewById<Button>(R.id.cancel)
        button_notify!!.isEnabled = isNotifyEnabled!!
        button_update!!.isEnabled = isUpdateEnabled!!
        button_cancel!!.isEnabled = isCancelEnabled!!
    }


    inner class NotificationReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {

            updateNotification()
        }
    }

    companion object {
        private const val ACTION_UPDATE_NOTIFICATION = "com.android.example.notify.ACTION_UPDATE_NOTIFICATION"

        private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"


        private const val NOTIFICATION_ID = 0
    }
}