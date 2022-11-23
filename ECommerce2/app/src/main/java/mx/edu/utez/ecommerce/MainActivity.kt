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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.edu.utez.ecommerce.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var carganding: ProgressBar

    companion object{
        const val TAG="firebase-test"
    }
    //val db = Firebase.firestore connecion manual
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val editMail = binding.editEmail
        val editPassord = binding.editPassword
        val editButton = binding.btnLogin

        carganding = binding.cargading
        carganding.visibility = View.GONE

        editButton.setOnClickListener{
            if (editMail.text.isNotEmpty() && editPassord.text.isNotEmpty()){
                signWithEmailPaasword(editMail.text.toString(), editPassord.text.toString())

            }else{
                Toast.makeText(this, "Campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ifbtnCreate.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivityCreateAccount::class.java)
            startActivity(intent)
        }

        /*
        setContentView(R.layout.activity_main)
        val editEmail = findViewById<EditText>(R.if.editMsil)
        db.collection("testC") inicio de coneccion manual
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
*/
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

    private fun reload() {
        val intent = Intent(this@MainActivity, MainActivityProfile::class.java)
        intent.putExtra("id", 2)
        intent.putExtra("Name", "Maracuya explosiva")
        startActivity(intent)
    }

    private fun signWithEmailPaasword(email: String, password: String) {
        carganding.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    Log.d(TAG, "Succesfuly")
                    reload()
                }else{
                    Log.w(TAG, "YOU DUMBASS", task.exception)
                    Toast.makeText(baseContext, "Fail0",
                    Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener(this){
                carganding.visibility = View.GONE
                Toast.makeText(this, "Checa tu internet crack", Toast.LENGTH_SHORT).show()

            }
    }
}