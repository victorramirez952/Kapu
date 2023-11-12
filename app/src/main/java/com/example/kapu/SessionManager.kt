package com.example.kapu

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context?) {
    val PRIVATE_MODE = 0
    val PREF_NAME = "SharedPreferences"
    val IS_LOGIN = "is_login"

    var pref: SharedPreferences? = context?.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var editor: SharedPreferences.Editor? = pref?.edit()

    fun setLogin(isLogin: Boolean){
        editor?.putBoolean(IS_LOGIN, isLogin)
        editor?.commit()
    }

    fun setUserEmail(email: String){
        editor?.putString("email", email)
        editor?.commit()
    }

    fun isLogin(): Boolean?{
        return pref?.getBoolean(IS_LOGIN, false)
    }

    fun getUserEmail(): String? {
        return pref?.getString("email", "")
    }

    fun removeData(){
        editor?.clear()
        editor?.commit()
    }
}