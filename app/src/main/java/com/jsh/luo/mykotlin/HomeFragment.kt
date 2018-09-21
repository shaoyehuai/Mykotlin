package com.jsh.luo.mykotlin

import android.support.v4.app.Fragment


class HomeFragment : Fragment(){

    private var mTitle : String? = null

    companion object {
        fun getInstance(title : String) : HomeFragment {
            val fragment = HomeFragment()
            fragment.mTitle = title
            return fragment
        }
    }
}
