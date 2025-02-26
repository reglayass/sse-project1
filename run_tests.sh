#!/bin/bash

start_time=$(date +%s)

# Default values
iterations=30
flask_test=false
express_test=false
springboot_test=false

# Function to display usage information
usage() {
    echo "Usage: $0 --flask --express --springboot --iter=<number>"
    exit 1
}

# Parse command-line options
while [[ "$#" -gt 0 ]]; do
    case $1 in
        --flask) flask_test=true ;;
        --express) express_test=true ;;
        --springboot) springboot_test=true ;;
        --iter=*) iterations="${1#*=}" ;; # Remove the 'iter=' part
        *) usage ;;  # Unknown option
    esac
    shift
done

# If no tests are specified, show usage
if ! $flask_test && ! $express_test && ! $springboot_test; then
    usage
fi

# Install artillery as a package
npm install

# Function to run tests
run_test() {
    local framework=$1
    local result_dir="tests/results/$framework"
    local test_file="tests/test_$framework.yml"

    # Check if results directory exists
    if [ ! -d "$result_dir" ]; then
        echo -e "Results directory for $framework does not exist. Creating it...\n"
        mkdir -p "$result_dir"
    fi

    # Prompt for password and store it in a variable
    read -s -p "Enter your password: " password

    # Run the test for the specified number of iterations
    for ((i=1; i<=iterations; i++)); do
        echo -e "Test Iteration: $i\n"

        echo -e "Starting framework...\n"
        docker compose up "$framework" -d
        # Give it some time to build the container
        echo -e "Letting Docker start up...\n"
        sleep 15

        # Run artillery test
        echo -e "Starting test for $framework...\n"
        echo "$password" | sudo -S ./energibridge -g --output="$result_dir/results_${framework}_${i}.csv" ab -c 2 -n 10000 "http://localhost:5000/filter?genre=Drama&rating=9&votes=100"

        # Run artillery test
        # echo -e "Starting Artillery test for $framework...\n"
        # artillery run --quiet "$test_file"
        # Artillery is done, kill the energibridge process
        echo -e "Test done.\n"
        docker compose down > /dev/null 2>&1

        echo -e "Iteration $i: Process cleanup complete.\n"

        # Sleep for 60 seconds before running the next iteration
        echo -e "Sleeping for 1 min...\n"
        sleep 60
    done
}

# Run the selected tests
if $flask_test; then
    run_test "flask"
fi

if $express_test; then
    run_test "express"
fi

if $springboot_test; then
    run_test "springboot"
fi

end_time=$(date +%s)
execution_time=$(( (end_time - start_time) / 60 ))
echo "Total Execution time: $execution_time minutes"

exit 0
