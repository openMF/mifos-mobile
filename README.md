<img height='175' src="https://user-images.githubusercontent.com/37406965/51083189-d5dc3a80-173b-11e9-8ca0-28015e0893ac.png" align="left" hspace="1" vspace="1">

# Mifos-Mobile Android Application for MifosX

An Android Application built on top of the MifosX Self-Service platform for end-user customers to view/transact on the accounts and loans they hold. Data visible to customers will be a sub-set of what staff can see. This is a native Android Application written in Java with POJO classes in Kotlin.

### Status

[![Join the chat at https://gitter.im/openMF/self-service-app](https://badges.gitter.im/openMF/self-service-app.svg)](https://gitter.im/openMF/self-service-app?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://travis-ci.org/openMF/mifos-mobile.svg?branch=development)](https://travis-ci.org/openMF/mifos-mobile)


## Screenshots

<p>
  <img src="https://user-images.githubusercontent.com/24931732/48102172-b0ccc800-e1de-11e8-9fb9-17c41234636e.png" />
</p>

## How to Contribute

This is an OpenSource project and we would be happy to see new contributors. The issues should be raised via the GitHub issue tracker.
For Issue tracker guidelines please click <a href="https://github.com/openMF/mifos-mobile/blob/development/.github/ISSUE_TEMPLATE.md">here</a>. All fixes should be proposed via pull requests.
For pull request guidelines please click <a href="https://github.com/openMF/mifos-mobile/blob/development/.github/CONTRIBUTING.md#pull-requests">here</a>. For commit style guidelines please click <a href="https://github.com/openMF/mifos-mobile/wiki/Commit-Style-Guide">here</a>.

### Branch Policy

We have the following branches :

 * **development**
     All the contributions should be pushed to this branch. If you're making a contribution,
     you are supposed to make a pull request to _development_.
     Please make sure it passes a build check on Travis.

     It is advisable to clone only the development branch using the following command:

    `git clone -b <branch> <remote_repo>`

    With Git 1.7.10 and later, add --single-branch to prevent fetching of all branches. Example, with development branch:

    `git clone -b development --single-branch https://github.com/username/mifos-mobile.git`

 * **master**
   The master branch contains all the stable and bug-free working code. The development branch once complete will be merged with this branch.

## Development Setup

Before you begin, you should have already downloaded the Android Studio SDK and set it up correctly. You can find a guide on how to do this here: [Setting up Android Studio](http://developer.android.com/sdk/installing/index.html?pkg=studio).

## Building the Code

1. Clone the repository using HTTP: git clone https://github.com/openMF/mifos-mobile.git

2. Open Android Studio.

3. Click on 'Open an existing Android Studio project'

4. Browse to the directory where you cloned the mobile-wallet repo and click OK.

5. Let Android Studio import the project.

6. Build the application in your device by clicking run button.

## Wiki

View the [wiki](https://github.com/openMF/self-service-app/wiki) to see pages that provide details on the project.

## Specification

See the [requirements](https://github.com/openMF/self-service-app/wiki/Design-&-Requirements) for an initial design mockup and documentation on the Fineract API.

