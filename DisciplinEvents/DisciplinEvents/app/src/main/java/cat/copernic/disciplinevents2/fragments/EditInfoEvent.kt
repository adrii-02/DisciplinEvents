package cat.copernic.disciplinevents.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.disciplinevents.DAO.EventDAO
import cat.copernic.disciplinevents.DAO.TimeDAO
import cat.copernic.disciplinevents.DAO.UserDAO
import cat.copernic.disciplinevents2.R
import cat.copernic.disciplinevents.adapters.REventosAdapter
import cat.copernic.disciplinevents.adapters.RHorariosAdapter
import cat.copernic.disciplinevents2.databinding.FragmentEditInfoEventBinding
import cat.copernic.disciplinevents2.databinding.FragmentInfoEventBinding
import cat.copernic.disciplinevents2.databinding.FragmentProfileUserBinding
import cat.copernic.disciplinevents2.databinding.FragmentREventosBinding
import cat.copernic.disciplinevents.model.Event
import cat.copernic.disciplinevents.model.Time
import cat.copernic.disciplinevents.model.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditInfoEvent : Fragment() {

    private lateinit var binding: FragmentEditInfoEventBinding
    private lateinit var timeDAO: TimeDAO
    private lateinit var eventDAO: EventDAO
    private lateinit var builder: android.app.AlertDialog.Builder
    private val args by navArgs<EditInfoEventArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}

        //Init DAO
        timeDAO = TimeDAO()
        eventDAO = EventDAO()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inflate the layout for this fragment init binding
        binding = FragmentEditInfoEventBinding.bind(view)

        //Call fun initRecyclerView
        initRecyclerView()

        //OnClick btn save
        binding.btnGuardar.setOnClickListener{

            //Changes values
            args.currentEvent.name = binding.textInputLayoutNombreContent.text.toString()
            args.currentEvent.description = binding.textInputLayoutDescriptionContent.text.toString()

            if(binding.textInputLayoutNombreContent.text.toString().isNotEmpty()&&binding.textInputLayoutDescriptionContent.text.toString().isNotEmpty())
            {
                //Call fun editEvent
                eventDAO.editEvent(args.currentEvent)

                //Nav to rEvents
                findNavController().navigate(R.id.action_editInfoEvent_to_REventos)
            } else {
                builder = android.app.AlertDialog.Builder(requireContext())
                builder.setTitle("Error")
                builder.setMessage("Debes rellenar todos los campos.")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()
            }

        }

        //OnClick btn cancel
        binding.btnCancelar.setOnClickListener{
            //Nav to rEvents
            findNavController().navigate(R.id.action_editInfoEvent_to_REventos)
        }

    }

    private fun initRecyclerView(){
        timeDAO.getHorarios(args.currentEvent.idEvent).addOnSuccessListener { times ->
            args.currentEvent.times = times

            val recyclerView = binding.recyclerTimes
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = RHorariosAdapter(args.currentEvent.times, { onItemSelected(it) }, { onItemDeletedSelected(it) })

        }.addOnFailureListener { exception ->
            Log.println( Log.ERROR,"","No se han cargado los horarios :(")
        }

    }

    private fun onItemSelected(time: Time){
        val action = EditInfoEventDirections.actionEditInfoEventToEditInfoTime(time, args.currentEvent)
        findNavController().navigate(action)
    }

    private fun onItemDeletedSelected(time: Time){
        //Delete Item
        timeDAO.deleteTime(args.currentEvent, time)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        // Inflate the layout for this fragment init binding
        binding = FragmentEditInfoEventBinding.inflate(inflater, container, false)

        //Add values of object in Layout
        val editName = binding.textInputLayoutNombreContent.findViewById<EditText>(R.id.textInputLayoutNombreContent)
        editName.text = Editable.Factory.getInstance().newEditable(args.currentEvent.name)

        val editDescription = binding.textInputLayoutDescriptionContent.findViewById<EditText>(R.id.textInputLayoutDescriptionContent)
        editDescription.text = Editable.Factory.getInstance().newEditable(args.currentEvent.description)

        return binding.root
    }
}