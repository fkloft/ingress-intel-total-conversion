default:
	./build.py mobile && adb install -r build/mobile/IITC_Mobile-debug.apk && adb shell am start -n com.cradle.iitc_mobile/com.cradle.iitc_mobile.IITC_Mobile

