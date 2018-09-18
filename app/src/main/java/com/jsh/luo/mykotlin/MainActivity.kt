package com.jsh.luo.mykotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.flyco.tablayout.listener.CustomTabEntity

/**
 * Created by luo on 2018/9/18.
 */
class MainActivity : AppCompatActivity(){

    private val mTabTitle = arrayOf("首页","推荐","我的")

    private val mTabEntities = ArrayList<CustomTabEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (0 until mTabTitle.size).mapTo(mTabEntities){TabE}
    }
}