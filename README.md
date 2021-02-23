# Genies Android Avatar API Library Sample
This repository contains the [Genies Avatar API library](https://github.com/geniesinc/GeniesApiLibrary-Android/tree/master/avatar-api-library) and a [sample app](https://github.com/geniesinc/GeniesApiLibrary-Android/tree/master/avatar-api-sampleapp) showing how the library can be used

## Avatar API library

### Library integration - gradle
 
1. [Apply for a Partner Account and get your `authToken`](https://geniesinc.github.io/#step-one-apply-for-a-partner-account)
2.  Open gradle.properties file
   - Add
   ```
   authToken=YOUR_AUTH_TOKEN
   ```
2. Open build.gradle (project) file
   - Add the following to allprojects / repositories
```
  maven {
            url 'https://jitpack.io'
            credentials { username authToken }
        }
```
3. Open build.gradle (Module: app) file
   - Add to dependencies
   ```
   implementation 'com.github.geniesinc:android-genies-api:0.0.5:dev@aar'
   ```

#### Avatar API client initialization:

```kotlin
private val avatarApi: AvatarAPIClient = AvatarAPIClient(apiKey)
```

#### Avatar API get assets 
```kotlin
avatarApi.getAssetsForCategory(idToken, CATEGORY_HATS)
```

#### Avatar API get user closet 
```kotlin
 avatarApi.getClosetItems(idToken, userId)
```

#### Avatar API deposit asset to closet
```kotlin
avatarApi.depositAsset(idToken, userId, assetId)
```

##### Avatar API withdraw asset from closet
```kotlin
avatarApi.withdrawAsset(idToken, userId, assetId, assetInstanceId)
```
## Sample
To use the sample app you need to get an `authToken` and `clientId` by [applying for a partner account](https://geniesinc.github.io/#step-one-apply-for-a-partner-account)
