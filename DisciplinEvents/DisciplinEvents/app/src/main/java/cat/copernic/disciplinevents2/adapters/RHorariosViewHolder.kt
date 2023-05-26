package cat.copernic.disciplinevents.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.disciplinevents2.databinding.ItemInfoEventBinding
import cat.copernic.disciplinevents2.databinding.ItemTimeBinding
import cat.copernic.disciplinevents.model.Time

class RHorariosViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private var binding: ItemTimeBinding

    init {
        binding = ItemTimeBinding.bind(view)
    }

    fun render(time: Time, onClickListener: (Time) -> Unit, onClickListener2: (Time) -> Unit) {
        binding.Horario.text = time.date + " " + time.time

        binding.btnEdit.setOnClickListener {
            onClickListener(time)
        }

        binding.btnDelete.setOnClickListener {
            onClickListener2(time)
        }

    }
}