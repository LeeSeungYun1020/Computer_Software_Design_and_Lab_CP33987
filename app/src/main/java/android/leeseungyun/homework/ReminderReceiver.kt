package android.leeseungyun.homework

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.widget.Toast
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = context.getString(R.string.reminder)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = context.getString(R.string.reminder)
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE_REMINDER,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, channelId)
        builder.apply {
            setSmallIcon(R.drawable.ic_assignment_ind_black_24dp)
            setWhen(System.currentTimeMillis())
            setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_stat_notify))
            setContentTitle(message)
            setAutoCancel(true)
            setContentIntent(pendingIntent)
        }
        manager.notify(NOTIFICATION_ID_REMINDER, builder.build())

        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
//        Handler().postDelayed({
//            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
//        }, 1000)
    }
}
