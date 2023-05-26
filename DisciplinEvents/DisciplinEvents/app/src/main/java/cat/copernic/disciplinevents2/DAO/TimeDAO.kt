package cat.copernic.disciplinevents.DAO

import android.util.Log
import cat.copernic.disciplinevents.model.Event
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

        // Inicializar DAO
        userDAO = UserDAO()
        bd = userDAO.getCurrentDB()

        // Definir una consulta que apunta a la subcolección "horarios" de un documento de evento específico
        val query = bd.collection("eventos").document(idEvento).collection("horarios")

        // Llamar al método get en la consulta definida anteriormente y agregar un escuchador para manejar la respuesta
        return query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Si la tarea se completa con éxito, recorrer el resultado y agregar los horarios a la lista de horarios
                val result = task.result
                if (result != null) {
                    for (document in result) {
                        val data = document.data
                        val idTime = document.id
                        val horario = data["horario"] as String
                        val fecha = data["fecha"] as String
                        val time = Time(idTime, fecha, horario)
                        horarios.add(time)
                    }
                }
            } else {
                // Si la tarea no se completa con éxito, registrar un error en los registros
                val exception = task.exception
                Log.e("TAG", "Error al descargar documentos", exception)
            }
        }.continueWith { task ->
            // Devolver una tarea con la lista de horarios
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

    fun editTime(event: Event, time: Time) {
        // Init DAO
        userDAO = UserDAO()
        bd = userDAO.getCurrentDB()

        // Users
        bd.collection("eventos").document(event.idEvent).collection("horarios").document(time.idTime).update(
            hashMapOf(
                "fecha" to time.date,
                "horario" to time.time
            ) as Map<String, Any>
        ).addOnSuccessListener {

            Log.d("TAG", "Horario actualizado correctamente")
        }.addOnFailureListener { e ->
            Log.e("TAG", "Error al actualizar el Horario", e)
        }
    }

    fun deleteTime(event: Event, time: Time) {
        // Init DAO
        userDAO = UserDAO()
        bd = userDAO.getCurrentDB()

        val documentRef = bd.collection("eventos").document(event.idEvent).collection("horarios").document(time.idTime)
        documentRef.delete().addOnSuccessListener {
            Log.d("TAG", "Horario eliminado correctamente")
        }.addOnFailureListener { e ->
            Log.e("TAG", "Error al eliminar el Horario", e)
        }
    }
}