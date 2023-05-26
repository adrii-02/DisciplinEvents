package cat.copernic.disciplinevents.DAO

import android.util.Log
import cat.copernic.disciplinevents.model.Event
import cat.copernic.disciplinevents.model.Time
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


/**

    Clase que define el objeto DAO para eventos.
 */
class EventDAO {

    private lateinit var auth: FirebaseAuth
    private lateinit var bd: FirebaseFirestore
    private lateinit var userDAO : UserDAO

    /**

    Método que inserta un evento para el usuario actual.

    @param event el evento a ser insertado.
     */
    fun setEvent(event: Event){
        //Init DAO
        userDAO = UserDAO()
        auth = userDAO.getCurrentUser()
        bd = userDAO.getCurrentDB()

        var userEmail = userDAO.getUserId()

        //Assigned email to event
        event.currentUserEmail = userEmail

        //Users
        bd.collection("eventos").document().set(
            hashMapOf(
                "nombre" to event.name,
                "descripcion" to event.description,
                "usuarioId" to event.currentUserEmail
            )
        ).addOnSuccessListener {
            Log.d("TAG", "Evento registrado correctamente")

        }.addOnFailureListener { e ->
            Log.e("TAG", "Error al registrar el Evento", e)
        }
    }


    /**

    Método que actualiza un evento existente en la base de datos.

    @param event el evento a ser actualizado.
     */
    fun editEvent(event: Event) {
        // Init DAO
        userDAO = UserDAO()
        auth = userDAO.getCurrentUser()
        bd = userDAO.getCurrentDB()

        // Users
        bd.collection("eventos").document(event.idEvent).update(
            hashMapOf(
                "nombre" to event.name,
                "descripcion" to event.description,
            ) as Map<String, Any>
        ).addOnSuccessListener {

            Log.d("TAG", "Evento actualizado correctamente")
        }.addOnFailureListener { e ->
            Log.e("TAG", "Error al actualizar el Evento", e)
        }
    }

    /**

    Método que obtiene una lista de eventos del usuario actual.

    @return una tarea que devuelve una lista de eventos.
     */
    fun getEvents(): Task<ArrayList<Event>> {
        val events = ArrayList<Event>()
        // Init DAO
        userDAO = UserDAO()
        auth = userDAO.getCurrentUser()
        bd = userDAO.getCurrentDB()
        val query = bd.collection("eventos").whereEqualTo("usuarioId", userDAO.getUserId())
        return query.get().addOnSuccessListener { result ->
            for (document in result) {
                val data = document.data
                val idEvent = document.id
                val name = data["nombre"] as String
                val description = data["descripcion"] as String
                val email = data["usuarioId"] as String
                val listTimes = ArrayList<Time>()

                val event = Event(idEvent, name, description, email, listTimes)
                events.add(event)
            }
        }.continueWith { task ->
            events
        }
    }


    /**

    Método que elimina un evento y sus horarios de la base de datos.

    @param event el evento a eliminar.
     */
        fun deleteEvent(event: Event) {
            val db = FirebaseFirestore.getInstance()
            val eventRef = db.collection("eventos").document(event.idEvent)
            val horariosRef = eventRef.collection("horarios")

            horariosRef.get()
                .addOnSuccessListener { horariosSnapshot ->
                    db.runTransaction { transaction ->
                        for (horarioDoc in horariosSnapshot) {
                            transaction.delete(horarioDoc.reference)
                        }

                        transaction.delete(eventRef)
                    }
                        .addOnSuccessListener {
                            Log.d("TAG", "Evento y horarios eliminados correctamente")
                        }
                        .addOnFailureListener { e ->
                            Log.e("TAG", "Error al eliminar el Evento y horarios", e)
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("TAG", "Error al obtener la lista de horarios", e)
                }
        }
}