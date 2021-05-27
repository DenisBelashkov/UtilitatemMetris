package org.vsu.pt.team2.utilitatemmetrisapp.ui.components

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.descriptionText
import com.mikepenz.materialdrawer.model.interfaces.nameText
import com.mikepenz.materialdrawer.util.addItems
import com.mikepenz.materialdrawer.util.addStickyDrawerItems
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.AppbarContentBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.IntentExtrasManager
import org.vsu.pt.team2.utilitatemmetrisapp.managers.SessionManager
import org.vsu.pt.team2.utilitatemmetrisapp.ui.login.LoginActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.main.*
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.openActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.replaceFragment

class DrawerController(
    private val activity: AppCompatActivity,
    private val materialDrawerSliderView: MaterialDrawerSliderView,
    private val appbarContentBinding: AppbarContentBinding,
    private val sessionManager: SessionManager,
) {
    private var mDrawerLayout: DrawerLayout? = null

    fun create() {
        createHeader()
        createDrawer()
        mDrawerLayout = materialDrawerSliderView.drawerLayout
    }

    fun disableDrawer() {
        appbarContentBinding.iconHamburger.visibility = View.GONE
        appbarContentBinding.iconBack.visibility = View.VISIBLE
//        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        mDrawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        appbarContentBinding.toolbarIconContainer.setOnClickListener {
            activity.supportFragmentManager.popBackStack()
        }
    }

    fun enableDrawer() {
        appbarContentBinding.iconHamburger.visibility = View.VISIBLE
        appbarContentBinding.iconBack.visibility = View.GONE
//        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        mDrawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        appbarContentBinding.toolbarIconContainer.setOnClickListener {
            materialDrawerSliderView.drawerLayout?.open()
        }
    }

    fun initAnonimous(): DrawerItemsEasyCreator {
        return DrawerItemsEasyCreator().apply {
            addItem(
                simpleMenuItem("Оплатить"),
                { view, pos, drItem ->
                    activity.replaceFragment(AddMeterFragment())
                }
            )
            addItem(
                simpleMenuItem("Сохранённые счётчики"),
                { view, pos, drItem ->
                    activity.replaceFragment(SavedMetersFragment())
                }
            )
            addItem(
                DividerDrawerItem()
            )
            addItem(
                simpleMenuItem("Продолжить регистрацию"),
                { view, pos, drItem ->
                    activity.openActivity(
                        LoginActivity::class.java,
                        false,
                        IntentExtrasManager.continueRegister::putInto
                    )
                }
            )
        }
    }

    fun initDefalut(): DrawerItemsEasyCreator {
        return DrawerItemsEasyCreator().apply {
            addItem(
                simpleMenuItem("Оплатить"),
                { view, pos, drItem ->
                    activity.replaceFragment(AddMeterFragment())
                }
            )
            addItem(
                simpleMenuItem("Сохранённые счётчики"),
                { view, pos, drItem ->
                    activity.replaceFragment(SavedMetersFragment())
                }
            )
            addItem(
                DividerDrawerItem()
            )
            addItem(
                simpleMenuItem("Мои счета"),
                { view, pos, drItem ->
                    activity.replaceFragment(MyAccountsFragment())
                }
            )
            addItem(
                DividerDrawerItem()
            )
            addItem(
                simpleMenuItem("История выплат"),
                { view, pos, drItem ->
                    activity.replaceFragment(HistoryFragment())
                }
            )
        }
    }

    private fun createDrawer() {
        val drawerItemsEasyCreator = if (sessionManager.isDemo) initAnonimous() else initDefalut()
        for (item in drawerItemsEasyCreator.items()) {
            materialDrawerSliderView.addItems(item)
        }

        materialDrawerSliderView.addStickyDrawerItems(
            simpleMenuItem("Настройки").apply { identifier = 1010 }
        )
        materialDrawerSliderView.onDrawerItemClickListener = { view: View?,
                                                               drawerItem: IDrawerItem<*>,
                                                               position: Int ->
            if (drawerItem.identifier == 1010L)
                activity.replaceFragment(SettingsFragment())
            else
                drawerItemsEasyCreator.clickListener().invoke(view, drawerItem, position)
            false
        }
    }

    private fun createHeader() {
        val profileItem = ProfileDrawerItem().apply {
            nameText = sessionManager.user.email
            descriptionText = if (sessionManager.isDemo) "Анонимный аккаунт" else ""
        }
        AccountHeaderView(activity).apply {
            attachToSliderView(materialDrawerSliderView)
            profiles?.add(profileItem)
            selectionListEnabledForSingleProfile = false
            headerBackground = ImageHolder(R.drawable.header)

        }
    }

    private fun simpleMenuItem(text: String): IDrawerItem<*> {
        return PrimaryDrawerItem().apply {
            isIconTinted = true
            name = StringHolder(text)
            isSelectable = false
        }
    }

    class DrawerItemsEasyCreator {
        private var identifierCounter = 0L;

        private val drawerItemIdToPairItemAction = mutableMapOf<Long, Pair<IDrawerItem<*>, ((
            view: View?,
            drawerItem: IDrawerItem<*>,
            position: Int,
        ) -> Unit)?>>()

        fun addItem(
            item: IDrawerItem<*>,
            onClick: ((
                view: View?,
                drawerItem: IDrawerItem<*>,
                position: Int,
            ) -> Unit)? = null
        ) {
            drawerItemIdToPairItemAction[identifierCounter] = Pair(
                item.apply { identifier = identifierCounter.toLong() }, onClick
            )
            identifierCounter++
        }

        fun items(): List<IDrawerItem<*>> {
            return drawerItemIdToPairItemAction.values.map { it.first }
        }
//        fun addItemsToDrawer(mdsv: MaterialDrawerSliderView) {
//            for (itemAndAction in drawerItemIdToPairItemAction.values)
//                mdsv.addItems(itemAndAction.first)
//        }

        fun clickListener(): ((v: View?, item: IDrawerItem<*>, position: Int) -> Boolean) =
            { v: View?, item: IDrawerItem<*>, position: Int ->
                drawerItemIdToPairItemAction[item.identifier]?.second?.invoke(v, item, position)
                false
            }
    }
}