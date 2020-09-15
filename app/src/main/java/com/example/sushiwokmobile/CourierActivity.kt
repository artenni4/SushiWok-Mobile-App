package com.example.sushiwokmobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class CourierActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Log.println(Log.DEBUG, null, "start activity")
        setContentView(R.layout.activity_courier2)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val tabOrders = findViewById<com.google.android.material.tabs.TabItem>(R.id.orders_tab)
        val tabActiveOrders = findViewById<com.google.android.material.tabs.TabItem>(R.id.active_orders_tab)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)

        val pageAdapter = PageAdapter(
            supportFragmentManager,
            tabLayout.tabCount)
        viewPager.adapter = pageAdapter


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager.currentItem = tab.position
                    pageAdapter.notifyDataSetChanged()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.three_dots_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit_item -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, StartLoginActivity::class.java)
                startActivity(intent)
                OrdersListsHolder.clearAllLists()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}