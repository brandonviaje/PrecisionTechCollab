document.addEventListener("DOMContentLoaded", function () {
    // Attach event listener to the form after the DOM is loaded
    const form = document.getElementById('signin-form');

    if (form) {
        console.log("Sign-in form found, attaching submit handler");
        // Attach the sign-in handler to the form submission
        form.addEventListener('submit', handleSignIn);
    } else {
        console.error("Sign-in form not found");
    }
});

// Function to handle sign-in logic
function handleSignIn(event) {
    event.preventDefault();
    console.log("Sign-in attempt started");

    const username = document.getElementById('login-username').value;
    const password = document.getElementById('password').value;
    console.log("Attempting login with:", username);

    // Basic validation
    if (!username || !password) {
        alert('Username and password are required!');
        return;
    }

    console.log("Attempting login with username:", username);

    // Show loading indicator
    const submitButton = event.target.querySelector("button[type='submit']");
    const originalButtonText = submitButton.textContent;
    submitButton.textContent = "Signing In...";
    submitButton.disabled = true;

    // Call the backend API to verify credentials
    fetch(`/api/accounts?username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => {
            console.log("Response status:", response.status);

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            return response.json();
        })
        .then(data => {
            // Handle the response data
            console.log("Response data:", JSON.stringify(data));

            if (data.success === true) {
                console.log("Login successful for user:", username);

                // Store authentication data in localStorage
                localStorage.setItem('isSignedIn', 'true');
                localStorage.setItem('username', username);
                localStorage.setItem('userName', username);

                // Store password for account page display
                localStorage.setItem('password', password);

                // Store additional user information
                localStorage.setItem('fullName', data.fullName || username);

                // Store join date if available from the response, otherwise set current date
                if (data.joinDate) {
                    localStorage.setItem('joinDate', data.joinDate);
                } else {
                    const now = new Date();
                    const options = { year: 'numeric', month: 'long', day: 'numeric' };
                    localStorage.setItem('joinDate', now.toLocaleDateString('en-US', options));
                }

                // Redirect to index page
                window.location.href = "../index.html";
            } else {
                console.log("Login failed: Invalid username or password");
                alert('Invalid username or password!');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error during sign in. Please try again.');
        })
        .finally(() => {
            // Reset button state
            submitButton.textContent = originalButtonText;
            submitButton.disabled = false;
        });
}