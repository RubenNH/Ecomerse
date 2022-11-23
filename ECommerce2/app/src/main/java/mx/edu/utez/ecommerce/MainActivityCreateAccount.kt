package mx.edu.utez.ecommerce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import mx.edu.utez.ecommerce.databinding.ActivityMainCreateAccountBinding

class MainActivityCreateAccount : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var carganding: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_profile)
        auth = Firebase.auth
        val binding = ActivityMainCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editEmail = binding.idEmail
        val editPass = binding.idPasswd
        val editPass2 = binding.idPasswd2
        val cedionfirm = binding.idbtn

        carganding = binding.cargandind2
        carganding.visibility = View.GONE

        cedionfirm.setOnClickListener {
            if (editEmail.text.isNotEmpty() && editPass.text.isNotEmpty() && editPass2.text.isNotEmpty()){
                if (editPass.text.toString().length > 6){
                    if (editPass.text.toString() == editPass2.text.toString()){
                        createAccount(editEmail.text.toString(), editPass.text.toString())
                    }else{
                        Toast.makeText(this,"error 1", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this,"error 2", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,R.string.dataNotAllowed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        carganding.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                    task ->
                carganding.visibility = View.GONE
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(MainActivity.TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    reload()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(MainActivity.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener(this){
                carganding.visibility = View.GONE
                Toast.makeText(this, "Checa tu internet crack", Toast.LENGTH_SHORT).show()
            }
    }

    private fun reload() {
        val intent = Intent(this@MainActivityCreateAccount, MainActivityProfile::class.java)
        startActivity(intent)
    }
}