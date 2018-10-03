package com.example.thiagovilela.myapplication

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_criar_login.*

class CriarLoginActivity : AppCompatActivity(), View.OnClickListener {

    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_login)

        aroz.setOnClickListener(this)

        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios")

    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.aroz)
            signup()
    }

    private fun signup() {

        val email = editText2.text.toString()
        val senha = editText3.text.toString()
        val nome = nomeEditText.text.toString()

        if (!email.isEmpty() && !senha.isEmpty() && !nome.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = mAuth.currentUser
                    val uid = user!!.uid
                    mDatabase.child(uid).child("nome").setValue(nome)

                    Toast.makeText(this, getString(R.string.usuario_criado), Toast.LENGTH_LONG).show()
                    val intent = Intent(this, ConfigActivity::class.java)
                    startActivity(intent)
                } else
                    Toast.makeText(this, getString(R.string.email_senha_invalidos), Toast.LENGTH_LONG).show()
            }

        } else
            Toast.makeText(this, getString(R.string.email_senha_nao_preenchidos), Toast.LENGTH_LONG).show()
    }
}