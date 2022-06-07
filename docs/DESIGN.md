# Bill Splitter App Design Document
## Architecture Description
- Framework:
  - Kotlin / Jetpack Compose
	  - Resources for Jetpack Compose
		  - [Jetpack Compose: State](https://www.youtube.com/watch?v=cDabx3SjuOY&list=PLQkwcJG4YTCSpJ2NLhDTHhi6XBNfk9WiC)
		  - [Creating Your First Jetpack Compose App - Android Jetpack Compose - Part 1](https://www.youtube.com/watch?v=mymWGMy9pYI)
		  - [Jetpack Compose Tutorial for Android: Getting Started](https://www.raywenderlich.com/15361077-jetpack-compose-tutorial-for-android-getting-started)
- Database/User Authentication:
  - Firebase is used to store user information and bill history
  - data is fetched from the Firebase database to display on the profile page

## Design Process Documentation
### Stage 1
- We sketched out how we wanted the app to look like as well as decide on the functionalities we wanted it to have.
![](https://i.imgur.com/V8EHFIO.jpg)
### Stage 2
- We decided to focus on the Bill Splitter UI and logic for the MVP and implemented the UIS using XML.
![](https://i.imgur.com/e5guprw.png)
![](https://i.imgur.com/yGtXbys.png)
### Stage 3
- We later switched over to Jetpack Compose due to its similarities to React and its intuitiveness. 
- We also added some bill splitting logic.
### Stage 4
- We added UI for the profile page to easily access the bill history's of each user
- We set up firebase to store user information, bill history, and authentication.
![](https://i.imgur.com/9iWOPY3.png)
![](https://i.imgur.com/9GOZUzE.png)
![](https://i.imgur.com/mUFvRng.png)

## Challenges
* Jetpack Compose vs XML 
  * We had initially decided as a group to use XML files to create our UI since that was what we were used to using in class. However, we later discovered that it might be much easier implementing the UI and logic using Jetpack Compose. While Jetpack Compose is more intuitive than XML, it still required learning from the entire team since no one had any prior experience with it. With Jetpack Compose we were able to decrease the amount of code needed to write and increase readability. We also struggled a little trying to fit all of our features within two distinct fragments. We wanted to minimize the number of fragments used to reduce the taskwork for ourselves but the bill splitting features have a lot of dependencies between fields so it took some brainstorming to come up with our current design.
