package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.ActivityMainBinding
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.DrawerController


class MainActivity : AppCompatActivity() {

    lateinit var drawerController: DrawerController
    lateinit var titleTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        titleTV = binding.appbarContentInclude.toolbarTitleTextview
        setSupportActionBar(binding.appbarContentInclude.toolbar)

        val materialDrawerSliderView = binding.slider

        drawerController =
            DrawerController(this, materialDrawerSliderView, binding.appbarContentInclude)
        drawerController.create()
        drawerController.enableDrawer()
    }
}