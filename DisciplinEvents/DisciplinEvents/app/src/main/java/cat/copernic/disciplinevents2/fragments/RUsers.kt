package cat.copernic.disciplinevents.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.disciplinevents.DAO.UserDAO
import cat.copernic.disciplinevents2.R
import cat.copernic.disciplinevents.adapters.REventosAdapter
import cat.copernic.disciplinevents.adapters.RUsersAdapter
import cat.copernic.disciplinevents2.databinding.FragmentREventosBinding
import cat.copernic.disciplinevents2.databinding.FragmentRUsersBinding
import cat.copernic.disciplinevents.model.Event
import cat.copernic.disciplinevents.model.User
import hotchemi.android.rate.AppRate


class RUsers : Fragment() {

    private lateinit var binding: FragmentRUsersBinding
    private lateinit var userDAO: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}

        //INIT DAO
        userDAO = UserDAO()

        initRecyclerView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inflate the layout for this fragment init binding
        binding = FragmentRUsersBinding.bind(view)

        //Call fun initRecyclerView
        initRecyclerView()

        // Google Play
        AppRate.with(requireActivity()).setInstallDays(0).setLaunchTimes(2).setRemindInterval(1).monitor()
        AppRate.showRateDialogIfMeetsConditions(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_r_users, container, false)
    }

    private fun initRecyclerView(){

        userDAO.getUsers().addOnSuccessListener { users ->

            // Use the events list here to initialize the RecyclerView adapter
            val recyclerView = binding.recyclerUsers
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = RUsersAdapter(users, { onItemDeleteSelected(it) })

        }.addOnFailureListener { exception ->

            Log.println( Log.ERROR,"","No se han cargado los eventos :(")
        }
    }

    private fun onItemDeleteSelected(user: User){

        //Delete Item
        userDAO.deleteUser(user)

        //Recharge Recyclerview
        initRecyclerView()

    }
}