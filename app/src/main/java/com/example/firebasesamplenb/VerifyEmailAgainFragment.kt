package com.example.firebasesamplenb


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 */
class VerifyEmailAgainFragment : DialogFragment() {

    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var mContext: FragmentActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_verify_email_again, container, false)

        emailEditText = view.findViewById(R.id.etDialogMail)
        passwordEditText = view.findViewById(R.id.etDialogPassword)
        mContext = activity!!

        val btnCancel = view.findViewById<Button>(R.id.btnDialogCancel)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        val btnSend = view.findViewById<Button>(R.id.btnDialogSend)
        btnSend.setOnClickListener {

            if (emailEditText.text.toString().isNotEmpty() && passwordEditText.text.toString().isNotEmpty()) {

                loginAndSendMail(emailEditText.text.toString(), passwordEditText.text.toString())

            } else {
                Toast.makeText(mContext, "Bos alanlari Doldurunuz!", Toast.LENGTH_SHORT).show()
            }


        }


        return view
    }

    private fun loginAndSendMail(email: String, password: String) {

        var credential = EmailAuthProvider.getCredential(email, password)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sendVerifyMailAgain()
                dialog.dismiss()
            } else {
                Toast.makeText(mContext, "Email veya Sifre Hatali!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun sendVerifyMailAgain() {

        var user = FirebaseAuth.getInstance().currentUser

        if (user != null) {

            user.sendEmailVerification().addOnCompleteListener(object : OnCompleteListener<Void> {
                override fun onComplete(p0: Task<Void>) {
                    if (p0.isSuccessful) {
                        Toast.makeText(mContext, "Mail Gonderildi!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(mContext, "Mail Gonderilemedi!", Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }

    }


}
