package cat.copernic.disciplinevents2.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import cat.copernic.disciplinevents.DAO.UserDAO
import cat.copernic.disciplinevents2.activitys.MainActivity
import cat.copernic.disciplinevents2.activitys.MainActivityAdmin
import cat.copernic.disciplinevents2.activitys.Register
import cat.copernic.disciplinevents2.activitys.recoverPassword
import cat.copernic.disciplinevents2.R
import cat.copernic.disciplinevents2.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var userDAO: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //HIDE APP BAR
        if (supportActionBar != null)
            supportActionBar!!.hide()

        //INIT LAYOUT
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //INIT DAO
        userDAO = UserDAO()

        //GET INSTANCE
        auth = Firebase.auth

        //REGISTER
        binding.txtNoTienesCuenta.setOnClickListener{

            startActivity(Intent(this, Register::class.java))
            finish()
        }

        //PASSWORD
        binding.txtRecuperar.setOnClickListener {

            val intent = Intent(this, recoverPassword::class.java)
            startActivity((intent))
            finish()
        }

        //LOGIN
        binding.btnInicioSesion.setOnClickListener{

            //Save email and password values
            var email = binding.textInputLayoutEmailContent.text.toString().replace(" ", "")
            var password = binding.textInputLayoutPasswordContent.text.toString().replace(" ", "")

            //Create variable builder for AlertDialog
            val builder = AlertDialog.Builder(this)

            //Filters login
            if(email.isNotBlank()&&password.isNotEmpty()){
                login(email, password)

            } else if(email.isBlank()&&password.isNotEmpty()){
                builder.setTitle("Error")
                builder.setMessage("El correo no puede estar vacío")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()

            } else if(password.isBlank()&&email.isNotEmpty()){
                builder.setTitle("Error")
                builder.setMessage("La contraseña no puede estar vacia")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()

            } else if(password.isBlank()&&email.isBlank()){
                builder.setTitle("Error")
                builder.setMessage("Los campos no pueden estar vacíos")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()
            }
        }
    }

    //FUN LOGIN
    private fun login(email:String, password:String){

        //Create variable builder for AlertDialog
        val builder = AlertDialog.Builder(this)

        //Login user with email & password
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){task ->
            if(task.isSuccessful){

                userDAO.getUser().addOnSuccessListener { task ->

                    var currentUser = task!!

                    if (currentUser.admin!!) {
                        startActivity(Intent(this, MainActivityAdmin::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }

            } else {
                builder.setTitle("Error")
                builder.setMessage("Ha ocurrido un fallo en el Inicio de Sesión")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()

            }
        }
    }
    override fun onStart() {
        super.onStart()

    }
}