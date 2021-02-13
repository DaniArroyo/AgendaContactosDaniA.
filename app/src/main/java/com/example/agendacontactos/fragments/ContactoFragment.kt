package com.example.agendacontactos.fragments

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import com.example.agendacontactos.R
import java.io.ByteArrayOutputStream

class ContactoFragment : Fragment() {

    val REQUEST_PICTURE = 100
    val PERMISSION_REQUEST_CODE = 101
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.imageView)
        val btnCapture = view.findViewById<ImageButton>(R.id.imageButtonCapture)
        val btnGuardar = view.findViewById<Button>(R.id.buttonGuardar)
        val nombre = view.findViewById<EditText>(R.id.editTextNombre)
        val apellidos = view.findViewById<EditText>(R.id.editTextApellidos)
        val email = view.findViewById<EditText>(R.id.editTextEmail)
        val telefono = view.findViewById<EditText>(R.id.editTextTelefono)

        btnCapture.setOnClickListener {
            if (checkPermissions()) {
                takePicture()
            } else {
                requestPermissions()
            }
        }
        btnGuardar.setOnClickListener {
            val intent = Intent(Intent.ACTION_INSERT)
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE)
            intent.putExtra(ContactsContract.Intents.Insert.NAME, nombre.text.toString() + " " + apellidos.text.toString())
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email.text.toString())
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, telefono.text.toString())

            val bmp: Bitmap? = getScreenViewBitmap(imageView)
            val stream = ByteArrayOutputStream()
            bmp!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()
            bmp.recycle()

            val row = ContentValues().apply {
                put(ContactsContract.CommonDataKinds.Photo.PHOTO, byteArray)
                put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
            }
            val data = arrayListOf(row)
            intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data)
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ContactoFragment().apply {
                }
    }

    private fun getScreenViewBitmap(view: View): Bitmap {
        val specSize = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(specSize, specSize)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(view.left, view.top, view.right, view.bottom)
        view.draw(canvas)
        return bitmap
    }

    private fun checkPermissions(): Boolean {
        return (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermissions() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
    }

    private fun takePicture() {
        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_PICTURE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture()
            } else {
                Toast.makeText(activity?.applicationContext, "Permiso denegado", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(activity?.applicationContext, "Permiso denegado", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PICTURE) {
            var bitmap = data!!.extras!!.get("data") as Bitmap
            imageView.setImageBitmap(bitmap)
        }
    }
}