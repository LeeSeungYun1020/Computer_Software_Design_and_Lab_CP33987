package android.leeseungyun.homework

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.cursoradapter.widget.SimpleCursorAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var cursor: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)
        db = dbHelper.readableDatabase
        initCursor()

        nameListView.adapter = SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_2,
            cursor,
            arrayOf(DBHelper.VALUE_CONTACTS_NAME, DBHelper.VALUE_CONTACTS_NUMBER),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )

        addFab.setOnClickListener {
            startActivityForResult(Intent(this, SubActivity::class.java), SubActivity.CODE_ACTIVITY)
        }

        nameSearchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
                true
            } else {
                false
            }
        }

        nameSearchButton.setOnClickListener {
            search()
        }
    }

    private fun search() {
        updateCursor(nameSearchEditText.text.toString().isNotEmpty())
//        if (nameSearchEditText.text.toString().isNotEmpty())
//            updateCursor(search = true)
//        else
//            updateCursor()
    }

    private fun initCursor() {
        cursor = db.query(
            DBHelper.TABLE_CONTACTS,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }

    private fun updateCursor(search: Boolean = false) {
        cursor.close()
        if (search) {
            cursor = db.query(
                DBHelper.TABLE_CONTACTS,
                null,
                "${DBHelper.VALUE_CONTACTS_NAME} = ?",
                arrayOf(nameSearchEditText.text.toString()),
                null,
                null,
                null,
                null
            )
        } else {
            initCursor()
        }
        (nameListView.adapter as SimpleCursorAdapter).changeCursor(cursor)
        nameListView.invalidate()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SubActivity.CODE_ACTIVITY && resultCode == SubActivity.CODE_SUCCESS) {
            updateCursor()
        }
    }

    override fun onDestroy() {
        cursor.close()
        db.close()
        dbHelper.close()
        super.onDestroy()
    }
}
