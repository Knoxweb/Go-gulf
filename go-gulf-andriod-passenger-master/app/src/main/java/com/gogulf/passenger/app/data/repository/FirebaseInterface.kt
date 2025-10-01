
import com.google.firebase.firestore.ListenerRegistration

interface CollectionInterface {
    fun listeners(listener: ListenerRegistration?)
}
