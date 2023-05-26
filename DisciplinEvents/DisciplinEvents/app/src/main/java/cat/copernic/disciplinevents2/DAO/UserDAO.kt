package cat.copernic.disciplinevents.DAO

import android.util.Log
import cat.copernic.disciplinevents.model.Event
import cat.copernic.disciplinevents.model.Time
import cat.copernic.disciplinevents.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserDAO {

    private lateinit var auth: FirebaseAuth
    private lateinit var bd: FirebaseFirestore
    private lateinit var userDAO : UserDAO

    // Fun getCurrentUser
    fun getCurrentUser(): FirebaseAuth {
        return Firebase.auth
    }

    //Fun getCurrentDB
    fun getCurrentDB(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    //Fun insertUser
    fun setUser(bd: FirebaseFirestore, userObj: User) {

        //Users
        bd.collection("usuarios").document(userObj.email.toString()).set(
            hashMapOf(
                "email" to userObj.email,
                "nombre" to userObj.name,
                "apellidos" to userObj.lastName,
                "genero" to userObj.gender,
                "admin" to userObj.admin
            )
        ).addOnSuccessListener {
            Log.d("TAG", "Usuario registrado correctamente")
        }.addOnFailureListener { e ->
            Log.e("TAG", "Error al registrar el usuario", e)
        }

    }

    fun getUser(): Task<User?> {
        //Init DAO
        userDAO = UserDAO()

        val db = FirebaseFirestore.getInstance()
        val userId = userDAO.getUserId()
        val documentRef = db.collection("usuarios").document(userId)

        return documentRef.get().continueWith { task ->
            val document = task.result
            if (document != null && document.exists()) {
                val data = document.data
                val name = data?.get("nombre") as String?
                val lastName = data?.get("apellidos") as String?
                val gender = data?.get("genero") as String?
                val email = data?.get("email") as String?
                val admin = data?.get("admin") as Boolean?
                val listEvents = ArrayList<Event>()

                val user = User(name, lastName, email, gender, admin, listEvents)
                //user.id = document.id

                user
            } else {
                Log.d("TAG", "No se encontró el usuario con ID: $userId")
                null
            }
        }.addOnFailureListener { e ->
            Log.e("TAG", "Error al obtener el usuario con ID: $userId", e)
        }
    }

    fun getUsers(): Task<ArrayList<User>> {
        val listUsers = ArrayList<User>()
        // Init DAO
        userDAO = UserDAO()
        auth = userDAO.getCurrentUser()
        bd = userDAO.getCurrentDB()
        val query = bd.collection("usuarios")
        return query.get().addOnSuccessListener { result ->
            for (document in result) {
                val data = document.data
                val name = data["nombre"] as String
                val lastname = data["apellidos"] as String
                val genero = data["genero"] as String
                val email = document.id
                val admin = data["admin"] as Boolean
                val listEvents = ArrayList<Event>()

                val user = User(name, lastname, email, genero, admin, listEvents)
                listUsers.add(user)
            }
        }.continueWith { task ->
            listUsers
        }
    }

    fun editUser(user: User) {
        // Init DAO
        userDAO = UserDAO()
        auth = userDAO.getCurrentUser()
        bd = userDAO.getCurrentDB()

        // Users
        bd.collection("usuarios").document(userDAO.getUserId()).update(
            hashMapOf(
                "nombre" to user.name,
                "apellidos" to user.lastName,
                "genero" to user.gender
            ) as Map<String, Any>
        ).addOnSuccessListener {

            Log.d("TAG", "Usuario actualizado correctamente")
        }.addOnFailureListener { e ->
            Log.e("TAG", "Error al actualizar el Usuario", e)
        }
    }

    fun deleteUser(user: User){
        // Init DAO
        userDAO = UserDAO()
        bd = userDAO.getCurrentDB()

        val documentRef = bd.collection("usuarios").document(user.email.toString())
        documentRef.delete().addOnSuccessListener {
            Log.d("TAG", "Usuario eliminado correctamente")
        }.addOnFailureListener { e ->
            Log.e("TAG", "Error al eliminar el Usuario", e)
        }
    }

    //Fun getEmail
    fun getUserId(): String {
        val currentUser = Firebase.auth.currentUser
        return currentUser?.email.toString()
    }
}