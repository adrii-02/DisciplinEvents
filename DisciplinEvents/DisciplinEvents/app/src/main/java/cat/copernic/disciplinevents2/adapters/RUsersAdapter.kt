package cat.copernic.disciplinevents.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.disciplinevents2.R
import cat.copernic.disciplinevents.model.Event
import cat.copernic.disciplinevents.model.User

class RUsersAdapter(

    // List of Event (Parameter)
    private val listUsers:List<User>,

    // Constructor parameter (lambda function)
    // Extends of ViewHolder" because this class is a Adapter
    private val onClickListener: (User) -> Unit): RecyclerView.Adapter<RUsersViewHolder>()

{
    // Inflates a layout file for each list item
    // Returns a new ViewHolder object that holds the inflated view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RUsersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RUsersViewHolder(layoutInflater.inflate(R.layout.item_user, parent, false))
    }

    // Get num of items in listEvents
    override fun getItemCount(): Int {
        return listUsers.size
    }

    // Sets the contents of the ViewHolder
    // Passing in the corresponding Event (item) object and the onClickListener
    override fun onBindViewHolder(holder: RUsersViewHolder, position: Int) {
        val item = listUsers[position]
        holder.render(item, onClickListener)
    }
}