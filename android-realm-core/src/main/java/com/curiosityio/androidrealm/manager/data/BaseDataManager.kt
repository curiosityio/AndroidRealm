package com.curiosityio.androidrealm.manager.data

import android.os.Looper
import io.realm.Realm

abstract class BaseDataManager {

    lateinit var realm: Realm

    @Throws(RuntimeException::class)
    protected fun performRealmTransaction(changeData: Realm.Transaction) {
        if (Looper.getMainLooper().thread == Thread.currentThread()) {
            throw RuntimeException("Cannot perform transaction from UI thread.")
        }
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction(changeData)
        if (!realm.isClosed) realm.close()
    }

    protected fun performRealmQuery(queryData: (realm: Realm, done: () -> Unit) -> Unit) {
        val realm = Realm.getDefaultInstance()

        queryData(realm, {
            if (!realm.isClosed) realm.close()
        })
    }

}