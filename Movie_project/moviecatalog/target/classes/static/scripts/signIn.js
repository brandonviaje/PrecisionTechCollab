// Function to handle sign-in logic with proper user authentication
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

    // Debug: Check if there are any stored users
    const storedUsers = JSON.parse(localStorage.getItem("users")) || [];
    console.log("Currently stored users:", storedUsers.length);
});

// Function to handle sign-in logic
function handleSignIn(event) {
    event.preventDefault();
    console.log("Sign-in attempt started");

    const username = document.getElementById('login-username').value;
    const password = document.getElementById('password').value;

    console.log("Attempting login with username:", username);

    // Retrieve users array from localStorage
    const users = JSON.parse(localStorage.getItem("users")) || [];
    console.log("Found", users.length, "stored users");

    // Find user with matching username and password
    const user = users.find(user => user.username === username && user.password === password);
    console.log("User found:", !!user);

    if (user) {
        console.log("Login successful for user:", username);
        alert('Login successful!');

        // Store authentication data in localStorage
        localStorage.setItem('isSignedIn', 'true');
        localStorage.setItem('username', username);
        localStorage.setItem('userName', username);

        // Store user data
        localStorage.setItem('fullName', user.name);
        localStorage.setItem('password', password);

        // Handle join date
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

        console.log("Username stored in localStorage:", localStorage.getItem('username'));
        console.log("UserName stored in localStorage:", localStorage.getItem('userName'));

        // Redirect to index page
        window.location.href = "../index.html";
    } else {
        console.log("Login failed: Invalid username or password");
        alert('Invalid username or password!');
    }
}

// Helper function to get current date in a formatted string
function getCurrentDate() {
    const now = new Date();
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return now.toLocaleDateString('en-US', options);
}