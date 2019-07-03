package com.example.firebasesamplenb

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initMyAuthStateListener()

        txtSignUp.setOnClickListener {

            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnSignIn.setOnClickListener(this)

        txtVerifyEmailAgain.setOnClickListener {
            var showDialog = VerifyEmailAgainFragment()
            showDialog.show(supportFragmentManager, "show Dialog")

        }


    }

    private fun progressBarVisible() {
        pbSignIn.visibility = View.VISIBLE
    }

    private fun progressBarInvisible() {
        pbSignIn.visibility = View.INVISIBLE
    }

    private fun initMyAuthStateListener() {

        mAuthStateListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = p0.currentUser

                if (user != null) {

                    if (user.isEmailVerified) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Mail adresi onaylanmis giris yapilabilir!",
                            Toast.LENGTH_SHORT
                        ).show()
                        var intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Mail adresi onayli degil!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }else{
                    Toast.makeText(this@LoginActivity,"HATA!!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onClick(p0: View?) {

        if (etMail1.text.isNotEmpty() && etPassword1.text.isNotEmpty()) {

            progressBarVisible()

            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(etMail1.text.toString(), etPassword1.text.toString())
                .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                    override fun onComplete(p0: Task<AuthResult>) {

                        if (p0.isSuccessful) {
                            progressBarInvisible()

                            if (!p0.result!!.user.isEmailVerified) {
                                FirebaseAuth.getInstance().signOut()
                            }
                            /*Toast.makeText(
                                this@LoginActivity,
                                "Giris Basarili!" + FirebaseAuth.getInstance().currentUser?.email,
                                Toast.LENGTH_SHORT
                            ).show()
                            */

                        } else {
                            progressBarInvisible()
                            Toast.makeText(
                                this@LoginActivity,
                                "Hatali Giris!" + p0.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

                })

        } else {
            Toast.makeText(this, "Bos Alanlari Doldurun!", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener)
    }

}
