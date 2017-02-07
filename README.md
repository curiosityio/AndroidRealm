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
compile 'com.github.curiosityio:android-realm-core:0.1.0'
```
