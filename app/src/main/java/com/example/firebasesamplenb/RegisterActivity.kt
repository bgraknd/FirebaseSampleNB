package com.example.firebasesamplenb

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener {

            if (etMail.text.isNotEmpty() && etPassword.text.isNotEmpty() && etPasswordConfirm.text.isNotEmpty()) {
                if (etPassword.text.toString().equals(etPasswordConfirm.text.toString())) {

                    newUserRegister(etMail.text.toString(), etPassword.text.toString())

                } else {
                    Toast.makeText(this, "Farkli Sifre", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Bos alanlari doldurunuz!", Toast.LENGTH_SHORT).show()
            }


        }
    }

    private fun newUserRegister(mail: String, password: String) {

        progressBarVisible()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, password)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                override fun onComplete(p0: Task<AuthResult>) {

                    if (p0.isSuccessful) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Uye Kaydedildi" + FirebaseAuth.getInstance().currentUser?.uid,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        VerifyMail()
                        FirebaseAuth.getInstance().signOut()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Uye Kaydi Basarisiz!" + p0.exception?.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }

                }

            })

        progressBarInvisible()

    }

    private fun progressBarVisible() {
        pbRegister.visibility = View.VISIBLE
    }

    private fun progressBarInvisible() {
        pbRegister.visibility = View.INVISIBLE

    }

    private fun VerifyMail() {

        var user = FirebaseAuth.getInstance().currentUser

        if (user != null) {

            user.sendEmailVerification().addOnCompleteListener(object : OnCompleteListener<Void> {
                override fun onComplete(p0: Task<Void>) {
                    if (p0.isSuccessful) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Mail Gonderildi!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Mail Gonderilemedi!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            })
        }

    }


}
