package cat.copernic.disciplinevents.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.disciplinevents2.databinding.ItemUserBinding
import cat.copernic.disciplinevents.model.User

class RUsersViewHolder(view: View) : RecyclerView.ViewHolder(view)
{

    private var binding: ItemUserBinding // Binding ItemEvent

    // Init View
    init {
        binding = ItemUserBinding.bind(view)
    }

    // Fun Render
    // Save event values in layout item
    fun render(user: User, onClickListener: (User) -> Unit) {
        binding.email.text = user.email.toString()

        binding.btnDelete.setOnClickListener {
            onClickListener(user)
        }

    }
}