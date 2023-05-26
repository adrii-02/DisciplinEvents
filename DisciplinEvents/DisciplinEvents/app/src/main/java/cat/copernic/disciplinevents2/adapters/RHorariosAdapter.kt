package cat.copernic.disciplinevents.adapters

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.disciplinevents2.R
import cat.copernic.disciplinevents.model.Time

class RHorariosAdapter(
    private val listTime: List<Time>,
    private val onClickListener: (Time) -> Unit,
    private val onClickListener2: (Time) -> Unit): RecyclerView.Adapter<RHorariosViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RHorariosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RHorariosViewHolder(layoutInflater.inflate(R.layout.item_time, parent, false))
    }

    override fun getItemCount(): Int {
        return listTime.size
    }

    override fun onBindViewHolder(holder: RHorariosViewHolder, position: Int) {
        val item = listTime[position]
        holder.render(item, onClickListener, onClickListener2)
    }

}