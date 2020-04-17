package android.leeseungyun.homework

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private const val DB_VERSION = 2
        private const val DB_NAME = "contactDB"
        const val TABLE_CONTACTS = "contacts"
        const val VALUE_CONTACTS_NAME = "name"
        const val VALUE_CONTACTS_NUMBER = "number"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            create table $TABLE_CONTACTS (
                _id integer primary key autoincrement,
                $VALUE_CONTACTS_NAME,
                $VALUE_CONTACTS_NUMBER
            )
        """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 테스트 편의상 DB 버전 업데이트로 DB 초기화
        if (newVersion == DB_VERSION) {
            db.execSQL("drop table $TABLE_CONTACTS")
            onCreate(db)
        }
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table $TABLE_CONTACTS")
        onCreate(db)
    }

}