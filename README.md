# AnimeRift

AnimeRift is a Java-only Android application source tree for a lightweight anime catalog and playback experience designed for low-end devices, Android 6.0+ and Android 11/32-bit ARM compatibility.

This repository intentionally avoids Gradle, Maven, Kotlin, Compose, and generated build scripts. The project is structured so it can be imported into an Android Java project in Android Studio or another Java-compatible IDE and built with the Android SDK manually.

## What is included
- Arabic-first UI with English fallback strings
- Local anime catalog screen with search and genre filters
- Anime detail flow and sample catalog data
- Local favorites and viewing history storage via SharedPreferences and SQLite
- Local video playback using Android's native `VideoView`
- Direct URL validation and local file selection via Android Storage Access Framework
- Documentation for advanced player/subtitle dependencies without inventing unsupported claims

## Important limitations
- This source tree is not claiming a verified Gradle build in the current environment.
- A full Android SDK and proper Java Android project setup are still required to build the app.
- Advanced formats or DRM-protected media are intentionally documented as out of scope unless a compatible external library is added manually.

## Documentation
- `LIBRARIES.txt` — manual library catalog and dependency notes
- `PROJECT_STRUCTURE.txt` — module-by-module structure explanation
- `docs/BUILD_GUIDE.md` — manual build guidance without Gradle files
- `docs/FEATURE_STATUS.md` — implemented and planned features

## License
This project retains the repository's existing license in `LICENSE`.
