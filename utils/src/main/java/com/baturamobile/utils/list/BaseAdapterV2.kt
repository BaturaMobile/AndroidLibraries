package eus.realsociedad.realzale.library.realist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by vssnake on 08/05/2017.
 */

abstract class BaseAdapterV2 : RecyclerView.Adapter<BaseAdapterV2.BaseAdapterViewHolderV2>() {

    private var arrayItems: ArrayList<NoImageModelV2>? = null


    var multiSet = false

    protected var holderClick: HolderClick<NoImageModelV2>? = null

    init {
        arrayItems = ArrayList()
    }

    override fun onBindViewHolder(holder: BaseAdapterViewHolderV2, position: Int) {
        holder.bindView(arrayItems!![position], position)
    }


    fun addItems(arrayItems: ArrayList<NoImageModelV2>, multiSet: Boolean) {
        this.arrayItems = arrayItems
        notifyDataSetChanged()
        this.multiSet = multiSet
    }

    fun holderClick(holderClick: HolderClick<NoImageModelV2>) {
        this.holderClick = holderClick
    }


    override fun getItemCount(): Int {
        return arrayItems!!.size
    }

    abstract inner class BaseAdapterViewHolderV2(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var itemModelV2: NoImageModelV2? = null
            private set

        var holderPosition = 0

        var context: Context

        init {
            setupView(itemView)
            context = itemView.context
        }

        abstract fun setupView(view: View)

        internal fun bindView(modelV2: NoImageModelV2, position: Int) {
            this.itemModelV2 = modelV2
            this.holderPosition = position

            refreshView()


        }

        abstract fun refreshView()

        override fun onClick(v: View) {

            if (multiSet){
                itemModelV2?.let {
                    itemModelV2!!.selected = !itemModelV2!!.selected
                }
                notifyDataSetChanged()
            }else{
                arrayItems?.forEach {
                    item -> item.selected = false
                }
                itemModelV2?.let {
                    itemModelV2!!.selected = true
                }
            }
            if (holderClick != null) {
                holderClick?.onHolderClick(arrayItems, position)
            }


        }
    }

    interface HolderClick<T> {
        fun onHolderClick(items: ArrayList<T>?, position: Int)
    }
}
