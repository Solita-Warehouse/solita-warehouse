package viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.User

class UserView : ViewModel() {

    private val _user = MutableLiveData<User?>()

    val user: MutableLiveData<User?> get() = _user

    fun setUser(user: User) {
        _user.value = user
    }

    fun deleteUser() {
        _user.value = null
    }
}