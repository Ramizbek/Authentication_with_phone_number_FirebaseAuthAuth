package ramizbek.aliyev.firebaseauthwithphone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import ramizbek.aliyev.firebaseauthwithphone.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyShared.init(this)

        if (MyShared.list.isEmpty()) {
            binding.btnEnter.setOnClickListener {
                val text = binding.edtPhoneNumber.text.toString()
                if (text.isNotEmpty() && text.length == 13 && text.substring(
                        0,
                        text.length - 9
                    ) == "+998"
                ) {
                    MyObject.number = text
                    startActivity(Intent(this, MainActivity2::class.java))
                } else {
                    Toast.makeText(this, "This is not Uzbek number", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            finish()
            startActivity(Intent(this, MainActivity3::class.java))

        }
    }
}