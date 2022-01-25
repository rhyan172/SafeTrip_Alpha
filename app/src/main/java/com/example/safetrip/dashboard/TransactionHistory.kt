package com.example.safetrip

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.example.safetrip.dashboard.TransactHistoryAdapter
import com.example.safetrip.database_adapter.TransactHistory
import com.google.firebase.database.*


class TransactionHistory : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var tRecyclerView: RecyclerView
    private lateinit var transactArrayList: ArrayList<TransactHistory>
    private lateinit var preferences: SharedPreferences

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        tRecyclerView = findViewById(R.id.routeList)
        tRecyclerView.layoutManager = LinearLayoutManager(this)
        tRecyclerView.setHasFixedSize(true)

        transactArrayList = arrayListOf<TransactHistory>()
        getData()
    }

    private fun getData() {
        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val pnc = preferences.getString("PHONE_NUMBER", "PHONE-NUMBER").toString()
        database = FirebaseDatabase.getInstance().getReference("Names/$pnc/TransactionHistory")
        database.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    for (transactSnapshot in snapshot.children)
                    {
                        val transact = transactSnapshot.getValue(TransactHistory::class.java)
                        transactArrayList.add(transact!!)
                    }
                    tRecyclerView.adapter = TransactHistoryAdapter(transactArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}