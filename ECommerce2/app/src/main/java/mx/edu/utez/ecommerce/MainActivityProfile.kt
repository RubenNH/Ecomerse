package mx.edu.utez.ecommerce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.edu.utez.ecommerce.MainActivity.Companion.TAG
import mx.edu.utez.ecommerce.databinding.ActivityMainCreateAccountBinding
import mx.edu.utez.ecommerce.databinding.ActivityMainProfileBinding
import com.squareup.picasso.Picasso

class MainActivityProfile : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var profile: ImageView
    private lateinit var email: TextView
    private lateinit var fullname: TextView
    private lateinit var carganding: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_profile)
        auth = Firebase.auth
        db = Firebase.firestore;
        this.setTitle(R.string.profile)
        val binding = ActivityMainProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val btnSignOut = binding.btnExit
        profile = binding.imageView3
        email = binding.Email
        fullname = binding.profileFullName

        carganding = binding.carganding3
        carganding.visibility = View.GONE

        btnSignOut.setOnClickListener {
            auth.signOut()
            reload()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            email.text = currentUser.email
            getUserByUid(currentUser.uid)
        }else{
            reload()
        }
    }

    private fun reload() {
        val intent = Intent(this@MainActivityProfile, MainActivity::class.java)
        startActivity(intent)
    }

    private fun getUserByUid(uid: String){
        db.collection("users")
            .whereEqualTo("id", uid)
            .get()
            .addOnSuccessListener { documents ->
                carganding.visibility = View.VISIBLE
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    fullname.text = document.data["Name"].toString()
                    Picasso.get().load(document.data["Foto de perfil"].toString()).into(profile)
                }
            }
            .addOnFailureListener { exception ->
                carganding.visibility = View.GONE
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}