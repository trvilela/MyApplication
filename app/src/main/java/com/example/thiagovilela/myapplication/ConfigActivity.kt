package com.example.thiagovilela.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.OptionalPendingResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_config.*


class ConfigActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {


    lateinit var mDatabase: DatabaseReference

    lateinit var googleApiClient: GoogleApiClient

    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        buttonQrCode.setOnClickListener(this)

        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios")

        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {

                val result = snapshot.child(mAuth.currentUser!!.uid).child("nome").value
                nomeBaseTextView.text = result.toString()
            }


        })
    }

    override fun onClick(view: View) {

        val id = view.id

        if (id == R.id.buttonQrCode) {
          //  val intent = Intent(this, ReaderQrCodeActivity::class.java)
          //startActivity(intent)
        }
    }


    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStart() {
        super.onStart()

        val opr: OptionalPendingResult<GoogleSignInResult> = Auth.GoogleSignInApi.silentSignIn(googleApiClient)

        if (opr.isDone) {
            val result: GoogleSignInResult = opr.get()
            handleSignInResult(result)
        } else
            opr.setResultCallback {
                fun onResult(googleSignInResult: GoogleSignInResult) {
                    handleSignInResult(googleSignInResult)
                }
            }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            val account = result.signInAccount

            nomeBaseTextView.text = account!!.givenName
        }
    }


}