// Function to handle sign-in logic with temporary credentials
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

    // Temporary credentials
    const tempUsername = "red";
    const tempPassword = "red";

    // Debugging log to verify input values
    console.log("Entered Username: " + username);
    console.log("Entered Password: " + password);
    console.log("Expected Username: " + tempUsername);
    console.log("Expected Password: " + tempPassword);

    // Check if the entered username and password match the temp credentials
    if (username === tempUsername && password === tempPassword) {
        alert('Login successful!');

        // Store both 'username' and 'userName' for compatibility with both scripts
        localStorage.setItem('isSignedIn', 'true');
        localStorage.setItem('username', username);
        localStorage.setItem('userName', username);
        localStorage.setItem('password', password); // Store password for display on account page

        console.log("Username stored in localStorage:", localStorage.getItem('username'));
        console.log("UserName stored in localStorage:", localStorage.getItem('userName'));

        // Redirect to index page
        window.location.href = "../index.html";
    } else {
        alert('Invalid username or password!');
    }
}