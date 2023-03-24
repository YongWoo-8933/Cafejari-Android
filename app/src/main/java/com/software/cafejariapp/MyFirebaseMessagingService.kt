package com.software.cafejariapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.app.NotificationCompat
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
class MyFirebaseMessagingService : FirebaseMessagingService() {
    //    fun onTokenRefresh() {
    //        // Get updated InstanceID token.
    //        val refreshedToken: String = FirebaseInstanceId.getInstance().getToken()
    //        Log.d(TAG, "Refreshed token: $refreshedToken")
    //
    //        // If you want to send messages to this application instance or
    //        // manage this apps subscriptions on the server side, send the
    //        // Instance ID token to your app server.
    //        sendRegistrationToServer(refreshedToken)
    //    }
    //
    //    override fun onNewToken(token: String) {
    //        super.onNewToken(token)
    //    }

    @SuppressLint("ServiceCast")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isNotEmpty()) { // 백그라운드에서 수신
            sendNotification(
                remoteMessage.data["title"].toString(), remoteMessage.data["body"].toString()
            )
        } else { // 포그라운드에서 수신
            sendNotification(
                remoteMessage.notification?.title, remoteMessage.notification?.body ?: ""
            )
        }
    }

    private fun sendRegistrationToServer(refreshedToken: String) {}

    // 받은 알림을 기기에 표시하는 메서드
    private fun sendNotification(title: String?, body: String) {
        val fullscreenIntent = Intent(this, MainActivity::class.java)
        fullscreenIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        fullscreenIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val fullscreenPendingIntent = PendingIntent.getActivity(
            this, 0, fullscreenIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = resources.getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo_light)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
            .setSound(defaultSoundUri)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(fullscreenPendingIntent)
            .setFullScreenIntent(fullscreenPendingIntent, true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                channelId, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(
            (System.currentTimeMillis() / 7).toInt(),
            notificationBuilder.build()
        )
    }
}