package cat.copernic.disciplinevents.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cat.copernic.disciplinevents.DAO.TimeDAO
import cat.copernic.disciplinevents2.R
import cat.copernic.disciplinevents2.databinding.FragmentCreateTimeBinding
import cat.copernic.disciplinevents.model.Time
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class createTime : Fragment() {

    private lateinit var binding: FragmentCreateTimeBinding
    private lateinit var calendar: Calendar
    private lateinit var timeDAO: TimeDAO
    private lateinit var timeObj: Time
    private val args by navArgs<createTimeArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}

       //Init DAO
        timeDAO = TimeDAO()

        //Init getInstance Calendar
        calendar = Calendar.getInstance()

        //Init time obj
        timeObj = Time("", "", "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inflate the layout for this fragment init binding
        binding = FragmentCreateTimeBinding.bind(view)

        var date = ""
        var time = ""

        binding.inputDate.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)) { view, year, monthOfYear, dayOfMonth ->
            date = "${dayOfMonth}/${monthOfYear+1}/${year}"
            binding.textDate.setText(date)
        }

        // Agregar un evento OnTimeChangedListener al TimePicker para obtener la hora seleccionada
        binding.inputTime.setOnTimeChangedListener { view, hourOfDay, minute ->
            time = "${hourOfDay}:${minute}"
            binding.textTime.setText(time)
        }

        binding.btnCrear.setOnClickListener {
            try {

                // Guardar la fecha y hora en el objeto timeObj
                timeObj.date = date
                timeObj.time = time

                timeDAO.setHorario(args.currentEvent.idEvent, timeObj)

                //Nav to InfoEvent
                val action = createTimeDirections.actionCreateTimeToInfoEvent(args.currentEvent)
                findNavController().navigate(action)

            } catch (e: ParseException) {
                Toast.makeText(requireContext(), "Error: fecha/hora no válida", Toast.LENGTH_SHORT).show()
            }
        }

        //Nav to InfoEvent
        binding.btnCancelarr.setOnClickListener {
            val action = createTimeDirections.actionCreateTimeToInfoEvent(args.currentEvent)
            findNavController().navigate(action)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment init binding
        binding = FragmentCreateTimeBinding.inflate(inflater, container, false)

        return binding.root
    }
}