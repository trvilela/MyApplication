package com.example.thiagovilela.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    private val mAuth = FirebaseAuth.getInstance()

    lateinit var googleApiClient: GoogleApiClient

    private val SIGN_IN_CODE = 777

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signinButton.setOnClickListener(this)
        esqueciSenhaButton.setOnClickListener(this)
        buttonPrimeiroAcesso.setOnClickListener(this)
        signinButtonGoogle.setOnClickListener(this)

        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

    }

    override fun onClick(view: View) {
        val id = view.id

        if (id == R.id.signinButton)
            signin()
        else if (id == R.id.buttonPrimeiroAcesso) {
            criarLogin()
        } else if (id == R.id.signinButtonGoogle)
            signinGoogle()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    private fun criarLogin() {
        val intent = Intent(this, CriarLoginActivity::class.java)
        startActivity(intent)
    }

    private fun signinGoogle() {

        val intent = Intent(Auth.GoogleSignInApi.getSignInIntent(googleApiClient))
        startActivityForResult(intent, SIGN_IN_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_CODE) {
            val result: GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)

        } else {
            Toast.makeText(this, "Usuário não logado", Toast.LENGTH_LONG).show()
        }

    }
}





