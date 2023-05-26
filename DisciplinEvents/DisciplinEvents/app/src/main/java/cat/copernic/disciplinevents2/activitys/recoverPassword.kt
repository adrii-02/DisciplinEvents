package cat.copernic.disciplinevents2.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.appcompat.app.AlertDialog
import cat.copernic.disciplinevents2.databinding.ActivityRecoverPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


/**

Clase que maneja la recuperación de contraseña.

Hereda de la clase AppCompatActivity.
 */
class recoverPassword : AppCompatActivity() {

    private lateinit var binding: ActivityRecoverPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //HIDE APP BAR
        if (supportActionBar != null)
            supportActionBar!!.hide()

        //INIT LAYOUT
        binding = ActivityRecoverPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //GET INSTANCE
        auth = Firebase.auth


        //RESET PASSWORD
        binding.btnEnviar.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            var gmail = binding.inputEmail2.text.toString()

            if(gmail.isNotEmpty()) {
                resetPassword(gmail)

            } else {
                //Builder Message
                builder.setTitle("Error")
                builder.setMessage("El campo no puede estar vacío")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()
            }
        }

        //CANCEL
        binding.btnCancelar.setOnClickListener{
            Log.d("GoLogin", "Ir a Login")
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

    }

    //FUN RESET PASSWORD
    private fun resetPassword(email: String){
        val builder = AlertDialog.Builder(this)

        auth.setLanguageCode("es")
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->

            if(task.isSuccessful){
                //Builder Message
                builder.setTitle("Perfecto!")
                builder.setMessage("Se ha enviado el Email con éxito, dirígete a tu Email.")
                //Nav to Login when select "Aceptar"
                builder.setPositiveButton("Aceptar") { dialog, _ ->
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                }
                val dialog = builder.create()
                dialog.show()

            } else {
                //Builder Message
                builder.setTitle("Error")
                builder.setMessage("No se ha podido enviar el Correo, porfavor introdúzcalo de nuevo.")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()
            }
        }
    }
}