package android.leeseungyun.homework

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saveButton.setOnClickListener {
            if (checkValues())
                saveReminder()
        }
    }

    // 메시지 및 날짜 값 확인
    private fun checkValues(): Boolean {
        if (messageEditText.text.isNullOrEmpty()) {
            AlertDialog.Builder(this).apply {
                setTitle(R.string.err_title_message)
                setMessage(R.string.err_info_message)
                setPositiveButton(R.string.err_ok, null)
                create()
                show()
            }
            return false
        }

        try {
            checkTime(
                year = yearEditText.text.toString().toIntOrNull()
                    ?: throw TimeValueException(getString(R.string.year)),
                month = monthEditText.text.toString().toIntOrNull()
                    ?: throw TimeValueException(getString(R.string.month)),
                day = dayEditText.text.toString().toIntOrNull()
                    ?: throw TimeValueException(getString(R.string.day)),
                hour = hourEditText.text.toString().toIntOrNull()
                    ?: throw TimeValueException(getString(R.string.hour)),
                minute = minuteEditText.text.toString().toIntOrNull()
                    ?: throw TimeValueException(getString(R.string.minute))
            )
        } catch (e: TimeValueException) {
            val msg = "${getString(R.string.err_info_time)} (${e.message})"
            AlertDialog.Builder(this).apply {
                setTitle(R.string.err_title_time)
                setMessage(msg)
                setPositiveButton(R.string.err_ok, null)
                create()
                show()
            }
            return false
        }
        return true
    }

    // 유효 날짜 검증 및 갱신
    private fun checkTime(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        val c = Calendar.getInstance()
        var checker = false

        val nowYear = c[Calendar.YEAR]
        c[Calendar.YEAR] = if (nowYear % 100 <= year && year < 100)
            year + (nowYear / 100 * 100)
        else if (nowYear <= year)
            year
        else
            throw TimeValueException(getString(R.string.year)) // year error
        if (c[Calendar.YEAR] == nowYear)
            checker = true

        val nowMonth = c[Calendar.MONTH] + 1
        if (month in 1..12)
            if (checker)
                when {
                    nowMonth < month -> {
                        c[Calendar.MONTH] = month - 1
                        checker = false
                    }
                    nowMonth == month -> c[Calendar.MONTH] = month - 1
                    else -> throw TimeValueException(getString(R.string.month)) // month error
                }
            else
                c[Calendar.MONTH] = month - 1
        else throw TimeValueException(getString(R.string.month)) // month error

        if (day in 1..31)
            if (checker)
                when {
                    c[Calendar.DAY_OF_MONTH] < day -> {
                        c[Calendar.DAY_OF_MONTH] = day
                        checker = false
                    }
                    c[Calendar.DAY_OF_MONTH] == day -> c[Calendar.DAY_OF_MONTH] = day
                    else -> throw TimeValueException(getString(R.string.day)) // day error
                }
            else
                c[Calendar.DAY_OF_MONTH] = day
        else throw TimeValueException(getString(R.string.day)) // day error
        if (c[Calendar.MONTH] != (month - 1)) // 28, 29, 30일인 월 확인
            throw TimeValueException(getString(R.string.day)) // day error

        if (hour in 0..23)
            if (checker)
                when {
                    c[Calendar.HOUR_OF_DAY] < hour -> {
                        c[Calendar.HOUR_OF_DAY] = hour
                        checker = false
                    }
                    c[Calendar.HOUR_OF_DAY] == hour -> c[Calendar.HOUR_OF_DAY] = hour
                    else -> throw TimeValueException(getString(R.string.hour)) // hour error
                }
            else
                c[Calendar.HOUR_OF_DAY] = hour
        else throw TimeValueException(getString(R.string.hour)) // hour error

        if (minute in 0..59)
            if (checker && c[Calendar.MINUTE] > minute)
                throw TimeValueException(getString(R.string.minute)) // minute error
            else
                c[Calendar.MINUTE] = minute
        else throw TimeValueException(getString(R.string.minute)) // minute error

        c[Calendar.SECOND] = 0
        calendar = c
    }

    private fun saveReminder() {
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, messageEditText.text.toString())
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            REQUEST_CODE_REMINDER,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        manager.set(AlarmManager.RTC, calendar.timeInMillis, pendingIntent)
    }
}
