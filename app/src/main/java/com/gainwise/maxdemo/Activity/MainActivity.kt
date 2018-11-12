package com.gainwise.maxdemo

import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.DividerItemDecoration.VERTICAL
import android.support.v7.widget.LinearLayoutManager
import com.gainwise.maxdemo.Adapters.RVNameAdapter
import com.gainwise.maxdemo.SQLiteUtil.DBHelperShippedIn
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            != PackageManager.PERMISSION_GRANTED){
//            requestRuntimePermissions()
//        }
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//            != PackageManager.PERMISSION_GRANTED){
//            requestRuntimePermissions()
//        }
        val helper = DBHelperShippedIn(this)
        var db = helper.readableDatabase
        val nameCursor = db.rawQuery("SELECT _name FROM _table_associations",null)
        val nameList = getNameListWithCursor(nameCursor)
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        val itemDecor = DividerItemDecoration(this, VERTICAL)
        recyclerView.addItemDecoration(itemDecor)
        val adapter = RVNameAdapter(this, nameList)
        recyclerView.setAdapter(adapter)
    }

    fun requestRuntimePermissions(){
        val permissions = arrayOf("android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE")
        ActivityCompat.requestPermissions(this, permissions, 0)
    }

    private fun getNameListWithCursor(nameCursor: Cursor): MutableList<String>? {
        nameCursor.moveToFirst()
        val list = ArrayList<String>()
        do{
            list.add(nameCursor.getString(0))
        }while (nameCursor.moveToNext())

        return list
    }




}
