package cat.copernic.disciplinevents2.activitys

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import cat.copernic.disciplinevents.DAO.UserDAO
import cat.copernic.disciplinevents2.R
import cat.copernic.disciplinevents2.databinding.ActivityMainAdminBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

/**

La clase MainActivityAdmin es la pantalla principal de la aplicación si eres Admin.

Implementa la interfaz NavigationView.OnNavigationItemSelectedListener para manejar la selección de elementos del menú lateral.
 */
class MainActivityAdmin : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainAdminBinding
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    private lateinit var userDAO: UserDAO
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //INIT LAYOUT
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Init userDAO
        userDAO = UserDAO()

        //GET INSTANCE
        auth = userDAO.getCurrentUser()

        //GET NavController
        navController = findNavController(R.id.navHostFragmentAdmin)


        //START -> LATERAL MENU & TOOLBAR
        //Set ActionBar into Toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        //Change Title
        //val actionBar = supportActionBar
        //actionBar?.setTitle("DisciplinEvents")

        //Save DrawerLayout in drawer
        drawer = binding.drawerLayoutAdmin

        //Config of toggle & drawer
        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        //Save navView (lateral menu) in navigationView
        val navigationView: NavigationView = binding.navViewAdmin
        //Select this like ItemSelectListener
        navigationView.setNavigationItemSelectedListener(this)
        //FINISH -> LATERAL MENU & TOOLBAR

    }

    //CALL METHOD WHEN SELECTED MENU ITEM
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)
        val navController = this.navController


        when (item.itemId) {
            R.id.cerrarSession -> {
                // Fun logout
                logOut(auth)
            }
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val view = super.onCreateView(name, context, attrs)
        //val navController = findNavController(R.id.)
        //navController.setGraph(R.navigation.admin_nav_graph)
        return view
    }

    //CALL METHOD AFTER onCreate
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()

    }

    //CALL METHOD IF BE A CHANGE IN DEVICE CONFIGURATION
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    //CALL METHOD WHEN MENU ITEM IS SELECT
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //If menu item selected is the burger
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logOut(auth: FirebaseAuth){
        //LogOut
        auth.signOut()

        //Nav to Login
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}