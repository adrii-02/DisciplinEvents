package cat.copernic.disciplinevents.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cat.copernic.disciplinevents.DAO.EventDAO
import cat.copernic.disciplinevents.DAO.UserDAO
import cat.copernic.disciplinevents2.R
import cat.copernic.disciplinevents2.databinding.FragmentCreateEventBinding
import cat.copernic.disciplinevents2.databinding.FragmentEditInfoEventBinding
import cat.copernic.disciplinevents2.databinding.FragmentREventosBinding
import cat.copernic.disciplinevents.model.Event
import cat.copernic.disciplinevents.model.Time


class CreateEvent : Fragment() {

    private lateinit var binding: FragmentCreateEventBinding
    private lateinit var builder: android.app.AlertDialog.Builder
    private lateinit var eventDAO: EventDAO
    private lateinit var event: Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Init eventDAO
        eventDAO = EventDAO()

        //Init event
        event = Event("", "", "", "", ArrayList<Time>())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inflate the layout for this fragment init binding
        binding = FragmentCreateEventBinding.bind(view)

        binding.btnCrear.setOnClickListener {
            // Save layout values in event object
            event.name = binding.textInputLayoutNombreContent.text.toString()
            event.description = binding.textInputLayoutDescripcionContent.text.toString()

            if(binding.textInputLayoutNombreContent.text.toString().isNotEmpty()&&binding.textInputLayoutDescripcionContent.text.toString().isNotEmpty())
            {
                // Call fun setEvent
                eventDAO.setEvent(event)

                // Nav to REvent
                findNavController().navigate(R.id.action_createEvent_to_REventos)
            } else {
                builder = android.app.AlertDialog.Builder(requireContext())
                builder.setTitle("Error")
                builder.setMessage("Debes rellenar todos los campos.")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()
            }


        }

        //OnClick btn turn
        binding.btnCancelarr.setOnClickListener{
            //Nav to rEvents
            findNavController().navigate(R.id.action_createEvent_to_REventos)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment init binding
        binding = FragmentCreateEventBinding.inflate(inflater, container, false)

        return binding.root
    }


}