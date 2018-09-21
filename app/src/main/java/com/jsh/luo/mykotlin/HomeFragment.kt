package com.jsh.luo.mykotlin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment(){

    private var mTitle : String? = null

    companion object {
        fun getInstance(title : String) : HomeFragment {
            val fragment = HomeFragment()
            fragment.mTitle = title
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_home,container,false)

        return view
    }
}
