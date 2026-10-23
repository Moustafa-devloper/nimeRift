# Feature Status

## Completed
- Java-only Android project structure created under `app/src/main/java/com/animrift/app`
- Arabic-first UI with English fallback and RTL-ready layout patterns
- Home catalog with search, genre filtering, and sample anime entries
- Local favorite toggling and history storage using SQLite and SharedPreferences
- Native video playback support with local file selection and direct URL validation
- Clear history action and remove-favorite behavior via list-item toggle
- Documentation of dependencies and manual build limitations

## Partial
- Advanced subtitle rendering is documented but not yet fully integrated in the app UI
- Cover art placeholders and richer media metadata are scaffolded but not full remote API integration
- Download manager integration is documented as a future-safe capability, not yet wired into UI

## Planned
- Source-provider API adapters for authorized metadata sources
- Secure URL validation and error handling for remote streams
- Download progress UI with Android `DownloadManager`
- Full archive or library-based subtitle support for advanced formats
- Low-memory optimization pass across all screens and adapters
- Real API integration based on a legitimate source and user authorization

## Important note
This is a first working Java/XML source tree, not a claim of a verified built APK. The code is intentionally conservative and framework-first to support low-end Android devices.
