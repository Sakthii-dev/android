package com.example.eshop.Home


import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.eshop.AppDatabase
import com.example.eshop.LoginSignUp.LoginScreen
import com.example.eshop.R
import com.example.eshop.User
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.eshop.R.layout.activity_home)

        window.statusBarColor = Color.parseColor("#FF1CB7FD")

        val drawerLayout:DrawerLayout = findViewById(com.example.eshop.R.id.drawerLayout)
        val navView:NavigationView = findViewById(com.example.eshop.R.id.nav_draw)
        val toolbar:androidx.appcompat.widget.Toolbar = findViewById(com.example.eshop.R.id.tool)



        val headerView = navView.getHeaderView(0)
        val fname = headerView.findViewById<View>(com.example.eshop.R.id.nav_fname) as TextView
        val lname = headerView.findViewById<View>(com.example.eshop.R.id.nav_lname) as TextView
        val email = headerView.findViewById<View>(com.example.eshop.R.id.nav_email) as TextView
        val phone = headerView.findViewById<View>(com.example.eshop.R.id.nav_phone) as TextView

         sharedPreferences = getSharedPreferences("Account",MODE_PRIVATE)
        val appDb: AppDatabase = AppDatabase.getDataBase(this)


        //Db User Data retrieve

        var user:User

        GlobalScope.launch(Dispatchers.IO) {

            user= appDb.UserDao().getUser(sharedPreferences.getString("phone","").toString())
            withContext(Dispatchers.Main){
                fname.setText(user.firstName)
                lname.setText(user.lastName)
                email.setText(user.email)
                phone.setText(user.phone)
            }

        }


        // Navigation drawer toggle setup
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.nav_header_title,R.string.navigation_drawer_close)
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))

        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    navView.setNavigationItemSelectedListener(this)

    }

    //Navigation drawer Item selection operation
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            com.example.eshop.R.id.logout -> {
                val editor = sharedPreferences.edit()
                editor.putString("phone","")
                editor.putString("password","")
                editor.apply()
                startActivity(Intent(this@Home, LoginScreen::class.java))
                finish ()
            }

        }

        return true
    }


}

