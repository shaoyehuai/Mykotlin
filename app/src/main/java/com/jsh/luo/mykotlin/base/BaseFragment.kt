package com.jsh.luo.mykotlin.base

import android.support.v4.app.Fragment
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.annotation.NonNull
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment : Fragment() {

    /**
     * 所属Activity
     */
    private val activity: Activity? = null

    private var rootView : View? = null

    private var isFirstVisible = false//真正要显示的View是否已经被初始化（正常加载）
    private var isFragmentVisible = false
    private var isReuseView = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVariable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(rootView == null){
            // 强制竖屏显示
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val layoutResId = getCreateViewLayoutId()
            if(layoutResId > 0){
                rootView = inflater.inflate(layoutResId,container,false)
            }
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(rootView == null){

        }
        initView(rootView)
    }

    //setUserVisibleHint()在Fragment创建时会先被调用一次，传入isVisibleToUser = false
    //如果当前Fragment可见，那么setUserVisibleHint()会再次被调用一次，传入isVisibleToUser = true
    //如果Fragment从可见->不可见，那么setUserVisibleHint()也会被调用，传入isVisibleToUser = false
    //总结：setUserVisibleHint()除了Fragment的可见状态发生变化时会被回调外，在new Fragment()时也会被回调
    //如果我们需要在 Fragment 可见与不可见时干点事，用这个的话就会有多余的回调了，那么就需要重新封装一个
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(rootView == null)
            return

        if(isFirstVisible && isVisibleToUser){
            onFragmentFirstVisible()
            isFirstVisible = false
        }

        if(isVisibleToUser){
            onFragmentVisibleChange(true)
            isFragmentVisible = true
            return
        }

        if(isFragmentVisible){
            isFragmentVisible = false
            onFragmentVisibleChange(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        initVariable()
    }

    abstract fun getCreateViewLayoutId() : Int

    abstract fun initView(rootView : View?)

    /**
     * 跳转到指定的Activity
     *
     * @param extraName      要传递的值的键名称
     * @param extraValue     要传递的int类型值
     * @param targetActivity 要跳转的目标Activity
     */
    fun startActivity(@NonNull extraName : String, @NonNull extraValue : Int, @NonNull targetActivity :Class<*>) {
        if (TextUtils.isEmpty(extraName)) {
            throw NullPointerException ("传递的值的键名称为null或空")
        }
        val intent = Intent(activity, targetActivity)
        intent.putExtra(extraName, extraValue)
        startActivity(intent)
    }

    private fun initVariable(){
        isFragmentVisible = false
        isFirstVisible = true
        rootView = null
        isReuseView = true
    }

//    ---------fragment lazy load-------

    /**
     * 在fragment首次可见时回调，可在这里进行加载数据，保证只在第一次打开Fragment时才会加载数据，
     * 这样就可以防止每次进入都重复加载数据
     * 该方法会在 onFragmentVisibleChange() 之前调用，所以第一次打开时，可以用一个全局变量表示数据下载状态，
     * 然后在该方法内将状态设置为下载状态，接着去执行下载的任务
     * 最后在 onFragmentVisibleChange() 里根据数据下载状态来控制下载进度ui控件的显示与隐藏
     */
    protected fun onFragmentFirstVisible(){

    }


    /**
     * 去除setUserVisibleHint()多余的回调场景，保证只有当fragment可见状态发生变化时才回调
     * 回调时机在view创建完后，所以支持ui操作，解决在setUserVisibleHint()里进行ui操作有可能报null异常的问题
     *
     * 可在该回调方法里进行一些ui显示与隐藏，比如加载框的显示和隐藏
     * */
    protected fun onFragmentVisibleChange(isVisible : Boolean){

    }
}