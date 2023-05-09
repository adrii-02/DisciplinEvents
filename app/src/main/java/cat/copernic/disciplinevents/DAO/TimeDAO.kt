package cat.copernic.disciplinevents.DAO

import android.util.Log
import cat.copernic.disciplinevents.model.Time
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import kotlin.collections.ArrayList

class TimeDAO {

    private lateinit var bd: FirebaseFirestore
    private lateinit var userDAO : UserDAO

    fun getHorarios(idEvento: String): Task<ArrayList<Time>> {
        val horarios = ArrayList<Time>()
        // Init DAO
        userDAO = UserDAO()
        bd = userDAO.getCurrentDB()
        val query = bd.collection("eventos").document(idEvento).collection("horarios")
        return query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result
                if (result != null) {
                    for (document in result) {
                        val data = document.data
                        val horario = data["horario"] as String
                        val fecha = data["fecha"] as String
                        val time = Time(fecha, horario)
                        horarios.add(time)
                    }
                }
            } else {
                val exception = task.exception
                Log.e("TAG", "Error al descargar documentos", exception)
            }
        }.continueWith { task ->
            horarios
        }
    }

    fun setHorario(idEvento: String, horario: Time): Task<Void> {
        // Init DAO
        userDAO = UserDAO()
        bd = userDAO.getCurrentDB()

        // Add new horario to "horarios" subcollection of the specified evento document
        return bd.collection("eventos").document(idEvento).collection("horarios").document().set(
                hashMapOf(
                    "fecha" to horario.date,
                    "horario" to horario.time
                )
            )
            .addOnSuccessListener {
                Log.d("TAG", "Horario creado correctamente")
            }.addOnFailureListener { e ->
                Log.e("TAG", "Error al crear el horario", e)
            }
    }

    fun editHorario() {
        //Acabar función
    }
}