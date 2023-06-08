package com.example.mystockmarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
/**
 * LanguageAdapter is a RecyclerView adapter that is responsible for binding data to the views
 * in the RecyclerView. It is used to display a list of CoinData objects.
 *
 * @property mList The list of CoinData objects to be displayed in the RecyclerView.
 */
class LanguageAdapter(var mList: List<CoinData>) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    private lateinit var mListener: onItemClickListener
    /**
     * The interface that defines the click listener for the items in the RecyclerView.
     */
    interface onItemClickListener{
        /**
         * Callback method that is invoked when an item is clicked.
         *
         * @param position The position of the clicked item in the RecyclerView.
         */
        fun onItemClick(position: Int)
    }

    /**
     * Sets the click listener for the items in the RecyclerView.
     *
     * @param listener The click listener to be set.
     */
    fun setOnItemClickListener(listener: onItemClickListener)
    {
        mListener = listener
    }

    /**
     * ViewHolder class that holds references to the views in each item of the RecyclerView.
     *
     * @property itemView The root view of the item.
     * @property listener The click listener for the item.
     */
    inner class LanguageViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val logo : ImageView = itemView.findViewById(R.id.logoIv)
        val titleTv : TextView = itemView.findViewById(R.id.titleTv)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }
        }
    }
    /**
     * Sets the filtered list of CoinData objects to be displayed in the RecyclerView.
     *
     * @param mList The filtered list of CoinData objects.
     */
    fun setFilteredList(mList: List<CoinData>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item , parent , false)
        return LanguageViewHolder(view,mListener)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.logo.setImageResource(mList[position].logo)
        holder.titleTv.text = mList[position].title
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}