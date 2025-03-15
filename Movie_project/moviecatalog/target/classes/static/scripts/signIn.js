// Function to handle sign-in logic with user authentication
document.addEventListener("DOMContentLoaded", function () {
    // Attach event listener to the form after the DOM is loaded
    const form = document.getElementById('signin-form');

    // Attach the sign-in handler to the form submission
    form.addEventListener('submit', handleSignIn);
});

// Function to handle sign-in logic
function handleSignIn(event) {
    event.preventDefault();

    const username = document.getElementById('login-username').value;
    const password = document.getElementById('password').value;

    // Retrieve users array from localStorage
    const users = JSON.parse(localStorage.getItem("users")) || [];

    // Find user with matching username and password
    const user = users.find(user => user.username === username && user.password === password);

    // Fallback to temporary credentials for development purposes
    const tempUsername = "red";
    const tempPassword = "red";
    const isTempCredentials = username === tempUsername && password === tempPassword;

    if (user || isTempCredentials) {
        alert('Login successful!');

        // Store authentication data in localStorage
        localStorage.setItem('isSignedIn', 'true');
        localStorage.setItem('username', username);
        localStorage.setItem('userName', username);
        localStorage.setItem('password', password); // Store password for account page display

        // If it's a registered user, also store their full name and join date
        if (user) {
            localStorage.setItem('fullName', user.name);

            // If the user doesn't have a join date, set it now
            if (!user.joinDate) {
                const joinDate = getCurrentDate();
                user.joinDate = joinDate;
                // Update the user in the users array
                const userIndex = users.findIndex(u => u.username === username);
                if (userIndex !== -1) {
                    users[userIndex] = user;
                    localStorage.setItem("users", JSON.stringify(users));
                }
                localStorage.setItem('joinDate', joinDate);
            } else {
                localStorage.setItem('joinDate', user.joinDate);
            }
        } else {
            // For temp credentials, set join date to current date
            localStorage.setItem('joinDate', getCurrentDate());
        }

        console.log("Username stored in localStorage:", localStorage.getItem('username'));
        console.log("UserName stored in localStorage:", localStorage.getItem('userName'));

        // Redirect to index page
        window.location.href = "../index.html";
    } else {
        alert('Invalid username or password!');
    }
}

// Helper function to get current date in a formatted string
function getCurrentDate() {
    const now = new Date();
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return now.toLocaleDateString('en-US', options);
}