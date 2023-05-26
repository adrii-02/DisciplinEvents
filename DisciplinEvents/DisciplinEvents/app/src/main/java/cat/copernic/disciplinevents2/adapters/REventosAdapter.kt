package cat.copernic.disciplinevents.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.disciplinevents2.R
import cat.copernic.disciplinevents.model.Event

class REventosAdapter(

    // List of Event (Parameter)
    private val listEvents:List<Event>,

    // Constructor parameter (lambda function)
    // Extends of ViewHolder" because this class is a Adapter
    private val onClickListener: (Event) -> Unit,
    private val onClickListener2: (Event) -> Unit,
    private val onClickListener3: (Event) -> Unit): RecyclerView.Adapter<REventosViewHolder>()

{
    // Inflates a layout file for each list item
    // Returns a new ViewHolder object that holds the inflated view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): REventosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return REventosViewHolder(layoutInflater.inflate(R.layout.item_info_event, parent, false))
    }

    // Get num of items in listEvents
    override fun getItemCount(): Int {
        return listEvents.size
    }

    // Sets the contents of the ViewHolder
    // Passing in the corresponding Event (item) object and the onClickListener
    override fun onBindViewHolder(holder: REventosViewHolder, position: Int) {
        val item = listEvents[position]
        holder.render(item, onClickListener, onClickListener2, onClickListener3)
    }
}