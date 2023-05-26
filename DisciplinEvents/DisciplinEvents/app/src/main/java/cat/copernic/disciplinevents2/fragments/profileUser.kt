package cat.copernic.disciplinevents.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cat.copernic.disciplinevents.DAO.UserDAO
import cat.copernic.disciplinevents2.R
import cat.copernic.disciplinevents.Utils.Utils
import cat.copernic.disciplinevents2.databinding.FragmentProfileUserBinding
import cat.copernic.disciplinevents.model.Event
import cat.copernic.disciplinevents.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.net.URL


class profileUser : Fragment() {

    private lateinit var binding: FragmentProfileUserBinding
    private lateinit var userDAO: UserDAO

    //Obj User temporal
    private var user = User("", "", "", "", false,  ArrayList<Event>())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}

        //Init DAO
        userDAO = UserDAO()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //OnClick btn edit
        binding.btnEditar.setOnClickListener{

            //Nav to editProfileUser with obj parameter
            val action = profileUserDirections.actionProfileUserToEditProfileUser(user)
            findNavController().navigate(action)
        }

    }

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment init binding
            binding = FragmentProfileUserBinding.inflate(inflater, container, false)

            userDAO.getUser().addOnSuccessListener { task ->

                user = task!!

                //Add values of object in Layout
                binding.nombre.text = user.name
                binding.apellido.text = user.lastName
                binding.genero.text = user.gender
                binding.email.text = user.email
            }



            return binding.root
    }

}