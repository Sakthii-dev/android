package com.example.eshop.LoginSignUp

import android.annotation.SuppressLint

import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.eshop.AppDatabase
import com.example.eshop.AppDatabase.Companion.getDataBase
import com.example.eshop.Home.Home
import com.example.eshop.R
import com.example.eshop.User
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUp : AppCompatActivity() {

    private lateinit var firstname: EditText
    private lateinit var lastname:EditText
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var repassword:EditText
    private lateinit var phone:EditText



    lateinit var tv_firtname: TextView
    lateinit var tv_lastname:TextView
    lateinit var tv_password:TextView
    lateinit var tv_repassword:TextView
    lateinit var tv_mail:TextView
    lateinit var tv_phone:TextView

    var textLength = false
    var hasSmallLetter: Boolean = false
    var hasCapitalLetter: Boolean = false
    var hasSpacialChar: Boolean = false
    var hasNumber: Boolean = false

    lateinit var signUpbtn: Button



    var backBtn: ImageView? = null

    var builder: AlertDialog.Builder? = null


    lateinit var fnameLayout: TextInputLayout
    lateinit var mnameLayout:TextInputLayout
    lateinit var lnameLayout:TextInputLayout
    lateinit var passwordLayout:TextInputLayout
    lateinit var repasswordLayout:TextInputLayout
    lateinit var phoneLayout:TextInputLayout

    lateinit var cCode:CountryCodePicker

    var isMail = false
    var isFname = false
    var isLname = false
    var isPass = false
    var isRPass = false
    var isPhone = false

    lateinit var appDb:AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initId()


        //Sign Up button to call all validation functions

        signUpbtn.setOnClickListener { view: View? ->
            emptyCheck(firstname.text.toString(), tv_firtname, fnameLayout, "Fname")
            spaceCheck(firstname.text.toString(), tv_firtname, fnameLayout, "Fname")
            spaceCheck(email.text.toString(), tv_mail, mnameLayout, "Mail")
            emptyCheck(lastname.text.toString(), tv_lastname, lnameLayout, "Lname")
            spaceCheck(lastname.text.toString(), tv_lastname, lnameLayout, "Lname")
            validatePassword(password.text.toString(), passwordLayout)
            passwordMatchingCheck()
            spaceCheck(firstname.text.toString(), tv_firtname, fnameLayout, "Fname")

            emailValidation(email.text.toString())

            checkNumber(phone.text.toString())

            //Db updation and navigate to Login

            var isExist = false
            GlobalScope.launch(Dispatchers.IO) {

                isExist= appDb.UserDao().isRecordExistsUserId(phone.text.toString())
                withContext(Dispatchers.Main){
                    if(isExist){
                        Toast.makeText(this@SignUp, "This Number already exist "+isExist, Toast.LENGTH_LONG).show()
                    }else{
                        if(isFname && isLname && isMail && isPass && isRPass && isPhone && !isExist){



                            val user = User(
                                firstname.text.toString(), lastname.text.toString(), email.text.toString(),cCode.selectedCountryCode.toString(),phone.text.toString(),password.text.toString()
                            )
                            GlobalScope.launch(Dispatchers.IO) {
                                appDb.UserDao().insert(user)
                            }

                            builder!!.setTitle("Registered Successfully")
                                .setCancelable(true)
                                .setPositiveButton(
                                    "Continue"
                                ) { dialogInterface, i -> finish() }.show()

                        }
                    }
                }

            }


        }

        //back press operation

        backBtn?.setOnClickListener { view: View? -> showExitDialog() }


    }

    //Phone number validation function

    private fun checkNumber(number: String) {

        if(number.length == 10) {

            for (i in number) {
                if (!Character.isDigit(i)) {

                    phoneLayout.error = " "
                    tv_phone.setText("Invalid Number")
                    isPhone = false
                    break
                } else {
                    phoneLayout.error = null
                    tv_phone.setText(" ")
                    isPhone = true
                }
            }
        }else if(number.length == 0){
            phoneLayout.error = " "
            tv_phone.setText("Should not be empty")
            isPhone = false
        }else{
            phoneLayout.error = " "
            tv_phone.setText("Number must be 10 digits")
            isPhone = false
        }


    }

    //Pre Initialization

    private fun initId() {
        window.statusBarColor = Color.parseColor("#FF1CB7FD")
        firstname = findViewById(R.id.firstname)
        lastname = findViewById(R.id.lastname)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        repassword = findViewById(R.id.re_password)
        tv_firtname = findViewById(R.id.tv_firstname)
        tv_lastname = findViewById(R.id.tv_lastname)
        tv_mail = findViewById(R.id.tv_mail)
        tv_password = findViewById(R.id.tv_password)
        tv_repassword = findViewById(R.id.tv_re_password)
        signUpbtn = findViewById(R.id.signUp)
        backBtn = findViewById(R.id.backBtn)
        fnameLayout = findViewById(R.id.fnameLayout)
        mnameLayout = findViewById(R.id.mnameLayout)
        lnameLayout = findViewById(R.id.lnameLayout)
        passwordLayout = findViewById(R.id.passwordLayout)
        repasswordLayout = findViewById(R.id.repasswordLayout)
        phoneLayout = findViewById(R.id.phoneLayout)

        cCode = findViewById(R.id.cCode)
        phone = findViewById(R.id.phone)
        tv_phone = findViewById(R.id.tv_phone)

        builder = AlertDialog.Builder(this)
        appDb = getDataBase(this)


    }

    //Field Empty checking function
    private fun emptyCheck(charSequence: String, textView: TextView, layout: TextInputLayout, str: String) {
        if (charSequence.length == 0) {
            textView.visibility = View.VISIBLE
            textView.setTextColor(Color.RED)
            textView.text = "Should not be empty"
            layout.error = " "

            when(str){
                "Fname" ->{ isFname = false }
                "Lname" ->{isLname = false}
                "Mail" -> {isMail = false}
                "Pass" -> {isPass= false}
                "Phone" -> {isPhone = false}
            }

        } else {

            textView.text = ""
            layout.error = null

            when(str){
                "Fname" ->{ isFname = true }
                "Lname" ->{isLname = true}
                "Mail" -> {isMail = true}
                "Pass" -> {isPass= true}
                "Phone" -> {isPhone = true}
            }
        }



    }

    //Checking the Password and re-enter password are same
    @SuppressLint("SetTextI18n")
    private fun passwordMatchingCheck() {
        if (repassword.text.toString().length == 0) {
            tv_repassword.setTextColor(Color.RED)
            tv_repassword.text = "Should not be empty"
            repasswordLayout.error = " "
            isRPass = false
        } else {
            if (password.text.toString() == repassword.text.toString()) {
                tv_repassword.setTextColor(Color.GREEN)
                tv_repassword.text = "Matched"
                repasswordLayout.error = null
                isRPass = true
            } else {
                tv_repassword.setTextColor(Color.RED)
                tv_repassword.text = "Not Matching"
                repasswordLayout.error = " "
                isRPass = false
            }
        }
    }

    //checking unwanted symbols occurrence
    @SuppressLint("SetTextI18n")
    private fun spaceCheck(charSequence: String, nameView: TextView, layout: TextInputLayout, str: String) {
        if (charSequence.length != 0) {
            for (i in charSequence.toCharArray()) {
                if (i == ' ') {
                    nameView.visibility = View.VISIBLE
                    nameView.setTextColor(Color.RED)
                    nameView.text = "Space Not Allowed!"
                    layout.error = " "

                    when(str){
                        "Fname" ->{ isFname = false }
                        "Lname" ->{isLname = false}
                        "Mail" -> {isMail = false}
                        "Pass" -> {isPass= false}
                        "Phone" -> {isPhone = false}
                    }


                    break
                } else if (!Character.isAlphabetic(i.code)) {
                    nameView.visibility = View.VISIBLE
                    nameView.setTextColor(Color.RED)
                    nameView.text = "Invalid name"
                    layout.error = " "

                    when(str){
                        "Fname" ->{ isFname = false }
                        "Lname" ->{isLname = false}
                        "Mail" -> {isMail = false}
                        "Pass" -> {isPass= false}
                        "Phone" -> {isPhone = false}
                    }

                    break
                } else {

                    when(str){
                        "Fname" ->{ isFname = true }
                        "Lname" ->{isLname = true}
                        "Mail" -> {isMail = true}
                        "Pass" -> {isPass= true}
                        "Phone" -> {isPhone = true}
                    }

                    layout.error = null
                    nameView.text = " "
                    //nameView.setVisibility(View.GONE);
                }
            }
        }
    }

    //Password validation function

    @SuppressLint("SetTextI18n")
    private fun validatePassword(text: String, layout: TextInputLayout) {
        hasNumber = false
        hasSpacialChar = hasNumber
        hasCapitalLetter = hasSpacialChar
        hasSmallLetter = hasCapitalLetter
        textLength = hasSmallLetter
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
        val txLength = if (textLength) "" else "length shout be 8, "
        val txUpper = if (hasCapitalLetter) "" else "1 Uppercase, "
        val txLower = if (hasSmallLetter) "" else "1 Lowercase, "
        val txSpecial = if (hasSpacialChar) "" else "1 Special char, "
        val txnumber = if (hasSpacialChar) "" else "1 Number"
        if (!(textLength && hasSpacialChar && hasCapitalLetter && hasSmallLetter && hasNumber)) {
            tv_password.setTextColor(Color.RED)
            tv_password.visibility = View.VISIBLE
            tv_password.text = "Requiered: $txLength $txLower $txUpper $txSpecial $txnumber"
            layout.error = " "
            isPass = false
        } else {
            tv_password.visibility = View.VISIBLE
            tv_password.setTextColor(Color.GREEN)
            tv_password.text = "Strong password"
            layout.error = null
            isPass = true
        }

    }

    // mobile back press fucntion
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    //Exit alert dialog funciton

    private fun showExitDialog() {
        builder!!.setTitle("Do you want to Exit?")
            .setCancelable(true)
            .setPositiveButton(
                "Yes"
            ) { dialogInterface, i -> finish() }.setNegativeButton(
                "No"
            ) { dialogInterface, i -> dialogInterface.cancel() }.show()
    }

    //email format validation function
    private fun emailValidation(email: String) {

        if (email.length != 0) {

            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mnameLayout.error = null
                mnameLayout.error = null
                isMail = true
                tv_mail.setText("")

            } else {
                isMail = false
                mnameLayout.requestFocus()
                mnameLayout.error = "Invalid"
                tv_mail.setText("invalid email")
            }
        } else {
            mnameLayout.requestFocus()
            isMail = false
            mnameLayout.error = "Invalid"
            tv_mail.setText("Should not be empty")
        }
    }


}

