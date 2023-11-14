package com.example.solita_warehouse

import android.content.Context
import com.example.solita_warehouse.ml.SsdMobilenetV11Metadata1

object ModelManager {
    private var model: SsdMobilenetV11Metadata1? = null

    fun getModel(context: Context): SsdMobilenetV11Metadata1 {
        if (model == null) {
            model = SsdMobilenetV11Metadata1.newInstance(context)
        }
        return model!!
    }

    fun closeModel() {
        model?.close()
        model = null
    }
}