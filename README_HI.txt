UNLOCK VIDEO PLAYER – BUILD NOTES

Is project mein aapka unlock.mp4 bundled hai. App Accessibility Service ka use karke unlock ke baad 10 second ka non-touchable video overlay dikhane ki koshish karta hai. Isse neeche wali app par touch pass-through hona chahiye.

IMPORTANT:
- Android/OriginOS background restrictions ki wajah se har device/firmware par unlock trigger guaranteed nahi hai.
- App install karne ke baad app kholen > Enable Unlock Detection > Accessibility settings > Unlock Video Player ON.
- iQOO/OriginOS mein app battery restriction ko Unrestricted/Allow background activity par set karna pad sakta hai.
- Debug APK abhi build ya physical device par test nahi hua hai.

GitHub Actions workflow .github/workflows/build-apk.yml APK build karta hai. Project ko GitHub repository mein push karke Actions tab se Build Android APK run karein. Artifact mein app-debug.apk milega.
