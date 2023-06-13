package com.example.mystockmarket

import android.widget.Toast

fun ValidateFields(email : String,pass : String,phone : String,conpass : String,name : String) : Boolean
{
    return email.isNotEmpty() && pass.isNotEmpty() && conpass.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty()
}
fun MatchPass(pass : String,conpass : String):Boolean
{
    return pass == conpass
}
fun checkEmail(email : String): Boolean
{
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return false
    }
    return true
}