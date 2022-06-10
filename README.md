# Bill Splitter

## Purpose
BillSplitter is an app that allows users to input the bill total along with each individual's amount spent at a restaurant and the app will manually split the tax/tip evenly among the people. This will help simplify a lot of the calculation processes that occur during bill splitting.

## Details and User Roles
Users can create an account on the register page by inputting an email and password which will then be stored in our firebase database. All future bills created by this user will be linked to their email in firebase and will be displayed on their profile page under bill history. Users can also add other registered emails to the bill which will then allow them to collaborate on the same bill from their respective devices.

## Repository Structure

Our [DESIGN](https://github.com/justinsurmani/BillSplitter/blob/main/docs/DESIGN.md) document and [MANUAL](https://github.com/justinsurmani/BillSplitter/blob/main/docs/MANUAL.md) are located in the [docs](https://github.com/justinsurmani/BillSplitter/tree/main/docs) folder.

In the [package](https://github.com/justinsurmani/BillSplitter/tree/main/app/src/main/java/edu/ucsb/cs/cs184/group9/billsplitter) we have three folders:
- dao
- repository
- ui

**dao** package contains data access objects used to represent data objects retrieved from firebase. They are immutable and are used in ViewModel to represent data.

**repository** package contains Repository layers to interact with the database layer. They are essentially methods to create/read/update/delete data objects like bills and users.

**ui** package contains Jetpack UI Composables. Actual UI pages follow a pattern where ViewModel is used as a source of truth for data and interactions wtih data, screen, represents the high level structure where states are hoisted, and content/composables are the individual reusable UI components that make up the entire content.

The MainActivity contains code to set up a navigation graph/navcontroller and it's where the UI content is located.

## Team Members
Angela Zhao, Charity Hsu, Doris Wei, Justin Surmani, Justin Vo

## Tech Stack

Framework: Kotlin & Jetpack Compose

Database: Firebase


