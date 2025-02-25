// Using the Fetch API to send a GET request to GitHub's API

async function fetchGitHubAPI() {
    try {
      const response = await fetch('https://api.github.com');
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      const data = await response.json();
      console.log('GitHub API response:', data);
    } catch (error) {
      console.error('Error fetching GitHub API:', error);
    }
  }
  
async function repeatRequests(times = 100) {
    for (let i = 1; i <= times; i++) {
      await fetchGitHubAPI(i);
    }
}
  