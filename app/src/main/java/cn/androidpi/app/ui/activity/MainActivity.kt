package cn.androidpi.app.ui.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import cn.androidpi.app.R
import cn.androidpi.app.databinding.ActivityMainBinding
import cn.androidpi.app.ui.base.BaseActivity
import cn.androidpi.app.ui.fragment.MainFragment

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

//        binding?.drawer?.setStatusBarBackground(R.color.transparent)
//        val toggle = ActionBarDrawerToggle(this, binding?.drawer, binding?.toolbar,
//                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        binding?.drawer?.addDrawerListener(toggle)
//        toggle.syncState()

        binding?.navigationView?.setNavigationItemSelectedListener(this)

        if (null === savedInstanceState) {
            initFragments()
        }

        // Example of a call to a native method
//        sample_text.text = stringFromJNI()
    }

    fun initFragments() {
        val ft = supportFragmentManager?.beginTransaction()
        ft?.replace(R.id.content, MainFragment.newInstance())
        ft?.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when(itemId) {
            // todo
        }
        binding?.drawer?.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    external fun stringFromJNI(): String

//    companion object {
//
//        // Used to load the 'native-lib' library on application startup.
//        init {
//            System.loadLibrary("native-lib")
//        }
//    }
}