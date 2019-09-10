package com.app.baljeet.iconfinderapp.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.app.baljeet.iconfinderapp.R
import com.app.baljeet.iconfinderapp.callbacks.OnDialogRetryClickListener

object DialogHelper {
    fun customErrorDialogBuilder(context: Context,
                                errorMessage: String, offset: Int, onDialogRetryClickListener: OnDialogRetryClickListener) {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setTitle(context.getString(R.string.error))
            .setMessage(errorMessage)
            .setCancelable(false)
            .setPositiveButton("Retry") { p0, p1 ->
                onDialogRetryClickListener.onRetry(offset)
                p0.dismiss() }
            .setNegativeButton("Cancel") { p0, p1 ->
                p0.dismiss() }

        dialogBuilder.show()
    }
}