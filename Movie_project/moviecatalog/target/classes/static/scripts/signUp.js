document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("signup-form");

    form.addEventListener("submit", function (event) {
        event.preventDefault(); // Prevent form submission

        const name = document.getElementById("name").value.trim();
        const username = document.getElementById("create_username").value.trim();
        const password = document.getElementById("password").value;
        const confirmPassword = document.getElementById("confirm-password").value;

        // Basic validation
        if (!name || !username || !password || !confirmPassword) {
            alert("All fields are required!");
            return;
        }

        // Check if passwords match
        if (password !== confirmPassword) {
            alert("Passwords do not match!");
            return;
        }

        // Retrieve existing users from localStorage or create an empty array
        const users = JSON.parse(localStorage.getItem("users")) || [];

        // Check if the username is already taken
        if (users.some(user => user.username === username)) {
            alert("This username is already taken. Please choose a different one.");
            return;
        }

        // Get current date for join date
        const joinDate = getCurrentDate();

        // Save user to localStorage with join date
        users.push({
            name,
            username,
            password,
            joinDate
        });
        localStorage.setItem("users", JSON.stringify(users));

        alert("Account created successfully! Please log in.");
        window.location.href = "signIn.html"; // Redirect to sign-in page
    });
});

// Helper function to get current date in a formatted string
function getCurrentDate() {
    const now = new Date();
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return now.toLocaleDateString('en-US', options);
}