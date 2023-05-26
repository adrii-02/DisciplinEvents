package cat.copernic.disciplinevents.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cat.copernic.disciplinevents.DAO.UserDAO
import cat.copernic.disciplinevents2.R
import cat.copernic.disciplinevents.Utils.Utils
import cat.copernic.disciplinevents2.databinding.FragmentEditProfileUserBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class EditProfileUser : Fragment() {

    private lateinit var binding: FragmentEditProfileUserBinding //Binding
    private lateinit var builder: android.app.AlertDialog.Builder
    private val args by navArgs<EditProfileUserArgs>()
    private var selectedImageUri: Uri? = null
    private lateinit var userDAO: UserDAO
    private lateinit var getContent: ActivityResultLauncher<String> //Implicit Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Init DAO
        userDAO = UserDAO()

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // Handle the returned Uri
            // Load the image into the ImageView
            Glide.with(this)
                .load(uri)
                .into(binding.imgUsuario)

            selectedImageUri = uri

            val storageRef = FirebaseStorage.getInstance().reference
            //agafem el nom del usuari que nosaltres introduirem per posar-lo al nom de l'imatge
            var text =  FirebaseAuth.getInstance().currentUser?.email?: "" + ".jpeg"

            //establim la ruta on s'emmagatzemarà l'imatge i establim el nom
            val imageRef = storageRef.child("imatges/usuaris").child(text)

            if (getContent != null) {
                //pujem l'imatge
                imageRef.putFile(selectedImageUri!!).addOnSuccessListener { taskSnapshot ->
                    //l'imatge s'ha pujat correctament
                }.addOnFailureListener { exception ->
                    //s'ha produit un error
                }

            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //OnClick btn save
        binding.btnGuardar.setOnClickListener{

            //Changes values
            args.currentUser.name = binding.textInputLayoutNombreContent.text.toString()
            args.currentUser.lastName = binding.textInputLayoutApellidoContent.text.toString()
            args.currentUser.gender = binding.textInputLayoutGeneroContent.text.toString()

            if(binding.textInputLayoutNombreContent.text.toString().isNotEmpty()&&
                binding.textInputLayoutApellidoContent.text.toString().isNotEmpty()&&
                binding.textInputLayoutGeneroContent.text.toString().isNotEmpty())
            {
                //Call fun editEvent
                userDAO.editUser(args.currentUser)

                //Nav to rEvents
                findNavController().navigate(R.id.action_editProfileUser_to_profileUser)

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
            //Nav to ProfileUser
            findNavController().navigate(R.id.action_editProfileUser_to_profileUser)
        }

        //OnClick change photo
        binding.imgUsuario.setOnClickListener {

            // Launch the intent to select an image from the gallery
            //Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getContent.launch("image/*")

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        // Inflate the layout for this fragment init binding
        binding = FragmentEditProfileUserBinding.inflate(inflater, container, false)

        // Add values of object in Layout
        val editName = binding.textInputLayoutNombreContent.findViewById<EditText>(R.id.textInputLayoutNombreContent)
        editName.text = Editable.Factory.getInstance().newEditable(args.currentUser.name)

        val editLastName = binding.textInputLayoutApellidoContent.findViewById<EditText>(R.id.textInputLayoutApellidoContent)
        editLastName.text = Editable.Factory.getInstance().newEditable(args.currentUser.lastName)

        val editGender = binding.textInputLayoutGeneroContent.findViewById<EditText>(R.id.textInputLayoutGeneroContent)
        editGender.text = Editable.Factory.getInstance().newEditable(args.currentUser.gender)

        //Call Corrutine
        lifecycleScope.launch(Dispatchers.IO) {
            Utils.loadImageFromFirebaseStorage(binding)
        }

        return binding.root
    }


}