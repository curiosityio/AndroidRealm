package com.curiosityio.androidrealm.manager

import android.content.Context
import android.preference.PreferenceManager
import com.curiosityio.androidrealm.config.AndroidRealmConfig
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.FieldAttribute
import java.util.*

object RealmInstanceManager {

    fun getTempInstance(): Realm = Realm.getInstance(getInMemoryConfiguration())
    fun getInstance(): Realm = Realm.getInstance(getConfiguration())

    private fun getInMemoryConfiguration(): RealmConfiguration {
        return RealmConfiguration.Builder()
                .name("in_memory.realm")
                .inMemory()
                .build()
    }

    private fun getConfiguration(): RealmConfiguration {
        return RealmConfiguration.Builder()
                .name("${AndroidRealmConfig.realmInstanceConfig?.getRealmInstanceName()}.realm")
                .schemaVersion(AndroidRealmConfig.migrationManager?.getCurrentSchemaVersion() ?: 0)
                .migration { dynamicRealm, oldVersion, newVersion ->
                    val schema = dynamicRealm.schema

                    for (i in oldVersion until newVersion) {
                        AndroidRealmConfig.migrationManager?.versionChange(i.toInt(), schema)
                    }
                }
                .build()
    }

}
