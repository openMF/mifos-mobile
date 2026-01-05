# iOS Platform

## Module: cmp-ios

> iOS platform target using CocoaPods integration

---

## Setup

1. Install CocoaPods dependencies:
   ```bash
   cd cmp-ios
   pod install
   ```

2. Open Xcode workspace:
   ```bash
   open iosApp.xcworkspace
   ```

3. Build and run in Xcode

---

## Key Files

| File | Purpose |
|------|---------|
| `cmp-ios/Podfile` | CocoaPods dependencies |
| `cmp-ios/iosApp/` | iOS app source |
| `cmp-ios/iosApp/Info.plist` | App configuration |
| `cmp-ios/iosApp/ContentView.swift` | Main UI entry |
| `cmp-shared/` | Shared KMP framework |

---

## Build Commands

| Command | Action |
|---------|--------|
| `pod install` | Install dependencies |
| `pod update` | Update dependencies |
| Xcode Build (Cmd+B) | Build app |
| Xcode Run (Cmd+R) | Run on simulator/device |

### Terminal Build

```bash
xcodebuild -workspace iosApp.xcworkspace \
           -scheme iosApp \
           -sdk iphonesimulator \
           -configuration Debug \
           build
```

---

## KMP Framework Integration

### Shared Framework

```ruby
# Podfile
target 'iosApp' do
  use_frameworks!
  pod 'cmp_shared', :path => '../cmp-shared'
end
```

### Using Shared Code

```swift
import cmp_shared

struct ContentView: View {
    var body: some View {
        ComposeView()
    }
}
```

---

## Xcode Project Structure

```
iosApp/
├── iosApp.xcodeproj/
├── iosApp.xcworkspace/
├── iosApp/
│   ├── Assets.xcassets/
│   ├── ContentView.swift
│   ├── Info.plist
│   └── iosApp.swift
└── Podfile
```

---

## iOS-Specific Features

| Feature | Implementation |
|---------|----------------|
| Biometrics | LocalAuthentication |
| Push Notifications | APNs |
| Deep Links | URL Schemes |

---

## Requirements

| Requirement | Version |
|-------------|---------|
| iOS Deployment Target | 14.0+ |
| Xcode | 15.0+ |
| CocoaPods | 1.12+ |

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Pod install fails | Run `pod repo update` first |
| Framework not found | Clean build folder (Cmd+Shift+K) |
| Simulator issues | Reset simulator content |

---

## Related

- [LAYER_STATUS.md](../LAYER_STATUS.md) - Platform overview
- [LAYER_GUIDE.md](../LAYER_GUIDE.md) - Architecture patterns
