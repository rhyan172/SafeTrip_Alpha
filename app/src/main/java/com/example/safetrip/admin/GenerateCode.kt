package com.example.safetrip

import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.collection.LLRBNode
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeReader
import com.google.zxing.qrcode.QRCodeWriter

class GenerateCode : AppCompatActivity() {
    private lateinit var qrCode: ImageView
    private lateinit var driverQrCode: EditText
    private lateinit var generateQrBtn: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_code)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        qrCode = findViewById(R.id.qrCode)
        driverQrCode = findViewById(R.id.driverQrCode)
        generateQrBtn = findViewById(R.id.generateQrBtn)

        database = FirebaseDatabase.getInstance().getReference("Driver")

        generateQrBtn.setOnClickListener{
            val checkDNum = driverQrCode.text.toString().trim()
            val cdn = "+63$checkDNum"
            database.child(cdn).get().addOnSuccessListener {
                if(it.exists())
                {
                    val data = driverQrCode.text.toString().trim()

                        val writer = QRCodeWriter()
                        try{
                            val bitMatrix = writer.encode("+63$data", BarcodeFormat.QR_CODE, 512, 512)
                            val width = bitMatrix.width
                            val height = bitMatrix.height
                            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

                            for(x in 0 until width){
                                for(y in 0 until height){
                                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                                }
                            }
                            qrCode.setImageBitmap(bmp)
                            Toast.makeText(this, "QR Code generated for the driver.", Toast.LENGTH_SHORT).show()
                        }
                        catch (e: WriterException){
                            e.printStackTrace()
                        }
                }
                else
                {
                    Toast.makeText(this, "Driver not found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}