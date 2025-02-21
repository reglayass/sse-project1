import requests
import time
from pyEnergiBridge.api import EnergiBridgeRunner


def start_energi_bridge():
    print("Starting EnergiBridge...")
    runner = EnergiBridgeRunner(is_containerized=True)
    runner.start(results_file="/flask/results.csv")
    return runner


def stop_energi_bridge(runner):
    print("Stopping EnergiBridge...")
    energy, duration = runner.stop()
    with open("energy_results.txt", "w") as f:
        f.write(f"Energy consumption (J): {energy}\n")
        f.write(f"Execution time (s): {duration}\n")
    print(f"Energy consumption (J): {energy}; Execution time (s): {duration}")


if __name__ == '__main__':
    # Wait for the server to start
    time.sleep(5)
    
    runner = start_energi_bridge()
    for i in range(10000):
        try:
            response = requests.get("http://host.docker.internal:5000")
        except requests.exceptions.RequestException as e:
            print(f"Request {i+1} failed: {e}")

    stop_energi_bridge(runner)