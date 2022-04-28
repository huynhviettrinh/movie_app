package com.example.appmovieandroid.util

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.example.appmovieandroid.R
import com.example.appmovieandroid.databinding.AlertDialogSuccessBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

interface Common {
    companion object{

        fun alertDialogSuccess (mess: String,context: Context) {
            val successDialog = LayoutInflater.from(context)
                .inflate(R.layout.alert_dialog_success, null, false)
            val bindingSuccessDialog = AlertDialogSuccessBinding.bind(successDialog)

            val dialog = MaterialAlertDialogBuilder(context).setView(successDialog)
                .setBackground(
                    ColorDrawable(0x00000000)
                ).create()

            bindingSuccessDialog.textMsg.text = mess

            bindingSuccessDialog.btnAction.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()

        }

    }
}