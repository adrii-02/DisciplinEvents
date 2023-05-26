package cat.copernic.disciplinevents.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.google.firebase.Timestamp
import java.time.LocalTime
import java.util.Date

@Parcelize
data class Time(
    var idTime: String,
    var date: String,
    var time: String
): Parcelable
