#!/bin/sh

# Function to check the current branch
check_current_branch() {
    echo "\n🚀 Checking the current git branch..."
    CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
    if [ "$CURRENT_BRANCH" = "master" ] || [ "$CURRENT_BRANCH" = "development" ]; then
        echo "🛑 Hold it right there! Committing directly to the '$CURRENT_BRANCH' branch? That's a big no-no!"
        echo "🚫 Direct commits to '$CURRENT_BRANCH' are like trying to use a wrench to write code—doesn't work! 😜"
        echo "\nABORTING COMMIT: You must navigate to a feature branch or create a new one to save the day! 🦸‍♂️🦸‍♀️\n"
        exit 1
    else
        echo "✅ Fantastic! You're on the '$CURRENT_BRANCH' branch, which is perfect for commits. Let's keep this awesome momentum going! 🚀✨"
    fi
}

# Function to run Spotless checks
run_spotless_checks() {
    echo "\n🚀 Spotless is now analyzing and formatting your code!"
    ./gradlew spotlessApply --no-configuration-cache --daemon > /tmp/spotless-result
    SPOTLESS_EXIT_CODE=$?

    if [ ${SPOTLESS_EXIT_CODE} -ne 0 ]; then
        cat /tmp/spotless-result
        rm /tmp/spotless-result
        echo "\n*********************************************************************************"
        echo "   💥 Uh-oh! Spotless found formatting issues in the code! Time to tidy up! 💥"
        echo "      💡 Tip: Check the reported issues and fix formatting errors. 🛠️"
        echo "*********************************************************************************"
        exit ${SPOTLESS_EXIT_CODE}
    else
        rm /tmp/spotless-result
        echo "🎉 Stellar job! Your code is pristine and has passed Spotless's formatting checks without a hitch! Keep shining bright! ✨🚀"
    fi
}

# Function to run ktlint checks
run_dependency_guard() {
    printf "\n🚀 Brace yourself! We're about to generate dependency guard baseline!"
    ./gradlew dependencyGuardBaseline
    KT_EXIT_CODE=$?

    if [ ${KT_EXIT_CODE} -ne 0 ]; then
        printf "\n*********************************************************************************"
        echo "     💥 Oh no! Something went wrong! 💥"
        echo "     💡 Unable to generate dependency baseline. 🛠️"
        printf "*********************************************************************************\n"
        exit ${KT_EXIT_CODE}
    else
        echo "🎉 Bravo! Dependency baseline has been generated successfully! Keep rocking that clean code! 🚀💫"
    fi
}

# Function to run Detekt checks
run_detekt_checks() {
    echo "\n🚀 Detekt is now analyzing your Kotlin code for potential issues!"
    ./gradlew detekt > /tmp/detekt-result
    DETEKT_EXIT_CODE=$?

    if [ ${DETEKT_EXIT_CODE} -ne 0 ]; then
        cat /tmp/detekt-result
        rm /tmp/detekt-result
        echo "\n*********************************************************************************"
        echo "     💥 Oh no! Detekt found issues in the code! Time to fix those issues! 💥"
        echo "     💡 Tip: Review the Detekt report to resolve these issues. 🛠️"
        echo "*********************************************************************************"
        exit ${DETEKT_EXIT_CODE}
    else
        rm /tmp/detekt-result
        echo "🎉 Fantastic work! Your Kotlin code has sailed through Detekt's analysis with ease! Onward to greatness! 🚀🌟"
    fi
}

# Function to print success message
print_success_message() {
    GIT_USERNAME=$(git config user.name)
    echo "\n *******************************************************************************"
    echo "🚀🎉 Huzzah, $GIT_USERNAME! Your code has triumphed through the Style Checker Dragon unscathed! 🐉"
    echo "Your code shines brighter than a supernova and sparkles like a constellation of stars! ✨🌌"
    echo "*******************************************************************************"
    echo "\n🚀🎉 Hold tight, $GIT_USERNAME! Your code is ready to commit and conquer new heights! 🌟✨ Keep up the amazing work! 💪\n"
}

# Main script execution
check_current_branch
run_spotless_checks
run_detekt_checks
run_dependency_guard
print_success_message

exit 0
