package com.curiosityio.androidrealm.manager.config

import android.content.Context
import com.curiosityio.androidrealm.config.AndroidRealmConfig
import com.curiosityio.androidrealm.config.RealmInstanceConfig
import com.curiosityio.androidrealm.config.RealmMigrationManager
import com.curiosityio.androidrealm.manager.RealmInstanceManager
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
class AndroidRealmConfigTest {

    @Test(expected = RuntimeException::class)
    @Throws(Exception::class)
    fun mustSetAllConfigOptions() {
        AndroidRealmConfig.Builder().build(Mockito.mock(Context::class.java))
    }

    @Test(expected = RuntimeException::class)
    @Throws(Exception::class)
    fun onlySetMigrationManager() {
        AndroidRealmConfig.Builder()
                .setMigrationManager(Mockito.mock(RealmMigrationManager::class.java))
                .build(Mockito.mock(Context::class.java))
    }

    @Test(expected = RuntimeException::class)
    @Throws(Exception::class)
    fun onlySetRealmInstanceManager() {
        AndroidRealmConfig.Builder()
                .setRealmInstanceConfig(Mockito.mock(RealmInstanceConfig::class.java))
                .build(Mockito.mock(Context::class.java))
    }

    @Test(expected = RuntimeException::class)
    @Throws(Exception::class)
    fun successfullyBuildAndroidRealm() {
        val migrationManager = Mockito.mock(RealmMigrationManager::class.java)
        val realmInstanceConfig = Mockito.mock(RealmInstanceConfig::class.java)

        AndroidRealmConfig.Builder()
                .setRealmInstanceConfig(realmInstanceConfig)
                .setMigrationManager(migrationManager)
                .build(Mockito.mock(Context::class.java))

        assertEquals(migrationManager, AndroidRealmConfig.migrationManager)
        assertEquals(realmInstanceConfig, AndroidRealmConfig.realmInstanceConfig)
    }

}
