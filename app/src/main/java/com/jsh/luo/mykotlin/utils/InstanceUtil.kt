package com.jsh.luo.mykotlin.utils

//import com.apt.InstanceFactory

object InstanceUtil {


    /**
     * 通过实例工厂去实例化相应类
     *
     * @param <T> 返回实例的泛型类型
     * @return T
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getInstance(clazz: Class<*>): T? {
        try {
//            return InstanceFactory.create(clazz) as T
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
