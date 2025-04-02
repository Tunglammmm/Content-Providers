package com.example.contentproviders

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var listViewContacts: ListView
    private val contactsList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ ListView
        listViewContacts = findViewById(R.id.listViewContacts)

        // Kiểm tra quyền truy cập danh bạ
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 1)
        } else {
            loadContacts()
        }
    }

    // Lấy danh bạ và cập nhật ListView
    private fun loadContacts() {
        val contacts = getContacts()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contacts)
        listViewContacts.adapter = adapter
    }

    // Hàm lấy danh bạ
    private fun getContacts(): List<String> {
        val contactsList = mutableListOf<String>()
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null, null, null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(0)  // Lấy tên
                val phone = it.getString(1) // Lấy số điện thoại
                contactsList.add("$name - $phone")
            }
        }
        return contactsList
    }
}


