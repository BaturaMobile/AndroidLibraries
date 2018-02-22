package eus.realsociedad.realzale.library.realist

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import eus.realsociedad.realzale.R

/**
 * Created by vssnake on 03/07/2017.
 */

class ListSelectableFragmentV2 : BaseV2RecycleViewFragment() {
    override fun getEmptyDrawable(): Drawable? {
        return ContextCompat.getDrawable(context!!,R.drawable.ic_error)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = RealAdapter()
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {


        val KEY_RESULT = "keyResult"

        val KEY_MODEL_LIST = "modelV2List"
        val KEY_MODEL_SELECTED = "modelSelected"

        val KEY_TYPE_LIST = "keyTypeList"
        val KEY_MULTISET = "keyMultiset"

        val NO_IMAGE_ADAPTER = 0
        val IMAGE_ADAPTER = 1

        fun instance(
                modelV2List: java.util.ArrayList<NoImageModelV2>,
                multiSet: Boolean, typeList: Int): ListSelectableFragmentV2 {

            val selectableFragment = ListSelectableFragmentV2()

            val bundle = Bundle()

            bundle.putSerializable(KEY_MODEL_LIST, modelV2List)
            bundle.putBoolean(KEY_MULTISET,multiSet)
            bundle.putInt(KEY_TYPE_LIST, typeList)

            selectableFragment.arguments = bundle

            return selectableFragment


        }
    }

}
