package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.ActivityMainBinding
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.DrawerController


class MainActivity : AppCompatActivity() {

    lateinit var drawerController: DrawerController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        setSupportActionBar(binding.appbarContentInclude.toolbar)

        drawerController = DrawerController(this, binding.appbarContentInclude.toolbar)
        drawerController.create()
        drawerController.enableDrawer()
    }
}