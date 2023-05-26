package cat.copernic.disciplinevents.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.disciplinevents.DAO.TimeDAO
import cat.copernic.disciplinevents2.R
import cat.copernic.disciplinevents.adapters.RHorariosAdapter
import cat.copernic.disciplinevents2.databinding.FragmentCreateTimeBinding
import cat.copernic.disciplinevents2.databinding.FragmentInfoEventBinding
import cat.copernic.disciplinevents2.databinding.FragmentProfileUserBinding
import cat.copernic.disciplinevents2.databinding.ItemInfoEventBinding
import cat.copernic.disciplinevents.model.Time


class InfoEvent : Fragment() {

    private lateinit var binding: FragmentInfoEventBinding
    private lateinit var bindingItem: ItemInfoEventBinding
    private lateinit var timeDAO: TimeDAO
    private val args by navArgs<InfoEventArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}

        //Init DAO
        timeDAO = TimeDAO()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //OnClick btn turn
        binding.btnVolver.setOnClickListener{
            //Nav to rEvents
            findNavController().navigate(R.id.action_infoEvent_to_REventos)
        }

        binding.btnSuma.setOnClickListener {
            val action = InfoEventDirections.actionInfoEventToCreateTime(args.currentEvent)
            findNavController().navigate(action)
        }

        //Call fun initRecyclerView
        initRecyclerView()

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
        val action = InfoEventDirections.actionInfoEventToEditInfoTime(time, args.currentEvent)
        findNavController().navigate(action)
    }

    private fun onItemDeletedSelected(time: Time){
        //Delete Item
        timeDAO.deleteTime(args.currentEvent, time)

        //Recharge Recyclerview
        initRecyclerView()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        // Inflate the layout for this fragment init binding
        binding = FragmentInfoEventBinding.inflate(inflater, container, false)

        //Add values of object in Layout
        binding.nombreEvento.text = args.currentEvent.name
        binding.descripcionEvento.text = args.currentEvent.description

        return binding.root
    }
}