package org.vsu.pt.team2.utilitatemmetrisapp.ui.components

import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.orhanobut.logger.Logger
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.ui.main.SettingsFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.replaceFragment

class DrawerController(
    private val activity: AppCompatActivity,
    private val toolbar: Toolbar
) {
    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
    private lateinit var mDrawerLayout: DrawerLayout

    fun create() {
        createHeader()
        createDrawer()
        mDrawerLayout = mDrawer.drawerLayout
    }

    fun disableDrawer() {
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        toolbar.setNavigationOnClickListener {
            activity.supportFragmentManager.popBackStack()
        }
    }

    fun enableDrawer() {
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toolbar.setNavigationOnClickListener {
            Logger.i("Navigation clicked")
            mDrawer.openDrawer()
        }
    }

    private fun createDrawer() {
        mDrawer = DrawerBuilder()
            .withActivity(activity)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .withAccountHeader(mHeader)
            .addDrawerItems(
                PrimaryDrawerItem()
                    .withIdentifier(10)
                    .withIconTintingEnabled(true)
                    .withName("123")
                    .withSelectable(false),
            )
            .addStickyDrawerItems(
                PrimaryDrawerItem()
                    .withIdentifier(1010)
                    .withIconTintingEnabled(true)
                    .withName("Настройки")
                    .withSelectable(false)
            )
            .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    when (drawerItem.identifier) {
                        1010L ->
                            activity.replaceFragment(SettingsFragment())
//                        10L ->
//                            activity.replaceFragment(CreateRoomFragment())
                    }
                    return false
                }
            }).build()
    }

    private fun createHeader() {
        mHeader = AccountHeaderBuilder()
            .withActivity(activity)
            .withProfiles(
                mutableListOf(
                    ProfileDrawerItem()
                        .withName("name")
                        .withEmail("email")
                        .withNameShown(true)
                )
            )
//            .withHeaderBackground(R.drawable.header)
            .addProfiles(

            )
            .build()
//        activity.viewModels<UserInfoViewModel>().value.userLiveData.observe(
//            activity, {
//                mHeader.updateProfile(
//                    ProfileDrawerItem()
//                        .withName(it.nickname)
//                        .withEmail(it.email)
//                )
//            }
//        )
    }
}