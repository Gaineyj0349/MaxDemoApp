package com.gainwise.maxdemo.Activity

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import com.gainwise.maxdemo.BEANS.CustomClickableSpan
import com.gainwise.maxdemo.BEANS.Spanner
import com.gainwise.maxdemo.R
import com.gainwise.maxdemo.SQLiteUtil.DBHelperShippedIn
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_show.*

class ShowActivity : AppCompatActivity() {

    lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        val name = intent.getStringExtra("name")

        val helper = DBHelperShippedIn(this)
         db = helper.readableDatabase
        val nameCursor = fetchCursor(name)


        val count = nameCursor.columnCount
        nameCursor.moveToFirst()
        for (i in 0 until count){
            show_tv.append(nameCursor.getColumnName(i))
            show_tv.append("\n")
            show_tv.append(nameCursor.getString(i))
            show_tv.append("\n\n")
        }

        try {
            Picasso.get().load(nameCursor.getString(nameCursor.getColumnIndex("_bio_image"))).into(show_iv);
        }catch (e: Exception){
            show_iv.visibility = View.GONE
        }

        val associationsList = buildList(nameCursor)



        val spanList = ArrayList<ClickableSpan>()
        for (i in associationsList.indices){
          spanList.add(CustomClickableSpan(associationsList.get(i), this))
        }
        makeLinks(
            show_tv,
            associationsList.toTypedArray()
        )
    }

    private fun fetchListOfIndexes(word: String): MutableList<Spanner> {
        val list = mutableListOf<Spanner>()
        val str = show_tv.text.toString().trim()
        var lastIndex = -2
        var previousIndex = 0


        while (lastIndex != -1 ) {
            Log.i("MAXdemo", "whileloop start")

            previousIndex = lastIndex
            lastIndex = str.indexOf(word, lastIndex)



            if (lastIndex != -1 && previousIndex != lastIndex ) {

                val span = Spanner(lastIndex,lastIndex + word.length )
                lastIndex += lastIndex + word.length
                list.add(span)
            }else{
                Log.i("MAXdemo", "whileloop break")
                break;
            }
        }
        return list
    }

    private fun buildList(nameCursor: Cursor): List<String> {
    val stringToBreak = nameCursor.getString(nameCursor.getColumnIndex("_associations_list"))
        return stringToBreak.split("|").map { it.trim() }
    }

    private fun fetchCursor(name: String?): Cursor {
        val c = db.rawQuery("SELECT _type FROM _table_associations WHERE _name LIKE '$name'", null)
        c.moveToFirst()
        val type = c.getString(0)

        val tableName =
        when (type){
            "person" -> "_table_person"
            "war" -> "_table_war"
            "conflict" -> "_table_conflict"
            else -> "error"
        }
        Log.i("MAXdemo", "SELECT * FROM $tableName WHERE _name LIKE '$name'")
        return db.rawQuery("SELECT * FROM $tableName WHERE _name LIKE '$name'",null)
    }

    fun makeLinks(textView: TextView, links: Array<String>) {
        val spannableString = SpannableString(textView.text)
        for (i in links.indices) {
            val link = links[i]

            val startIndexOfLink = textView.text.toString().indexOf(link)

            val indexList = fetchListOfIndexes(links[i])

            for (j in 0 until indexList.size) {
                val customSpanner = CustomClickableSpan(link, this@ShowActivity)
                spannableString.setSpan(
                    customSpanner, indexList.get(j).startIndex,
                    indexList.get(j).endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        textView.highlightColor = Color.TRANSPARENT // prevent TextView change background when highlight
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)
    }
}
