package com.jsh.luo.mykotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by luo on 2018/9/18.
 */
class MainActivity : AppCompatActivity(){

    private val mTabTitle = arrayOf("首页","推荐","我的")

    private val mTabEntities = ArrayList<CustomTabEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        SystemBarUtils.setFitsSystemWindows(this,true)
        SystemBarUtils().addStatusViewWithColor(this,resources.getColor(R.color.colorPrimary))

        (0 until mTabTitle.size).mapTo(mTabEntities){TabEntity(mTabTitle[it],R.mipmap.ic_launcher,R.mipmap.ic_launcher)}
        tab_layout.setTabData(mTabEntities)
        tab_layout.setOnTabSelectListener(object : OnTabSelectListener{
            override fun onTabSelect(position: Int) {

            }

            override fun onTabReselect(position: Int) {

            }
        })
    }
}