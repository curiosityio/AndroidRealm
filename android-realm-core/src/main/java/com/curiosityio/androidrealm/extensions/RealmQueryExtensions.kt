package com.curiosityio.androidrealm.extensions

import io.realm.RealmObject
import io.realm.RealmQuery

fun <MODEL: RealmObject> RealmQuery<MODEL>.findFirstOrNull(): MODEL? {
    if (count() <= 0) {
        return null
    } else {
        return findFirst()
    }
}

fun <MODEL: RealmObject> RealmQuery<MODEL>.findFirstOrNullAsync(): MODEL? {
    if (count() <= 0) {
        return null
    } else {
        return findFirstAsync()
    }
}

fun <MODEL: RealmObject> RealmQuery<MODEL>.doesExist(): Boolean {
    return count() > 0
}