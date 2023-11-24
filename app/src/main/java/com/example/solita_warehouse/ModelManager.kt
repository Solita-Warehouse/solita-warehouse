package com.example.solita_warehouse

import android.content.Context
import com.example.solita_warehouse.ml.SsdMobilenetV11Metadata1
import model.RentedItem

object ModelManager {
    private var model: SsdMobilenetV11Metadata1? = null
    private lateinit var item: RentedItem
    fun getModel(context: Context): SsdMobilenetV11Metadata1 {
        if (model == null) {
            model = SsdMobilenetV11Metadata1.newInstance(context)
        }
        return model!!
    }

    fun setItem(item: RentedItem){
        this.item = item
    }
    fun getItem(): RentedItem{
        return item
    }
    fun closeModel() {
        model?.close()
        model = null
    }
}