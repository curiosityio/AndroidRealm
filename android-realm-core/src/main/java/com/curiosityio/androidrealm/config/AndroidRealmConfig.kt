package com.curiosityio.androidrealm.config

import android.content.Context
import io.realm.Realm

class AndroidRealmConfig {

    companion object {
        var realmInstanceConfig: RealmInstanceConfig? = null
        var migrationManager: RealmMigrationManager? = null
    }

    class Builder {

        fun setRealmInstanceConfig(realmInstanceConfig: RealmInstanceConfig): Builder {
            AndroidRealmConfig.realmInstanceConfig = realmInstanceConfig
            return this
        }

        fun setMigrationManager(migrationManager: RealmMigrationManager): Builder {
            AndroidRealmConfig.migrationManager = migrationManager
            return this
        }

        fun build(context: Context) {
            if (AndroidRealmConfig.realmInstanceConfig == null ||
                    AndroidRealmConfig.migrationManager == null) {
                throw RuntimeException("You did not fully initialize AndroidRealm. In the AndroidRealmConfig.Builder(), make sure to set all of the config objects.")
            } else {
                Realm.init(context)
            }
        }

    }

}