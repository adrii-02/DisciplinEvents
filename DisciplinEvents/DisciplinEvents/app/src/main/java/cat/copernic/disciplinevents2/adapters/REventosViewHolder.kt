package cat.copernic.disciplinevents.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.disciplinevents2.databinding.ItemInfoEventBinding
import cat.copernic.disciplinevents.model.Event

class REventosViewHolder(view: View) : RecyclerView.ViewHolder(view)
{

    private var binding: ItemInfoEventBinding // Binding ItemEvent

    // Init View
    init {
        binding = ItemInfoEventBinding.bind(view)
    }

    // Fun Render
    // Save event values in layout item
    fun render(event: Event, onClickListener: (Event) -> Unit, onClickListener2: (Event) -> Unit, onClickListener3: (Event) -> Unit) {
        binding.nombreEvento.text = event.name

        binding.btnEdit.setOnClickListener {
            onClickListener(event)
        }

        binding.btnDelete.setOnClickListener {
            onClickListener3(event)
        }

        binding.cardViewEvent.setOnClickListener {
            onClickListener2(event)
        }

    }
}