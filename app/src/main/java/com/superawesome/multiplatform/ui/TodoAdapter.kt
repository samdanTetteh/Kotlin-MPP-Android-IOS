package com.superawesome.multiplatform.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.superawesome.multiplatform.R
import com.superawesome.sharedcode.model.Todo


/**
 * Recycler view adapter to load data from ViewModel
 * */
class TodoAdapter(var data : List<Todo>) : RecyclerView.Adapter<TodoAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_checked, parent, false)
        return  ViewHolder(view);
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem  = data.get(position)
        holder.bind(dataItem)
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val checkedView = itemView.findViewById<CheckedTextView>(android.R.id.text1)
        fun bind(dataItem : Todo){
            checkedView.text = dataItem.title
            checkedView.isChecked = dataItem.completed
        }

    }


}