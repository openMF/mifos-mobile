# Contribute to Self Service Android Application

This guide details how to use issues and pull requests to improve Self Service Android Application.

## Building Self-Service Android Application.

There are two steps, you have to follow when you are building the self-service android application.

1. Setting up the [Android Studio](https://github.com/openMF/self-service-app/wiki/Android-Studio-Setup)

2. [Building up Code](https://github.com/openMF/self-service-app/wiki/Building-up-Code) on Android Studio.

## Configure remotes

When a repository is cloned, it has a default remote called `origin` that points to your fork on GitHub, not the original repository it was forked from. To keep track of the original repository, you should add another remote named `upstream`:

The easiest way is to use the https URL:

`git remote add upstream https://github.com/openMF/self-service-app.git`

or if you have ssh set up you can use that URL instead:

`git remote add upstream git@github.com:openMF/self-service-app.git`

## Commit Style Guide

 Mifos Self-Service Android application have a set of [Commit Style Guidelines](https://github.com/openMF/self-service-app/wiki/Commit-Style-Guide). we strictly following this guideline to track every change, any bug fixes, any enhancement, and any new feature addition. we are suggesting you, please follow these guidelines to help us managing every commit.

## Issue tracker

The [issue tracker](https://github.com/openMF/self-service-app/issues) is only for obvious bugs, misbehavior, & feature requests in the latest stable or development release of Self-Service Android Application. When submitting an issue please conform to the issue submission guidelines listed below. Not all issues will be addressed and your issue is more likely to be addressed if you submit a pull request which partially or fully addresses the issue.

### Issue tracker guidelines

**[Search](https://github.com/openMF/self-service-app/search?q=&ref=cmdform&type=Issues)** for similar entries before submitting your own, there's a good chance somebody else had the same issue or feature request. Show your support with `+1:` and/or join the discussion. Please submit issues in the following format and feature requests in a similar format:

1. **Summary:** Summarize your issue in one sentence (what goes wrong, what did you expect to happen)
2. **Steps to reproduce:** How can we reproduce the issue?
3. **Expected behavior:** What did you expect the app to do?
4. **Observed behavior:** What did you see instead?  Describe your issue in detail here.
5. **Device and Android version:** What make and model device (e.g., Samsung Galaxy S3) did you encounter this on?  What Android version (e.g., Android 4.0 Ice Cream Sandwich) are you running?  Is it the stock version from the manufacturer or a custom ROM?
5. **Screenshots:** Can be created by pressing the Volume Down and Power Button at the same time on Android 4.0 and higher.
6. **Possible fixes**: If you can, link to the line of code that might be responsible for the problem.

## Pull requests

We welcome pull requests with fixes and improvements to Self-Service Android Application code, tests, and/or documentation. The features we would really like a pull request for are [open issues with the enhancements label](https://github.com/openMF/self-service-app/issues?labels=enhancement&page=1&state=open).

### Pull request guidelines

If you can, please submit a pull request with the fix or improvements including tests.

* Fork the project on GitHub 
* Create a feature branch
* Write tests and code
* Run the CheckStyle, PMD, Findbugs code analysis tools with the `gradlew check` to make sure you have written quality code.
* If you have multiple commits please combine them into one commit by squashing them.  See [this article](http://eli.thegreenplace.net/2014/02/19/squashing-github-pull-requests-into-a-single-commit) and [this Git documentation](http://git-scm.com/book/en/Git-Tools-Rewriting-History#Squashing-Commits) for instructions.
* Please follow the commit message [guidelines](https://github.com/openMF/self-service-app/wiki/Commit-Style-Guide) before making PR.
* Push the commit to your fork
* Submit a pull request with a motive for your change and the method you used to achieve it with the `development` branch.

We will accept pull requests if:

* The code has proper tests and all tests pass (or it is a test exposing a failure in existing code)
* It doesn't break any existing functionality
* It's quality code that conforms to standard style guides and best practices
* The description includes a motive for your change and the method you used to achieve it
* It is not a catch-all pull request but rather fixes a specific issue or implements a specific feature
* If it makes changes to the UI the pull request should include screenshots
* It is a single commit (please use `git rebase -i` to squash commits)

### Best Practices for reporting or requesting for Issues/Enhancements:
  - Follow the Issue Template while creating the issue.
  - Include Screenshots if any (especially for UI related issues)
  - For UI enhancements or workflows, include mockups to get a clear idea.

### Best Practices for assigning an issue:
- If you would like to work on an issue, inform in the issue ticket by commenting on it.
- Please be sure that you are able to reproduce the issue, before working on it. If not, please ask for clarification by commenting or asking the issue creator.

Note: Please do not work on issues which are already being worked on by another contributor. We don't encourage creating multiple pull requests for the same issue. Also, please allow the assigned person some days to work on the issue ( The time might vary depending on the difficulty). If there is no progress after the deadline, please comment on the issue asking the contributor whether he/she is still working on it. If there is no reply, then feel free to work on the issue.


### Best Practices to send Pull Requests:
  - Follow the Pull request template.
  - Commit messages should follow this template: `Fix #<issue-no> - <issue-desc>`
  - Squash all your commits to a single commit.
  - Create a new branch before adding and committing your changes ( This allows you to send multiple Pull Requests )
