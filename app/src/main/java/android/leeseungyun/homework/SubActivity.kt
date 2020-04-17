package android.leeseungyun.homework

import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
import kotlinx.android.synthetic.main.activity_sub.*

class SubActivity : AppCompatActivity() {
    companion object {
        const val CODE_ACTIVITY = 2001
        const val CODE_SUCCESS = 2002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        inputPhoneEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                save()
                true
            } else {
                false
            }
        }

        inputSave.setOnClickListener {
            save()
        }
    }

    private fun save() {
        val name = inputNameEditText.text.toString()
        val number = inputPhoneEditText.text.toString()
        if (name.isNotBlank() && number.isNotBlank()) {
            val dbHelper = DBHelper(this)
            val db = dbHelper.writableDatabase

            val values = contentValuesOf(
                DBHelper.VALUE_CONTACTS_NAME to name,
                DBHelper.VALUE_CONTACTS_NUMBER to number
            )
            db.insert(DBHelper.TABLE_CONTACTS, null, values)

            db.close()
            dbHelper.close()

            setResult(CODE_SUCCESS)
            finish()
        } else {
            Toast.makeText(this, R.string.err_save, Toast.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
