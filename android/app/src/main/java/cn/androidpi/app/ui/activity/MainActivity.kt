package cn.androidpi.app.ui.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import cn.androidpi.app.R
import cn.androidpi.app.databinding.ActivityMainBinding
import cn.androidpi.app.ui.activity.ReactMainActivity.Companion.OVERLAY_PERMISSION_REQ_CODE
import cn.androidpi.app.ui.base.BaseActivity
import cn.androidpi.app.ui.fragment.FragmentFactory
import cn.androidpi.app.ui.fragment.MainFragment
import cn.androidpi.app.ui.fragment.NotesFragment
import cn.androidpi.app.ui.fragment.TodoListFragment

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

//        setSupportActionBar(binding?.toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding?.navigationView?.setNavigationItemSelectedListener(this)

        if (null === savedInstanceState) {
            initFragments()
        }

    }


    fun initFragments() {
        val ft = supportFragmentManager?.beginTransaction()
        ft?.replace(R.id.content, MainFragment.newInstance())
        ft?.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when(itemId) {
            R.id.nav_todo -> TemplateActivity.startWith(this,
                    fragmentName = TodoListFragment.javaClass.canonicalName,
                    factory = object : FragmentFactory<TodoListFragment>() {
                        override fun create(): TodoListFragment {
                            return TodoListFragment.newInstance()
                        }
                    })
            R.id.nav_notes -> TemplateActivity.startWith(this,
                    fragmentName = NotesFragment.javaClass.canonicalName,
                    factory = object : FragmentFactory<NotesFragment>() {
                        override fun create(): NotesFragment {
                            return NotesFragment.newInstance()
                        }
                    })
        }
        binding?.drawer?.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }
}
