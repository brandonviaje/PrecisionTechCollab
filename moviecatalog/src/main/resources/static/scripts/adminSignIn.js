// Function to handle admin sign-in logic with temporary credentials
document.addEventListener("DOMContentLoaded", function () {
    // Attach event listener to the form after the DOM is loaded
    const form = document.getElementById('signin-form');

    // Attach the sign-in handler to the form submission
    if (form) {
        form.addEventListener('submit', handleAdminSignIn);
    }
});

// Function to handle admin sign-in logic
function handleAdminSignIn(event) {
    event.preventDefault();

    const username = document.getElementById('adminusername').value;
    const password = document.getElementById('adminpassword').value;

    // Temporary admin credentials
    const adminUsername = "admin";
    const adminPassword = "admin";

    // Debugging log to verify input values
    console.log("Entered Username: " + username);
    console.log("Entered Password: " + password);
    console.log("Expected Username: " + adminUsername);
    console.log("Expected Password: " + adminPassword);

    // Check if the entered username and password match the admin credentials
    if (username === adminUsername && password === adminPassword) {
        alert('Admin login successful!');

        // Store admin login information
        localStorage.setItem('isAdminSignedIn', 'true');
        localStorage.setItem('adminUsername', username);
        localStorage.setItem('adminPassword', password); // Store password for display on admin account page if needed

        console.log("Admin Username stored in localStorage:", localStorage.getItem('adminUsername'));

        // Redirect to admin dashboard
        window.location.href = "../components/manageMovies.html";
    } else {
        alert('Invalid admin credentials!');
    }
}