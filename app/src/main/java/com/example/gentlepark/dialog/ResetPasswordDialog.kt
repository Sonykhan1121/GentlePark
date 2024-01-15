package com.example.gentlepark.dialog

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.gentlepark.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setupBottomSheetDialog(
    onSendClick : (String ) -> Unit,

){
    val dialog  = BottomSheetDialog(requireContext())
    val view = layoutInflater.inflate(R.layout.reset_password_dialog,null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()
    val edEmail = view.findViewById<EditText>(R.id.email_dialog_id)
    val buttonSend = view.findViewById<Button>(R.id.send)
    val buttonCancel = view.findViewById<Button>(R.id.cancel)
    buttonSend.setOnClickListener {
        val email = edEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }
    buttonCancel.setOnClickListener {
        dialog.dismiss()
    }

}