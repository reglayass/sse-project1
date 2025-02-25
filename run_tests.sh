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
        echo "Results directory for $framework does not exist. Creating it..."
        mkdir -p "$result_dir"
    fi

    Run the test for the specified number of iterations
    for ((i=1; i<=iterations; i++)); do
        echo "Test Iteration: $i"

        echo "Starting Energibridge for $framework..."
        (sudo ./energibridge --output="$result_dir/results_${framework}_${i}.csv" docker compose up "$framework" > /dev/null 2>&1) &

        # Give it some time to build the container
        echo "Letting Docker start up..."
        sleep 15

        # Run artillery test
        echo "Starting Artillery test for $framework..."
        artillery run --quiet "$test_file"
        # Artillery is done, kill the energibridge process
        echo "Artillery done, killing process..."
        pkill energibridge
        echo "Iteration $i: Process cleanup complete."

        # Sleep for 60 seconds before running the next iteration
        echo "Sleeping for 1 min..."
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