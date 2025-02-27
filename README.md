# **Sustainable Servers**: Benchmarking energy consumption of various backend frameworks

This project is the experimental setup of a study that compares energy consumption of 3 popular backend frameworks: `express.js`, `Flask` and `SpringBoot`. Using [Apache Bench](https://httpd.apache.org/docs/current/programs/ab.html) to stress test each server isolated inside its own [Docker container](https://www.docker.com/resources/what-container/), [EnergiBridge](https://github.com/tdurieux/energibridge) measures the recorded energy consumptions during the load of API requests.

## Setup

Before running the experiment, make sure you have Docker installed and Energi Bridge set up as specified in the documentation. The absolut path you should put is the one of the `LibreHardwareMonitor.sys` inside this repository.

**NOTE:** We recommend setting up Linux containers. While the setup can be run on Linux-based OS & Windows, the results were run on **Windows**.

| Laptop | Lenovo Yoga Pro 9 |
| ------ | ------------------ |
| CPU    | Intel(R) Core(TM) Ultra 9 185H   2.30 GHz |
| RAM    | 32 GB      |
| GPU    | NVIDIA GeForce RTX4060 Laptop  |
| OS     | Windows 11 Home 24H2    |
##### Table 1: Laptop specifications used in our experiment

## How to run the experiment

1. Run Docker locally
2. For **Linux-based OS**, go to `/linux_experiment` & run `./run_tests.sh --iter=<number>` to run the default experiment we have used for our analysis. For **Windows**, go to `/windows_experiment` & run `run_tests.bat --iter=<number>`. If you would like to play around, you can change the number of iterations that are run per server using `--iter=[number]`.
3. To view our test results, go to `/tests` and unzip the `results.zip` folder. Inside it, there are sub-directories for each backend framework that contain data from every iteration formated as `.csv` files.
