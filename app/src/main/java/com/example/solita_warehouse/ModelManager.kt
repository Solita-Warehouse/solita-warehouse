package com.example.solita_warehouse

import android.content.Context
import com.example.solita_warehouse.ml.SsdMobilenetV11Metadata1

object ModelManager {
    private var model: SsdMobilenetV11Metadata1? = null
    private var item: String = ""
    fun getModel(context: Context): SsdMobilenetV11Metadata1 {
        if (model == null) {
            model = SsdMobilenetV11Metadata1.newInstance(context)
        }
        return model!!
    }

    fun setItem(item: String){
        this.item = item
    }
    fun getItem(): String{
        return item
    }
    fun closeModel() {
        model?.close()
        model = null
    }
}