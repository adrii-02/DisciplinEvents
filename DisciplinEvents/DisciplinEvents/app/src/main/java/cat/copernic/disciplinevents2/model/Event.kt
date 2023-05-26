package cat.copernic.disciplinevents.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp

@Parcelize
data class Event (
    var idEvent: String,
    var name: String,
    var description: String,
    var currentUserEmail: String?,
    var times: ArrayList<Time>,
): Parcelable
