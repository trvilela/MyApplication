package com.example.thiagovilela.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_config.*


class ConfigActivity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference

    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios")

        mDatabase.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {

                val result = snapshot.child(mAuth.currentUser!!.uid).child("nome").value
                nomeBaseTextView.text = "Bem vindo, "+ result.toString()
                }


        })
    }


}
