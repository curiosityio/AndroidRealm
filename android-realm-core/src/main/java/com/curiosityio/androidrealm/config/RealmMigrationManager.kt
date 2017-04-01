package com.curiosityio.androidrealm.config

import io.realm.RealmSchema

interface RealmMigrationManager {
    fun getCurrentSchemaVersion(): Long
    fun versionChange(versionToMigrateTo: Int, schema: RealmSchema)
}
