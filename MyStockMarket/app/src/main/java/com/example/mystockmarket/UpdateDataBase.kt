package com.example.mystockmarket

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


private lateinit var dbref: DatabaseReference
private lateinit var firebaseAuth: FirebaseAuth

fun updateEmail(usid: String,newMail: String)
{
    if(!checkEmpty(newMail)) {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.currentUser!!.updateEmail(newMail)

        dbref = FirebaseDatabase.getInstance().getReference()
        val updates: MutableMap<String, Any> = HashMap()
        updates["email"] = newMail
        dbref.child(usid).updateChildren(updates)
    }
}
fun updateName(usid: String,newName: String)
{
    if(!checkEmpty(newName)) {
        dbref = FirebaseDatabase.getInstance().getReference()
        val updates: MutableMap<String, Any> = HashMap()
        updates["name"] = newName
        dbref.child(usid).updateChildren(updates)
    }
}
fun updatePass(usid: String,newPass: String)
{
    if(!checkEmpty(newPass)) {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.currentUser!!.updatePassword(newPass)
        dbref = FirebaseDatabase.getInstance().getReference()
        val updates: MutableMap<String, Any> = HashMap()
        updates["pass"] = newPass
        dbref.child(usid).updateChildren(updates)
    }
}
fun updatePhone(usid: String,newPhone: String)
{
    if(!checkEmpty(newPhone))
    {
        dbref = FirebaseDatabase.getInstance().getReference()
        val updates: MutableMap<String, Any> = HashMap()
        updates["phone"] = newPhone
        dbref.child(usid).updateChildren(updates)
    }
}
fun updateCoins(usid: String,newCoin: String)
{
    if(!checkEmpty(newCoin))
    {
        dbref = FirebaseDatabase.getInstance().getReference()
        dbref.child(usid).get().addOnSuccessListener {
            var coins = it.child("coins").value.toString()
            if (coins != "") {
                coins += "," + newCoin
                val updates: MutableMap<String, Any> = HashMap()
                updates["coins"] = coins
                dbref.child(usid).updateChildren(updates)
            }
            else
            {
                val updates: MutableMap<String, Any> = HashMap()
                updates["coins"] = newCoin
                dbref.child(usid).updateChildren(updates)
            }
        }
    }
}
fun checkEmpty(field: String): Boolean {
    return field.isNullOrEmpty()
}
