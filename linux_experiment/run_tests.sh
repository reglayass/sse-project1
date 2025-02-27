#!/bin/bash

# Function to display usage information
usage() {
    echo "Usage: $0 --iter=<number>"
    exit 1
}

# Function to run tests
run_test() {
    local framework=$1
    local iter=$2
    local result_dir="tests/results/$framework"

    # Assign ports based on the framework
    local port
    case $framework in
        flask) port=5000 ;;
        express) port=3000 ;;
        springboot) port=8080 ;;
        *) echo "Unknown framework: $framework"; exit 1 ;;
    esac

    # Check if results directory exists
    if [ ! -d "$result_dir" ]; then
        echo -e "Results directory for $framework does not exist. Creating it...\n"
        mkdir -p "$result_dir"
    fi

    # Start the framework
    echo -e "Starting framework: $framework...\n"
    docker compose up "$framework" -d
    # Give it some time to build the container
    echo -e "Letting Docker start up...\n"
    sleep 15

    # Run artillery test
    echo -e "Starting test for $framework...\n"
    echo "$password" | sudo -S ./energibridge -g --output="$result_dir/results_${framework}_${iter}.csv" ab -c 150 -n 10000 "http://localhost:$port/filter?genre=Drama&rating=9&votes=100"

    echo -e "Test done for $framework.\n"
    docker compose down > /dev/null 2>&1

    echo -e "Process cleanup complete for $framework.\n"
}

start_time=$(date +%s)

# Default values
iterations=30

# Parse command-line options
while [[ "$#" -gt 0 ]]; do
    case $1 in
        --iter=*) iterations="${1#*=}" ;; # Remove the 'iter=' part
        *) usage ;;  # Unknown option
    esac
    shift
done

# Prompt for password and store it in a variable
read -s -p "Enter your password: " password

# List of frameworks to test
frameworks=("flask" "express" "springboot")

# Run the tests in random order for each iteration
for ((i=1; i<=iterations; i++)); do
    echo -e "Test Iteration: $i\n"
    
    # Shuffle the frameworks array
    shuffled_frameworks=($(shuf -e "${frameworks[@]}"))

    # Run tests for each framework in random order
    for framework in "${shuffled_frameworks[@]}"; do
        run_test "$framework" "$i"
        # Sleep for 60 seconds before running the next framework
        echo -e "Sleeping for 1 min before next framework...\n"
        sleep 60
    done
done

end_time=$(date +%s)
execution_time=$(( (end_time - start_time) / 60 ))
echo "Total Execution time: $execution_time minutes"

exit 0