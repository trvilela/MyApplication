package com.example.thiagovilela.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener,  {

    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        signinButton.setOnClickListener(this)
        esqueciSenhaButton.setOnClickListener(this)
        buttonPrimeiroAcesso.setOnClickListener(this)
        signinButtonGoogle.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val id = view.id

        if (id == R.id.signinButton)
            signin()
        else if (id == R.id.buttonLogin) {
            criarLogin()
        }
        else if (id == R.id.signinButtonGoogle)
            signinGoogle()
    }

    private fun signin() {

        val email = emailEditText.text.toString()
        val senha = senhaEditText.text.toString()

        if (!email.isEmpty() && !senha.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, getString(R.string.login_com_sucesso), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ConfigActivity::class.java)
                    startActivity(intent)
                    finish()
                } else
                    Toast.makeText(this, getString(R.string.email_senha_invalidos), Toast.LENGTH_LONG).show()
            }

        } else
            Toast.makeText(this, getString(R.string.email_senha_nao_preenchidos), Toast.LENGTH_LONG).show()
    }

    private fun criarLogin()
    {
        val intent = Intent(this, CriarLoginActivity::class.java)
        startActivity(intent)
    }

    private fun signinGoogle(){

        cameraView.captureImage{cameraKitImage -> getQ

        }

    }

}


