package activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import fragment.LogoutFragment
import fragment.OrderHistoryFragment
import fragment.ProfileFragment
import com.pratishtha.foodrunner.R
import fragment.FaqFragment
import fragment.FavouriteFragment
import fragment.HomeFragment

class Main2_Activity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinateLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView


    var previousMenuItem:MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_)
        title="All Restaurants"

        drawerLayout=findViewById(R.id.drawerLayout)
        coordinateLayout=findViewById(R.id.coordinateLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frameLayout)
        navigationView=findViewById(R.id.navigationView)

        setUpToolbar()

        openHome()

        val actionBarDrawerToggle=ActionBarDrawerToggle(this@Main2_Activity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if(previousMenuItem!=null){
                previousMenuItem?.isChecked=false
            }

            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it

            when(it.itemId)
            {
                R.id.home ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        HomeFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="All Restaurants"
                }

                R.id.profile ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        ProfileFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Profile"
                }

                R.id.favourites ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        FavouriteFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Favourite Restaturants"
                }

                R.id.orderHistory ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        OrderHistoryFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Order History"
                }

                R.id.faq ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        FaqFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="FAQs"
                }

                R.id.logout ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        LogoutFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="LogOut"
                }


            }

            return@setNavigationItemSelectedListener(true)
        }

    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="All Restaurants"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openHome(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, HomeFragment()).commit()
        supportActionBar?.title="All Restaurants"
        navigationView.setCheckedItem(R.id.home)
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(frag){

            !is HomeFragment -> openHome()
            else->super.onBackPressed()

        }
    }
}
