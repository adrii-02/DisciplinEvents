package cat.copernic.disciplinevents.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var name: String?,
    var lastName: String?,
    val email: String?,
    var gender: String?,
    var admin: Boolean?,
    val events: ArrayList<Event>?

): Parcelable
