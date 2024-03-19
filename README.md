<p>
  <img src="https://github.com/Vikashgathala/mifos-mobile/assets/12993867/0fd88221-24b4-4f6c-b589-45322a544a28" />
</p>

<img height='175' src="https://user-images.githubusercontent.com/37406965/51083189-d5dc3a80-173b-11e9-8ca0-28015e0893ac.png" align="left" hspace="1" vspace="1">

# Mifos-Mobile Android Application for MifosX

An Android Application built on top of the MifosX Self-Service platform for end-user customers to view/transact on the accounts and loans they hold. Data visible to customers will be a sub-set of what staff can see. This is a native Android Application written in Kotlin.

## Notice

:warning: We are fully committed to implement [Jetpack Compose](https://developer.android.com/jetpack/compose) and moving ourself to support 
`kotlin multi-platform`. **If you are sending any PR regarding `XML changes` we will `not` consider at this moment but converting XML to jetpack compose are most welcome.** If you sending any PR regarding logical changes in Activity/Fragment you are most welcome. 

### Status

| Master | Development | Chat |
|------------|-----------------|-----------------|
| ![Mifos-Mobile CI[Master]](https://github.com/openMF/mifos-mobile/workflows/Workflow%20for%20master/development%20branches/badge.svg?branch=master) | ![Mifos-Mobile CI[Development]](https://github.com/openMF/mifos-mobile/workflows/Workflow%20for%20master/development%20branches/badge.svg?branch=development) |[![Join the chat at https://mifos.slack.com/](https://img.shields.io/badge/Join%20Our%20Community-Slack-blue)](https://mifos.slack.com/)|


## Join Us on Slack

Mifos boasts an active and vibrant contributor community, Please join us on [slack](https://join.slack.com/t/mifos/shared_invite/zt-2f4nr6tk3-ZJlHMi1lc0R19FFEHxdvng). Once you've joined the mifos slack community, please join the `#mifos-mobile` channel to engage with mifos-mobile development. If you encounter any difficulties joining our Slack channel, please don't hesitate to open an issue. This will allow us to assist you promptly or send you an invitation.



## How to Contribute

This is an OpenSource project and we would be happy to see new contributors. The issues should be raised via the GitHub issue tracker.
For Issue tracker guidelines please click <a href="https://github.com/openMF/mifos-mobile/blob/development/.github/ISSUE_TEMPLATE.md">here</a>. All fixes should be proposed via pull requests.
For pull request guidelines please click <a href="https://github.com/openMF/mifos-mobile/blob/development/CONTRIBUTING.md#pull-requests">here</a>. For commit style guidelines please click <a href="https://github.com/openMF/mifos-mobile/wiki/Commit-Style-Guide">here</a>.

### Branch Policy

We have the following branches :

* **development**
  All the contributions should be pushed to this branch. If you're making a contribution,
  you are supposed to make a pull request to _development_.
  Please make sure it passes a build check on Github Workflows CI.

  It is advisable to clone only the development branch using the following command:

  `git clone -b <branch> <remote_repo>`

  With Git 1.7.10 and later, add --single-branch to prevent fetching of all branches. Example, with development branch:

  `git clone -b development --single-branch https://github.com/username/mifos-mobile.git`

* **ui-redesign**
  All the contributions related to redesigning of the app should be pushed to this branch. If you're making a contribution,
  you are supposed to make a pull request to _ui-redesign_.
  Please make sure it passes a build check on Github Workflows CI.

  This branch will be merged with the development branch once the redesign is complete.

* **master**
  The master branch contains all the stable and bug-free working code. The development branch once complete will be merged with this branch.

### Demo credentials
Fineract Instance: gsoc.mifos.community

Username: `mifos`

Password: `password`

### Instruction to get the latest APK

To get the latest apk of master/development branch from Github Artifacts, follow these steps:
1. Go to to the [Actions](https://github.com/openMF/mifos-mobile/actions?query=workflow%3A%22Workflow+for+master%2Fdevelopment+branches%22+event%3Apush) tab of this repository.
2. Select the latest workflow for master/development branch.
3. Click on hyperlink 'mifos-mobile' in Artifacts section.
4. Extract the downloaded file and get the apk.

## Development Setup

Before you begin, you should have already downloaded the Android Studio SDK and set it up correctly. You can find a guide on how to do this here: [Setting up Android Studio](http://developer.android.com/sdk/installing/index.html?pkg=studio).

## Building the Code

1. Clone the repository using HTTP: git clone https://github.com/openMF/mifos-mobile.git

2. Open Android Studio.

3. Click on 'Open an existing Android Studio project'

4. Browse to the directory where you cloned the mifos-mobile repo and click OK.

5. Let Android Studio import the project.

6. Build the application in your device by clicking run button.

## Wiki

View the [wiki](https://github.com/openMF/self-service-app/wiki) to see pages that provide details on the project.

## Specification

See the [requirements](https://github.com/openMF/self-service-app/wiki/Design-&-Requirements) for an initial design mockup and documentation on the Fineract API.

## PaymentHub Usecases

For Payment Hub usecases, check this [documentation](https://mifos.gitbook.io/docs/payment-hub-ee/overview/payment-hub-apis). Mifos Mobile utilises medium connector of Payment Hub.

## Note

The UI design is currently being revamped. New design can be found [here](https://docs.google.com/presentation/d/1yFR19vGlKW-amxzGms8TgPzd1jWkrALPFcaC85EyYpw/edit#slide=id.g6c6ccd991d_0_42)

## Contributors

Special thanks to the incredible code contributors who continue to drive this project forward.

<a href="https://github.com/openMF/mifos-mobile/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=openMF/mifos-mobile" />
</a>

