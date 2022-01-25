package com.example.safetrip.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safetrip.R
import com.example.safetrip.database_adapter.TransactHistory

class TransactHistoryAdapter(private val routeList : ArrayList<TransactHistory>): RecyclerView.Adapter<TransactHistoryAdapter.TransactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.route_item, parent, false)
        return TransactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactViewHolder, position: Int) {
        val currentitem = routeList[position]
        holder.drivername.text = currentitem.driverName
        holder.dateAndTime.text = currentitem.dateAndTime
        holder.totalfare.text = currentitem.totalFare
    }

    override fun getItemCount(): Int {
        return routeList.size
    }

    class TransactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val drivername : TextView = itemView.findViewById(R.id.rhDriverName)
        val dateAndTime : TextView = itemView.findViewById(R.id.rhTimePayed)
        val totalfare : TextView = itemView.findViewById(R.id.rhTotalFare)
    }
}