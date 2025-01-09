#!/bin/bash

# Check if gradlew exists in the project
if [ ! -f "./gradlew" ]; then
    echo "Error: gradlew not found in the project."
    exit 1
fi

echo "Starting all checks and tests..."

failed_tasks=()
successful_tasks=()

run_gradle_task() {
    echo "Running: $1"
    "./gradlew" $1
    if [ $? -ne 0 ]; then
        echo "Warning: Task $1 failed"
        failed_tasks+=("$1")
    else
        echo "Task $1 completed successfully"
        successful_tasks+=("$1")
    fi
}

tasks=(
    "check -p build-logic"
    "spotlessApply --no-configuration-cache"
    "dependencyGuardBaseline"
    "detekt"
)

for task in "${tasks[@]}"; do
    run_gradle_task "$task"
done

echo "All tasks have finished."

echo "Successful tasks:"
for task in "${successful_tasks[@]}"; do
    echo "- $task"
done

if [ ${#failed_tasks[@]} -eq 0 ]; then
    echo "All checks and tests completed successfully."
else
    echo "Failed tasks:"
    for task in "${failed_tasks[@]}"; do
        echo "- $task"
    done
    echo "Please review the output above for more details on the failures."
fi

echo "Total tasks: ${#tasks[@]}"
echo "Successful tasks: ${#successful_tasks[@]}"
echo "Failed tasks: ${#failed_tasks[@]}"