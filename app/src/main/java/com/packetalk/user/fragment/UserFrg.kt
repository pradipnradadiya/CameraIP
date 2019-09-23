package com.packetalk.user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.packetalk.BaseFragment
import com.packetalk.R
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.frg_user.*

class UserFrg : BaseFragment(), View.OnClickListener {
    private val frg = fragmentManager
    private val createUserFrg = CreateUserFrg()
    private val userLogFrg = UserLogFrg()
    private val updateUserFrg = UpdateUserFrg()

    companion object {
        private const val CREATE_USER = 1
        private const val USER_LOG = 2
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.creteUserClick -> {
                hideView()
                viewUser.visibility = View.VISIBLE
                callFragment(createUserFrg)
            }

            R.id.userLogClick -> {
                hideView()
                viewUserLog.visibility = View.VISIBLE
                callFragment(userLogFrg)
            }

            R.id.updateUserClick -> {
                hideView()
                viewUpdateUser.visibility = View.VISIBLE
                callFragment(updateUserFrg)
            }
        }
    }

    private lateinit var rootView: View

    override fun myFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.frg_user, parent, false)
        return rootView
    }

    override fun init() {

    }

    override fun initView() {
    }

    override fun postInitView() {
    }

    override fun addListener() {
        creteUserClick.setOnClickListener(this)
        userLogClick.setOnClickListener(this)
        updateUserClick.setOnClickListener(this)
    }

    override fun loadData() {
        AppLogger.e("replace frg")
        callFragment(UpdateUserFrg())
//        frg?.beginTransaction()?.add(R.id.frameContainer, createUserFrg, "1")?.commit();
//        frg?.beginTransaction()?.add(R.id.frameContainer, userLogFrg, "2")?.hide( UserLogFrg())
//            ?.commit();
    }

    private fun hideView() {
        viewUser.visibility = View.INVISIBLE
        viewUpdateUser.visibility = View.INVISIBLE
        viewUserLog.visibility = View.INVISIBLE
    }

    private fun callFragment(fragment: Fragment) {
        AppLogger.e("replace frg")
        val fm = fragmentManager
        val fragmentTransaction = fm?.beginTransaction()
        fragmentTransaction?.replace(R.id.frameUser, fragment)
//        fragmentTransaction?.addToBackStack(null);
        fragmentTransaction?.commit()
    }

    private fun contentFragment(fragmentToSelect: Fragment) {
        val fragmentTransaction = this.fragmentManager?.beginTransaction()
        if (this.fragmentManager?.fragments!!.contains(fragmentToSelect)) {
            // Iterate through all cached fragments.
            for (cachedFragment in this.fragmentManager!!.fragments) {
                if (cachedFragment !== fragmentToSelect) {
                    // Hide the fragments that are not the one being selected.
                    fragmentTransaction?.hide(cachedFragment)
                }
            }
            // Show the fragment that we want to be selected.
            fragmentTransaction?.show(fragmentToSelect)
        } else {
            // The fragment to be selected does not (yet) exist in the fragment manager, add it.
            fragmentTransaction?.add(R.id.frameUser, fragmentToSelect)
        }
        fragmentTransaction?.commit()
    }

    override fun onResume() {
        super.onResume()
    }

}
