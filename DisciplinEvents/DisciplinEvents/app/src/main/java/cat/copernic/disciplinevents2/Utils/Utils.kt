package cat.copernic.disciplinevents.Utils

import androidx.core.net.toUri
import cat.copernic.disciplinevents2.databinding.FragmentEditProfileUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Utils {

    companion object{
        suspend fun loadImageFromFirebaseStorage(binding: FragmentEditProfileUserBinding) = withContext(Dispatchers.IO) {


            try {
                var storageRef = FirebaseStorage.getInstance().reference
                var text = FirebaseAuth.getInstance().currentUser?.email ?: ""
                var imageRef = storageRef.child("imatges").child("usuaris").child(text)

                var downloadUrl = imageRef.downloadUrl.await()
                var x = downloadUrl.toString().toUri()

                Picasso.get().load(downloadUrl.toString().toUri()).into(binding.imgUsuario)

            } catch (e: Exception) {

            }

        }
    }
}