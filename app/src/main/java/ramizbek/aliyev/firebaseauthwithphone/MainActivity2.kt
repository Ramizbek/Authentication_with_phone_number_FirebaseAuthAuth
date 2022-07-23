package ramizbek.aliyev.firebaseauthwithphone

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import ramizbek.aliyev.firebaseauthwithphone.databinding.ActivityMain2Binding
import java.lang.Exception
import java.util.concurrent.TimeUnit

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var auth: FirebaseAuth
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private val num = MyObject.number
    private lateinit var handler: Handler
    private var countTime = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onResume() {
        super.onResume()
        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("uz")
        handler = Handler(Looper.getMainLooper())
        handler.postDelayed(runnable, 1)

        sendVerificationCode(num)

        binding.edtCode.addTextChangedListener {
            if (it.toString().length == 6) {
                verifyCode()
            }
        }

        binding.edtCode.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                val view = currentFocus
                if (view != null) {
                    val ime = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    ime.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
            true
        }
        mySubString()
    }

    @SuppressLint("SetTextI18n")
    private fun mySubString() {
        val bcode = "+998 (${num.substring(4, num.length - 7)}"
        val codes = "$bcode ${num.substring(6, num.length - 4)}-**-**"
        val ccode = "$bcode ${num.substring(6, num.length - 4)}"
        val dcode = "$ccode-${num.substring(num.length - 4, num.length - 2)}"
        val ecode = "$dcode-${num.substring(num.length - 2, num.length)}"
        binding.tvWarning.text = "One-time code $codes\n sent to the number"
        MyObject.number = ecode

    }

    private fun verifyCode() {
        try {
            val credential = PhoneAuthProvider.getCredential(
                storedVerificationId,
                binding.edtCode.text.toString()
            )
            signInWithPhoneAuthCredential(credential)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun resentCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val runnable = object : Runnable {
        @SuppressLint("SetTextI18n")
        override fun run() {
            if (binding.tvSmsCodeTimer.text.toString() != "00:00") {
                countTime--
                if (countTime <= 9) binding.tvSmsCodeTimer.text = "00:0$countTime"
                else binding.tvSmsCodeTimer.text = "00:$countTime"
                handler.postDelayed(this, 1000)
            } else {
                binding.resent.visibility = View.VISIBLE
                binding.resent.setOnClickListener {
                    resentCode(num)
                    countTime = 60
                    binding.resent.visibility = View.INVISIBLE
                }
            }
        }
    }


    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(p0: FirebaseException) {}

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(verificationId, token)
            storedVerificationId = verificationId
            resendToken = token
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity3::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "You entered wrong verification!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }
}