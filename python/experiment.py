import requests

# Make a GET request to GitHub's API root endpoint
for i in range(100):
    response = requests.get("https://api.github.com")
    # Print out the status code and JSON response
    print("Status Code:", response.status_code)