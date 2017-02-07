package com.curiosityio.androidrealmcorerx.extensions

import io.realm.RealmObject
import io.realm.RealmQuery
import rx.Observable

fun <MODEL: RealmObject> RealmQuery<MODEL>.findFirstOrNullAsyncObservable(): Observable<MODEL?> {
    return Observable.create { subscriber ->
        if (count() <= 0) {
            subscriber.onNext(null)
        } else {
            findFirstAsync().asObservable<MODEL>().subscribe { model ->
                if (model.isLoaded) {
                    subscriber.onNext(model)
                }
            }
        }
    }
}