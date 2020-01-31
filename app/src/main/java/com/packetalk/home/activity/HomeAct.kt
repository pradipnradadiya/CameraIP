package com.packetalk.home.activity

import android.os.Handler
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.Trailer.fragment.TrailerFrg
import com.packetalk.home.fragment.HomeFrg
import com.packetalk.map.fragment.MapFrg
import com.packetalk.setting.fragment.SettingFrg
import com.packetalk.user.fragment.UserFrg
import com.packetalk.util.AppLogger
import com.packetalk.util.SharedPreferenceSession
import com.packetalk.util.showInfoToast
import kotlinx.android.synthetic.main.act_home.*

class HomeAct : BaseActivity() {
    private val frg = supportFragmentManager
    private val homeFrg = HomeFrg()
    private val userFrg = UserFrg()
    private val mapFrg = MapFrg()
    private val trailerFrg = TrailerFrg()
    private val settingFrg = SettingFrg()
    private var doubleBackToExitPressedOnce = false
    val session: SharedPreferenceSession? = null

    companion object {
        private const val ID_USER = 1
        private const val ID_MAP = 2
        private const val ID_HOME = 3
        private const val ID_SETTING = 4
        private const val ID_TRAILER = 5
        var userId = ""
        var currentMapTrailerFlag = 0
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.act_home
    }

    override fun init() {
        bindToolBar("Home")
        val session = SharedPreferenceSession(this@HomeAct)
    }

    override fun initView() {
        bindBottomNavigation()
    }

    override fun postInitView() {
        if (!session?.memberId.equals("") || session?.memberId.isNullOrBlank()) {
            userId = session?.memberId.toString()
        }
    }

    override fun addListener() {
    }

    override fun loadData() {
//        frg.beginTransaction().add(R.id.frameContainer, trailerFrg, "5").hide(trailerFrg).commit();
//        frg.beginTransaction().add(R.id.frameContainer, settingFrg, "4").hide(settingFrg).commit();
//        frg.beginTransaction().add(R.id.frameContainer, homeFrg, "3").commit();
//        frg.beginTransaction().add(R.id.frameContainer, mapFrg, "2").hide(mapFrg).commit();
//        frg.beginTransaction().add(R.id.frameContainer, userFrg, "1").hide(userFrg).commit();

        replaceFragment(homeFrg)
    }

    private fun selectContentFragment(fragmentToSelect: Fragment) {
        val fragmentTransaction = this.supportFragmentManager.beginTransaction()
        if (this.supportFragmentManager.fragments.contains(fragmentToSelect)) {
            // Iterate through all cached fragments.
            for (cachedFragment in this.supportFragmentManager.fragments) {
                if (cachedFragment !== fragmentToSelect) {
                    // Hide the fragments that are not the one being selected.
                    fragmentTransaction.hide(cachedFragment)
                }
            }
            // Show the fragment that we want to be selected.
            fragmentTransaction.show(fragmentToSelect)
        } else {
            // The fragment to be selected does not (yet) exist in the fragment manager, add it.
            fragmentTransaction.add(R.id.frameContainer, fragmentToSelect)
        }
        fragmentTransaction.commit()
    }

    private fun bindBottomNavigation() {
        //Fragment declaration
        bottomNavigation.add(MeowBottomNavigation.Model(ID_USER, R.drawable.ic_user))
        bottomNavigation.add(MeowBottomNavigation.Model(ID_MAP, R.drawable.ic_map))
        bottomNavigation.add(MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_homes))
        bottomNavigation.add(MeowBottomNavigation.Model(ID_TRAILER, R.drawable.ic_trailer))
        bottomNavigation.add(MeowBottomNavigation.Model(ID_SETTING, R.drawable.ic_setting_ico))
        bottomNavigation.show(ID_HOME, true)

        bottomNavigation.setOnShowListener {
            val name = when (it.id) {
                ID_USER -> {
                    AppLogger.i("user")
                }
                ID_MAP -> {
                    AppLogger.i("map")
                }
                ID_HOME -> {
                    AppLogger.i("home")
                }
                ID_SETTING -> {
                    AppLogger.i("setting")
                }
                ID_TRAILER -> {
                    AppLogger.i("trailer")
                }
                else -> {
                    AppLogger.i("else")
                }
            }

            AppLogger.e(it.id.toString())
            AppLogger.e("$name page is selected")
        }

        bottomNavigation.setOnClickMenuListener {
            val name = when (it.id) {
                ID_USER -> {
//                    selectContentFragment(userFrg)
                    replaceFragment(userFrg)
                    bindToolBar("Users")
                }

                ID_MAP -> {
//                    selectContentFragment(mapFrg)
                    replaceFragment(mapFrg)
                    bindToolBar("Map")
                }

                ID_HOME -> {
//                    selectContentFragment(homeFrg)
                    replaceFragment(homeFrg)
                    bindToolBar("Home")
                }

                ID_SETTING -> {
//                    selectContentFragment(settingFrg)
                    replaceFragment(settingFrg)
                    bindToolBar("Setting")
                }

                ID_TRAILER -> {
//                    selectContentFragment(trailerFrg)
                    replaceFragment(trailerFrg)
                    bindToolBar("Trailer")
                }

                else -> {

                }
            }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        showInfoToast("Please click BACK again to exit")

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }


    override fun onResume() {
        super.onResume()
        if (currentMapTrailerFlag == 1) {
            currentMapTrailerFlag = 0
            replaceFragment(mapFrg)
            bottomNavigation.show(ID_MAP, true)
            bindToolBar("Map")
        } else if (currentMapTrailerFlag == 2) {
            currentMapTrailerFlag = 0
            replaceFragment(trailerFrg)
            bottomNavigation.show(ID_TRAILER, true)
            bindToolBar("Trailer")
        }
    }
}
