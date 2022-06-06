# Bill Splitter App Design Document

## Architecture Description
- Framework:
  - Kotlin / Jetpack Compose
- Database/User Authentication:
  - Firebase is used to store user information and bill history
  - data is fetched from the Firebase database to display on the profile page

## Design Process Documentation
### Stage 1
- We decided to focus on the Bill Splitter UI and logic for the MVP and decided to use Kotlin and XML initially. 
### Stage 2
- We implemented the UIs for our home page and bill splitting page using XML but later switched over to Jetpack Compose due to its similarities to React and its intuitiveness. 
- We also added some bill splitting logic.
### Stage 3
- We added UI for the profile page to easily access the bill history's of each user
- We set up firebase to store user information, bill history, and authentication.
