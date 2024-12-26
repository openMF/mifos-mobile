# Contributing to Mifos Mobile

Welcome to the Mifos Mobile contributing guide. This comprehensive document outlines how to contribute effectively to improve the Self Service Android Application, from reporting issues to submitting code changes.

The [Open Source Guides](https://opensource.guide/) website has a collection of resources for individuals, communities, and companies who want to learn how to run and contribute to an open source project. Contributors and people new to open source alike will find the following guides especially useful:

- [How to Contribute to Open Source](https://opensource.guide/how-to-contribute/)
- [Building Welcoming Communities](https://opensource.guide/building-community/)

## Code of Conduct

Mifos has adopted a Code of Conduct that we expect project participants to adhere to. Please read [the full text](CODE_OF_CONDUCT.md) so that you can understand what actions will and will not be tolerated.

> \[!Note]
> **We're moving to Jira for issue tracking. Please use [Jira](https://mifosforge.jira.com/jira/software/c/projects/MM/issues/?filter=allissues&jql=project%20%3D%20%22MM%22%20ORDER%20BY%20created%20DESC) for issue tracking.**
> **And Join our [slack](https://join.slack.com/t/mifos/shared_invite/zt-2wvi9t82t-DuSBdqdQVOY9fsqsLjkKPA) channel `mifos-mobile` to discuss all things about Mifos Mobile development. and do not cross post your messages in multiple channels. ask your question in the appropriate channel.**

## Get Involved

There are many ways to contribute to Mifos Mobile, and many of them do not involve writing any code. Here's a few ideas to get started:

- Simply start using Mifos Mobile. Go through the [Getting Started](https://github.com/openMF/mifos-mobile/wiki) guide. Does everything work as expected? If not, we're always looking for improvements. Let us know by [opening an issue](#issues).
- Look through the [open issues](https://github.com/openMF/mifos-mobile/issues). Provide workarounds, ask for clarification, or suggest labels. Help [triage issues](#triaging-issues-and-pull-requests).
- If you find an issue you would like to fix, [open a pull request](#pull-requests). Issues tagged as [_Good first issue_](https://github.com/openMF/mifos-mobile/labels/good%20first%20issue) are a good place to get started.
- Read through the [Wiki](https://github.com/openMF/mifos-mobile/wiki). If you find anything that is confusing or can be improved, you can click "Edit this page" at the top of most page, which takes you to the GitHub interface to make and propose changes.

Contributions are very welcome. If you think you need help planning your contribution, please ping us on Slack at [#mifos-mobile](https://join.slack.com/t/mifos/shared_invite/zt-2wvi9t82t-DuSBdqdQVOY9fsqsLjkKPA) and let us know you are looking for a bit of help.

### Join our Slack Channel & Jira

We have the [`#mifos-mobile`](https://join.slack.com/t/mifos/shared_invite/zt-2wvi9t82t-DuSBdqdQVOY9fsqsLjkKPA) channel on [Slack](https://join.slack.com/t/mifos/shared_invite/zt-2wvi9t82t-DuSBdqdQVOY9fsqsLjkKPA) to discuss all things about Mifos Mobile development. You can also be of great help by helping other users in the [`#mobile](https://join.slack.com/t/mifos/shared_invite/zt-2wvi9t82t-DuSBdqdQVOY9fsqsLjkKPA) channel.
also join [Jira](https://mifosforge.jira.com/jira/software/c/projects/MM/issues/?filter=allissues&jql=project%20%3D%20%22MM%22%20ORDER%20BY%20created%20DESC) for issue tracking.

[![Slack](https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=slack&logoColor=white)](https://join.slack.com/t/mifos/shared_invite/zt-2wvi9t82t-DuSBdqdQVOY9fsqsLjkKPA)
[![Jira](https://img.shields.io/badge/jira-%230A0FFF.svg?style=flat-square&logo=jira&logoColor=white)](https://mifosforge.jira.com/jira/software/c/projects/MM/issues/?filter=allissues&jql=project%20%3D%20%22MM%22%20ORDER%20BY%20created%20DESC)


### Triaging Issues and Pull Requests

One great way you can contribute to the project without writing any code is to help triage issues and pull requests as they come in.

- Ask for more information if you believe the issue does not provide all the details required to solve it.
- Suggest [labels](https://github.com/openMF/mifos-mobile/labels) that can help categorize issues.
- Flag issues that are stale or that should be closed.
- Ask for test plans and review code.

## Our Development Process

We uses [GitHub](https://github.com/openMF/mifos-mobile), [Slack](https://join.slack.com/t/mifos/shared_invite/zt-2wvi9t82t-DuSBdqdQVOY9fsqsLjkKPA), [Jira](https://mifosforge.jira.com/jira/software/c/projects/MM/issues/?filter=allissues&jql=project%20%3D%20%22MM%22%20ORDER%20BY%20created%20DESC) for issue tracking and development. The core team will be working directly there. All changes will be public from the beginning.

All pull requests will be checked by the continuous integration system, GitHub actions. There are unit tests, end-to-end tests, performance tests, style tests, and much more.

### Branch Organization

Mifos Mobile has one primary branch `development` and we use feature branches with deploy previews to deliver new features with pull requests.

## Issues

When [opening a new issue](https://mifosforge.jira.com/jira/software/c/projects/MM/issues/?filter=allissues&jql=project%20%3D%20%22MM%22%20ORDER%20BY%20created%20DESC), always make sure to fill out the issue template. **This step is very important!** Not doing so may result in your issue not being managed in a timely fashion. Don't take this personally if this happens, and feel free to open a new issue once you've gathered all the information required by the template.

**Please don't use the GitHub issue tracker for questions.** If you have questions about using Mifos Mobile, use of our [slack channel](https://join.slack.com/t/mifos/shared_invite/zt-2wvi9t82t-DuSBdqdQVOY9fsqsLjkKPA), and we will do our best to answer your questions.

### Bugs

We use [Jira](https://mifosforge.jira.com/jira/software/c/projects/MOBILE/boards/57) for our public bugs. If you would like to report a problem, take a look around and see if someone already opened an issue about it. If you are certain this is a new, unreported bug, you can submit a [bug report](https://github.com/facebook/docusaurus/issues/new?assignees=&labels=bug%2Cstatus%3A+needs+triage&template=bug.yml).

- **One issue, one bug:** Please report a single bug per issue.
- **Provide reproduction steps:** List all the steps necessary to reproduce the issue. The person reading your bug report should be able to follow these steps to reproduce your issue with minimal effort.

If you're only fixing a bug, it's fine to submit a pull request right away but we still recommend filing an issue detailing what you're fixing. This is helpful in case we don't accept that specific fix but want to keep track of the issue.

### Feature requests

If you would like to request a new feature or enhancement but are not yet thinking about opening a pull request, 
you can open a ticket on [Jira](https://mifosforge.jira.com/jira/software/c/projects/MM/issues/). Alternatively, you can use the [GitHub](https://docusaurus.io/feature-requests) for more casual feature requests and gain enough traction before proposing on Jira.

### Proposals

If you intend to make any non-trivial changes to existing implementations, we recommend filing an issue with the [Jira](https://mifosforge.jira.com/jira/software/c/projects/MM/issues/). This lets us reach an agreement on your proposal before you put significant effort into it. These types of issues should be rare.

### Claiming issues

We have a list of [beginner-friendly issues](https://github.com/openMF/mifos-mobile/issues?q=is%3Aissue+is%3Aopen+label%3A%22good+first+issue%22) to help you get your feet wet in the Mifos Mobile codebase and familiar with our contribution process. This is a great place to get started.

Apart from the `good first issue`, the following labels are also worth looking at:

- [`help wanted`](https://github.com/openMF/mifos-mobile/labels/help%20wanted): if you have specific knowledge in one domain, working on these issues can make your expertise shine.
- [`jira`](https://mifosforge.jira.com/jira/software/c/projects/MOBILE/boards/57): if you are looking for a specific issue, you can find it here.

If you want to work on any of these issues, just drop a message saying "I'd like to work on this", and we will assign the issue to you and update the issue's status as "claimed". **You are expected to send a pull request within seven days** after that, so we can still delegate the issue to someone else if you are unavailable.


![jira-create-issue](https://github.com/user-attachments/assets/f2440bc1-a7d2-4815-92d3-549a72983df6)

> \[!Note]
> To know more about [Jira](https://www.atlassian.com/software/jira/guides/getting-started/basics#step-4-create-an-issue) and how to create an issue, click [here](https://mifosforge.jira.com/jira/software/c/projects/MM/issues/)

## Development

### Pre-requisites
- Install [Android Studio](https://developer.android.com/studio)
- Install [Git](https://git-scm.com/downloads)
- Install JDK 17 or higher

For more information, see the [Development Setup](https://github.com/openMF/mifos-mobile/wiki/Set-up-an-environment) guide.

### Building the Code

Before you begin, you should have already downloaded the Android Studio SDK and set it up correctly. You can find a guide on how to do this here: [Setting up Android Studio](http://developer.android.com/sdk/installing/index.html?pkg=studio).

To Setup the project, follow the steps below:
- [Fork the Repository](https://github.com/openMF/mifos-mobile/wiki/Project-Setup#step-1-fork-the-repository)
- [Clone Your Forked Repository on Android Studio](https://github.com/openMF/mifos-mobile/wiki/Project-Setup#step-2-clone-your-forked-repository)
- [Set Up Your Development Branch](https://github.com/openMF/mifos-mobile/wiki/Project-Setup#step-3-set-up-your-development-branch)

### Code Conventions

- **Most important: Look around.** Match the style you see used in the rest of the project. This includes formatting, naming files, naming things in code, naming things in documentation, etc.
- "Attractive"
- We do have Prettier (a formatter) and ESLint (a syntax linter) to catch most stylistic problems. If you are working locally, they should automatically fix some issues during every git commit.
- **For documentation**: Do not wrap lines at 80 characters - configure your editor to soft-wrap when editing documentation.

Don't worry too much about styles in general—the maintainers will help you fix them as they review your code.

## Pull Requests

So you have decided to contribute code back to upstream by opening a pull request. You've invested a good chunk of time, and we appreciate it. We will do our best to work with you and get the PR looked at.

Working on your first Pull Request? You can learn how from this free video series:

[**How to Contribute to an Open Source Project on GitHub**](https://egghead.io/courses/how-to-contribute-to-an-open-source-project-on-github)

Please make sure the following is done when submitting a pull request:

1. **Keep your PR small.** Small pull requests (~300 lines of diff) are much easier to review and more likely to get merged. Make sure the PR does only one thing, otherwise please split it.
2. **Use descriptive titles.** It is recommended to follow this [commit message style](#semantic-commit-messages).
3. **Test your changes.** Describe your [**test plan**](#test-plan) in your pull request description.

All pull requests should be opened against the `development` branch.

We have a lot of integration systems that run automated tests to guard against mistakes. The maintainers will also review your code and fix obvious issues for you. These systems' duty is to make you worry as little about the chores as possible. Your code contributions are more important than sticking to any procedures, although completing the checklist will surely save everyone's time.

### Semantic Commit Messages

See how a minor change to your commit message style can make you a better programmer.

Format: `<type>(<scope>): <subject>`

`<scope>` is optional. If your change is specific to one/two packages, consider adding the scope. Scopes should be brief but recognizable, e.g. `content-docs`, `theme-classic`, `core`

The various types of commits:

- `feat`: a new API or behavior **for the end user**.
- `fix`: a bug fix **for the end user**.
- `docs`: a change to the website or other Markdown documents in our repo.
- `refactor`: a change to production code that leads to no behavior difference, e.g. splitting files, renaming internal variables, improving code style...
- `test`: adding missing tests, refactoring tests; no production code change.
- `chore`: upgrading dependencies, releasing new versions... Chores that are **regularly done** for maintenance purposes.
- `misc`: anything else that doesn't change production code, yet is not `test` or `chore`. e.g. updating GitHub actions workflow.

Do not get too stressed about PR titles, however. Your PR will be squash-merged and your commit to the `main` branch will get the title of your PR, so commits within a branch don't need to be semantically named. The maintainers will help you get the PR title right, and we also have a PR label system that doesn't equate with the commit message types. Your code is more important than conventions!

Example:

```
feat(core): allow overriding of webpack config
^--^^----^  ^------------^
|   |       |
|   |       +-> Summary in present tense. Use lower case not title case!
|   |
|   +-> The package(s) that this change affected.
|
+-------> Type: see above for the list we use.
```

### Test Plan

A good test plan has the exact commands you ran and their output and provides screenshots or videos if the pull request changes UI.

Tests are integrated into our continuous integration system, so you don't always need to run local tests. However, for significant code changes, it saves both your and the maintainers' time if you can do exhaustive tests locally first to make sure your PR is in good shape. There are many types of tests:

In MacOS, Windows or Linux, you should run the following commands before opening a PR, and make sure to pass all the commands:

* `./gradlew check -p build-logic` this checks build-logic configured properly.</br>
* `./gradlew spotlessApply --no-configuration-cache` an check and apply formatting to any file.</br>
* `./gradlew dependencyGuardBaseline`  to generate dependency-guard baseline.</br>
* `./gradlew detekt`  to check detekt error.</br>
* `./gradlew testDebug :lint:test :androidApp:lintRelease :lint:lint` to check lint and test error.</br>
* `./gradlew build` to build the project.</br>
* `./gradlew updateReleaseBadging` to update the badging for the project.</br>

*Or Run the `ci-prepush.sh` or `ci-prepush.bat` script to run all required checks in sequence.*
