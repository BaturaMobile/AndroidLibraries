package eus.realsociedad.realzale.library.realist

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.baturamobile.mvp.BaseRecycleViewView

import eus.realsociedad.realzale.R
import kotlinx.android.synthetic.main.real_fragment_rv.*


abstract class BaseV2RecycleViewFragment : Fragment(), BaseRecycleViewView<NoImageModelV2>, BaseAdapterV2.HolderClick<NoImageModelV2> {


    var adapter: BaseAdapterV2? = null


    var multiSet: Boolean = false

    abstract fun getEmptyDrawable() : Drawable?

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_rv, container, false)


        frv_empty_image.setImageDrawable (getEmptyDrawable())
        return view

    }

    lateinit var modelV2List: ArrayList<NoImageModelV2>
    internal var typeList: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        frv_rv.layoutManager = LinearLayoutManager(context)

        adapter?.holderClick(this)
        frv_rv.adapter = adapter

        modelV2List = arguments!!.getSerializable(ListSelectableFragmentV2.KEY_MODEL_LIST) as ArrayList<NoImageModelV2>
        typeList = arguments!!.getInt(ListSelectableFragmentV2.KEY_TYPE_LIST, ListSelectableFragmentV2.NO_IMAGE_ADAPTER)
        multiSet = arguments!!.getBoolean(ListSelectableFragmentV2.KEY_MULTISET)

        showCards(modelV2List)
        adapter!!.addItems(modelV2List,multiSet)
    }

    override fun onHolderClick(items: ArrayList<NoImageModelV2>?, position: Int) {
        val intent = Intent()
        intent.putExtra(ListSelectableFragmentV2.KEY_MODEL_SELECTED, items)
        activity!!.setResult(Activity.RESULT_OK, intent)
        if (!multiSet){
            activity!!.finish()
        }


    }
    @CallSuper
    override fun showCards(receiptModelV2List: MutableList<NoImageModelV2>?) {

        frv_rv.visibility = View.VISIBLE
        frv_empty_image.visibility = View.GONE
        frv_empty_text.visibility = View.GONE
        adapter?.addItems(receiptModelV2List as ArrayList<NoImageModelV2>, multiSet)
    }




    @CallSuper
    override fun showEmpty() {
        frv_rv.visibility = View.GONE
    }
}
