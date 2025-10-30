# TODO: Add Local Databases to MoodTracker App

## Step 1: Add Room Dependencies
- [x] Update `gradle/libs.versions.toml` to include Room versions.
- [x] Update `app/build.gradle.kts` to add Room dependencies.

## Step 2: Create Database Entities
- [x] Create `app/src/main/java/com/example/moodtracker/data/UserEntity.kt` for LogInCredentials (username, password, firstName, lastName).
- [x] Create `app/src/main/java/com/example/moodtracker/data/MoodEntity.kt` for Info (mood, date, username).

## Step 3: Create DAOs
- [x] Create `app/src/main/java/com/example/moodtracker/data/UserDao.kt` for user operations (insert, get by username).
- [x] Create `app/src/main/java/com/example/moodtracker/data/MoodDao.kt` for mood operations (insert, get by username).

## Step 4: Create AppDatabase
- [x] Create `app/src/main/java/com/example/moodtracker/data/AppDatabase.kt` extending RoomDatabase.

## Step 5: Update Activities and Repository
- [ ] Update `LoginActivity.kt` to authenticate via database query.
- [ ] Update `MoodHistoryRepository.kt` to use database for storing and retrieving mood history.
- [ ] Ensure username is passed through activities (from Login to Home to AssessMood to AddDetails).
- [x] Update `AddDetailsActivity.kt` to save mood with username.

## Step 6: Pre-populate Data
- [x] Pre-populate LogInCredentials with sample data (e.g., insert users on first app launch).

## Step 7: Test Integration
- [ ] Build and run the app to test login and mood tracking.
- [ ] Verify data persistence across app restarts.
