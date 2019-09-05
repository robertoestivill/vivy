package com.robertoestivill.vivy.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.onTextChanged(callback: (String) -> Unit): TextWatcher {

  val textWatcher = object : TextWatcher {
    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
      callback.invoke(p0.toString())
    }
  }
  addTextChangedListener(textWatcher)
  return textWatcher
}
