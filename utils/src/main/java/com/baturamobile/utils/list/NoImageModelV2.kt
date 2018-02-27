package eus.realsociedad.realzale.library.realist

import java.io.Serializable

/**
 * Created by vssnake on 19/01/2018.
 */

abstract class NoImageModelV2(var selected: Boolean) : Serializable {

    abstract fun getText(): String

    abstract fun getImageUri(): String

    override fun equals(obj: Any?): Boolean {
        return (obj as? NoImageModelV2)?.getText().equals(getText(), ignoreCase = true)
    }


}
