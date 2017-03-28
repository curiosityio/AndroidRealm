package com.curiosityio.config

class AndroidRealmConfig {

    companion object {
        var realmInstanceConfig: RealmInstanceConfig = DefaultRealmInstanceConfig()
        var migrationManager: RealmMigrationManager? = null

        fun overrideRealmInstanceConfig(realmInstanceConfig: RealmInstanceConfig) {
            this.realmInstanceConfig = realmInstanceConfig
        }

        fun setRealmMigrationManager(realmMigrationManager: RealmMigrationManager) {
            this.migrationManager = realmMigrationManager
        }
    }

}