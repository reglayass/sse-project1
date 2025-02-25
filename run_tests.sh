#! /bin/bash

# THIS IS A WORK IN PROGRESS

# Check if results directory exists
if [ ! -d "tests/results/flask" ]; then
    echo "Results directory for Flask does not exist. Creating it..."
    mkdir -p "tests/results/flask"
fi

# Install artillery as a package
npm install

sudo ./energibridge --output="tests/results/flask/results_flask_1.csv" docker compose up flask -d --remove-orphans &
$pid=$! # Capture the process ID of the energibridge background process
sleep 30 # Give it some time to build the container
artillery run tests/test_flask.yml
# Artillery is done, kill the energibridge process
echo "Artillery done, kill process..."
kill -9 $pid
wait $pid 2>/dev/null
echo "Iteration $i: Process cleanup complete."
sleep 60

# # Run the flask test
# # Run experiment 30 times
# for ((i=1; i<=30; i++)); do
#     echo "Test Iteration: $i"

#     echo "Starting container and running Energibridge..."
#     ./energibridge --output="results/flask/results_flask_${i}.csv" --build --remove-orphans compose up flask -d &
#     $pid=$! # Capture the process ID of the energibridge background process
#     sleep 30 # Give it some time to build the container
#     echo "Running artillery test..."
#     artillery run test_flask.yml

#     # Artillery is done, kill the energibridge process
#     echo "Artillery done, kill process..."
#     kill -9 $pid
#     wait $pid 2>/dev/null
#     echo "Iteration $i: Process cleanup complete."
#     sleep 60
# done