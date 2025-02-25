#! /bin/bash

# THIS IS A WORK IN PROGRESS

# Install artillery as a package
npm install

# Build database docker
docker compose up db -d

# Run the flask test
sudo ./energibridge --output="results_flask.csv" --build compose up flask -d &
$pid=$! # Capture the process ID of the energibridge background process
sleep 30 # Give it some time to build the container
artillery run test_flask.yml

# Artillery is done, kill the energibridge process
kill -9 $pid
wait $pid 2>/dev/null
echo "Process cleanup complete"