package cat.copernic.disciplinevents2.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.copernic.disciplinevents.DAO.UserDAO
import cat.copernic.disciplinevents2.activitys.Login
import cat.copernic.disciplinevents2.activitys.MainActivity
import cat.copernic.disciplinevents2.activitys.MainActivityAdmin
import cat.copernic.disciplinevents2.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.concurrent.schedule


class Splashard : AppCompatActivity() {
    private lateinit var userDAO: UserDAO
    var timer = Timer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //INIT DAO
        userDAO = UserDAO()

        //HIDE APP BAR
        if (supportActionBar != null)
            supportActionBar!!.hide()

        setContentView(R.layout.activity_splashard)



        timer.schedule(500) {

            val user = Firebase.auth.currentUser

            if (user != null) {

                userDAO.getUser().addOnSuccessListener { task ->

                    var currentUser = task!!

                    if (currentUser.admin!!) {
                        startActivity(
                            Intent(this@Splashard, MainActivityAdmin::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@Splashard, MainActivity::class.java))
                        finish()
                    }
                }

            } else {
                var intent = Intent(this@Splashard, Login::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}