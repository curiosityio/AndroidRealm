# AndroidRealm

Add Realm.io to your Android app along with useful utilities. 

# Install:

* Install realm-java to your project.

* Install libs to your project via gradle.

Add this to your root build.gradle at the end of repositories:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Then, install as many of the modules as you wish.

Core:

```
compile 'com.github.curiosityio.AndroidRealm:android-realm-core:0.1.0'
```

Core with some RxJava Observable goodness (depends on core, just adds Rx functionality to it.):

```
compile 'com.github.curiosityio.AndroidRealm:android-realm-core-rx:0.1.0'
```

RealmRecyclerViewAdapter (depends on core):
*Note: I do not advise using anymore. Use [official realm adapters](https://github.com/realm/realm-android-adapters) v2.+ instead as they now support.*

```
compile 'com.github.curiosityio.AndroidRealm:android-realm-recyclerview:0.1.0'
```

# Configure

```
AndroidRealmConfig.overrideRealmInstanceConfig() # Name realm instances. Allows you to easily create multiple realms.
AndroidRealmConfig.setRealmMigrationManager() # Used to easily migrate realm instances when versions get bumped.
```
