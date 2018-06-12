package com.androidpi.app.ui.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import com.androidpi.app.R
import com.androidpi.app.databinding.ActivityMainBinding
import com.androidpi.app.ui.base.BaseActivity
import com.androidpi.app.ui.fragment.FragmentFactory
import com.androidpi.app.ui.fragment.MainFragment
import com.androidpi.app.ui.fragment.NotesFragment
import com.androidpi.app.ui.fragment.TodoListFragment

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
