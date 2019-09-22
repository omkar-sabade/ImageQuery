package com.omkar.imagequery.utils

import android.os.AsyncTask

class DoAsync(val function: () -> Unit) : AsyncTask<Any, Unit, Any?>() {
    init {
        execute()
    }

    override fun doInBackground(vararg params: Any?) {
        function()
    }

}