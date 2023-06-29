package com.example.eshop.LoginSignUp

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.eshop.AppDatabase
import com.example.eshop.Home.Home
import com.example.eshop.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginScreen : AppCompatActivity() {

    private lateinit var loginPassword: TextInputEditText
    private lateinit var loginMail:TextInputEditText

    private lateinit var alartText: TextView
    private lateinit var register:TextView
    private lateinit var tv_mail:TextView

    private lateinit var login: AppCompatButton
    private var isPhone = false
    private var isPassword:Boolean = false


    lateinit var emailTxInput: TextInputLayout
    lateinit var passwordLayout:TextInputLayout

    lateinit var sharedPreferences:SharedPreferences
    lateinit var appDb:AppDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        initId()


      //  checking acount directly logged In

        if (sharedPreferences.getString("phone","") !="" && sharedPreferences.getString("password","") !=""){
            startActivity(Intent(this@LoginScreen, Home::class.java))
            finish()
        }


        //Login Button press

        login.setOnClickListener {

            //number and password validation function
            checkNumber(loginMail.text.toString().trim { it <= ' ' })
            validatePassword(loginPassword.text.toString())



            // Db data insertion and sharedPreference updation
            if (isPhone && isPassword) {

                var isExist = false
                GlobalScope.launch(Dispatchers.IO) {

                    isExist= appDb.UserDao().isRecordExistsUserId(loginMail.text.toString())
                    withContext(Dispatchers.Main){
                        if(isExist) {

                            var dbPass = ""
                            GlobalScope.launch(Dispatchers.IO) {

                                dbPass= appDb.UserDao().isCorrectPassword(loginMail.text.toString())
                                withContext(Dispatchers.Main){
                                    if(dbPass == loginPassword.text.toString()){
                                        val editor = sharedPreferences.edit()

                                        editor.putString("phone",loginMail.text.toString())
                                        editor.putString("password",loginPassword.text.toString())
                                        editor.apply()

                                        startActivity(Intent(this@LoginScreen, Home::class.java))
                                        finish()
                                    }else{
                                        Toast.makeText(this@LoginScreen, "Wrong Password..!", Toast.LENGTH_LONG).show()
                                        passwordLayout.error = " "
                                    }

                                }

                            }

                        }else{
                            Toast.makeText(this@LoginScreen, "Create new Account", Toast.LENGTH_LONG).show()

                        }
                    }

                }


            }
        }

        //Sign Up button press to Navigate

        register.setOnClickListener(View.OnClickListener { view: View? ->
            startActivity(Intent(this@LoginScreen,
                    SignUp::class.java))
        })

    }

    //Phone number validation function

    private fun checkNumber(number: String) {

        if(number.length == 10) {

            for (i in number) {
                if (!Character.isDigit(i)) {

                    emailTxInput.error = " "
                    tv_mail.setText("Invalid Number")
                    isPhone = false
                    break
                } else {
                    emailTxInput.error = null
                    tv_mail.setText(" ")
                    isPhone = true
                }
            }
        }else if(number.length == 0){
            emailTxInput.error = " "
            tv_mail.setText("Should not be empty")
            isPhone = false
        }else{

            emailTxInput.error = " "
            tv_mail.setText("Number must be 10 digits")
            isPhone = false
        }


    }


// pre Initializations
    private fun initId() {
        window.statusBarColor = Color.parseColor("#FF1CB7FD")
        loginPassword = findViewById(R.id.loginPassword)
        alartText = findViewById(R.id.tv_pass_alart)
        register = findViewById<TextView>(R.id.signUp)
        loginMail = findViewById(R.id.email)
        login = findViewById(R.id.signIn)
        tv_mail = findViewById<TextView>(R.id.tv_mail_alart)
        emailTxInput = findViewById(R.id.emailIntx)
        passwordLayout = findViewById<TextInputLayout>(R.id.passwordLayout)

         sharedPreferences = getSharedPreferences("Account",MODE_PRIVATE)
         appDb = AppDatabase.getDataBase(this)
    }


    // Password validation funciton
    private fun validatePassword(text: String) {
        var textLength = false
        var hasSmallLetter:Boolean = false
        var hasCapitalLetter:Boolean = false
        var hasSpacialChar:Boolean = false
        var hasNumber:Boolean = false
        if (text.length == 8) {
            textLength = true
        }
        for (i in text.toCharArray()) {
            if (Character.isUpperCase(i)) {
                hasCapitalLetter = true
            } else if (Character.isDigit(i)) {
                hasNumber = true
            } else if (Character.isLowerCase(i)) {
                hasSmallLetter = true
            } else if (text.contains(i.toString() + "") && i != ' ') {
                hasSpacialChar = true
            }
        }
        val txLength = if (textLength) "" else "length shout be 8"
        val txUpper = if (hasCapitalLetter) "" else "1 Uppercase"
        val txLower = if (hasSmallLetter) "" else "1 Lowercase"
        val txSpecial = if (hasSpacialChar) "" else "1 Special char"
        val txnumber = if (hasSpacialChar) "" else "1 Number"
        if (!(textLength && hasSpacialChar && hasCapitalLetter && hasSmallLetter && hasNumber)) {
            isPassword = false
            alartText.visibility = View.VISIBLE
            passwordLayout.setError(" ")
            alartText.requestFocus()
            alartText.setTextColor(Color.RED)
            alartText.text = "Requiered: $txLength $txLower $txUpper $txSpecial $txnumber"
        } else {
            isPassword = true
            alartText.visibility = View.GONE
            alartText.setTextColor(Color.GREEN)
            alartText.text = "Strong password"
            passwordLayout.setError(null)
        }

    }


}

