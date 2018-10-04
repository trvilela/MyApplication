package com.example.thiagovilela.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.OptionalPendingResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode.FORMAT_QR_CODE
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_vincular_quadro.*


class VincularQuadroActivity(private val CAMERA_REQUEST: Int = 1888, private val MY_CAMERA_PERMISSION_CODE: Int = 100) : AppCompatActivity(), View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    val options: FirebaseVisionBarcodeDetectorOptions = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(FORMAT_QR_CODE)
            .build()

    val detector: FirebaseVisionBarcodeDetector = FirebaseVision.getInstance()
            .getVisionBarcodeDetector(options)

    lateinit var mDatabase: DatabaseReference

    lateinit var imageView: ImageView

    lateinit var googleApiClient: GoogleApiClient

    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vincular_quadro)

        this.imageView = findViewById(R.id.imageView)

        buttonCapturarImagem.setOnClickListener(this)

        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        mDatabase = FirebaseDatabase.getInstance().getReference("qrcode")


    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClick(view: View) {

        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent){

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            val photo: Bitmap = data.extras.get("data") as Bitmap

            val image: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(photo)

            val result = detector.detectInImage(image)
                    .addOnSuccessListener {
                        for (barcode in it) {
                            val rawValue = barcode.rawValue
                            var int = 0
                            mDatabase.child(mAuth.currentUser!!.uid).child("numero_quadro").push().child("qrcode").setValue(rawValue)

                            val intent = Intent(this, ConfiguracoesActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this,"Qr não capturado. Tente novamente", Toast.LENGTH_SHORT).show()

                    }

        }

    }
}
