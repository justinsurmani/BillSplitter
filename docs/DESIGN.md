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

## User Interface and User Experience
### UI Design

- Default Page: User can select the amount of people splitting the bill and enter the subtotal and tax before creating the group
![](https://i.imgur.com/9iWOPY3.png)

- Bill Splitting Page: Users can split the bill accordingly and adjust the tip amount![](https://i.imgur.com/9GOZUzE.png)

- Profile page: User can view its information and bill history
![]()

### User Experience
- Users can use firebase authentication to securely log in and view their profile page
![]()

- User can adjust the prices and number of users for each item and each users total will be updated correspondingly in the bottom section.
![](https://i.imgur.com/vwtbDoI.gif)
