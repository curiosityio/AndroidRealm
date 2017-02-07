package com.curiosityio.androidrealmcorerx.extensions

import android.os.Looper
import com.curiosityio.androidrealm.manager.data.BaseDataManager
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmQuery
import rx.Completable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

@Throws(RuntimeException::class)
fun BaseDataManager.performRealmTransactionCompletable(changeData: Realm.Transaction): Completable {
    return Completable.create { subscriber ->
        if (Looper.getMainLooper().thread == Thread.currentThread()) {
            throw RuntimeException("Cannot perform transaction from UI thread.")
        }
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction(changeData)
        if (!realm.isClosed) realm.close()

        subscriber.onCompleted()
    }.subscribeOn(Schedulers.io())
}

fun <MODEL> BaseDataManager.getRealmDataCreatedListener(queryData: (Realm) -> RealmQuery<MODEL>): Completable where MODEL : RealmObject {
    return Completable.create { subscriber ->
        queryData(realm).findAllAsync().asObservable()
                .subscribe { data ->
                    if (data.count() > 0) {
                        subscriber.onCompleted()
                    }
                }
    }.subscribeOn(AndroidSchedulers.mainThread())
}