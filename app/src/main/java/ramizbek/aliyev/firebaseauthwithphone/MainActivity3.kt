package ramizbek.aliyev.firebaseauthwithphone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ramizbek.aliyev.firebaseauthwithphone.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityMain3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvPhoneNumberMain3.text = MyShared.list[0]
    }
}