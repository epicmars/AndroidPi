package cn.androidpi.app.components.activity

import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import android.view.WindowManager
import cn.androidpi.app.R
import cn.androidpi.app.components.base.BaseActivity
import cn.androidpi.app.components.fragment.MainFragment
import cn.androidpi.app.databinding.ActivityMainBinding

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

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
