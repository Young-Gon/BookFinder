package com.gondev.bookfinder.ui

import android.view.inputmethod.InputMethodManager

class KeyboardVisibilityDelegation(
    private val inputMethodManager: InputMethodManager
){
    fun showKeyboard() {
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    fun hideKeyboard() {
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

}