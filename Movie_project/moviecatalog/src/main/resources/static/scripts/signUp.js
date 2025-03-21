document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("signup-form");

    form.addEventListener("submit", function (event) {
        event.preventDefault(); // Prevent form submission

        const name = document.getElementById("name").value.trim();
        const username = document.getElementById("create_username").value.trim();
        const password = document.getElementById("password").value;
        const confirmPassword = document.getElementById("confirm-password").value;

        // Enhanced validation
        if (!name || !username || !password || !confirmPassword) {
            alert("All fields are required!");
            return;
        }

        // Check minimum length for username and password
        if (username.length < 3) {
            alert("Username must be at least 3 characters long");
            return;
        }

        if (password.length < 6) {
            alert("Password must be at least 6 characters long");
            return;
        }

        // Check if passwords match
        if (password !== confirmPassword) {
            alert("Passwords do not match!");
            return;
        }

        // Create form data for the API request
        const formData = new FormData();
        formData.append("fullName", name);
        formData.append("username", username);
        formData.append("password", password);

        // Show loading indicator
        const submitButton = form.querySelector("button[type='submit']");
        const originalButtonText = submitButton.textContent;
        submitButton.textContent = "Creating Account...";
        submitButton.disabled = true;

        // Send data to server using fetch API
        fetch('/api/accounts', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => { throw new Error(text || 'Network response was not ok') });
                }
                return response.text();
            })
            .then(data => {
                alert("Account created successfully! Please log in.");
                window.location.href = "signIn.html"; // Redirect to sign-in page
            })
            .catch(error => {
                console.error('Error:', error);
                alert(error.message || "Error creating account. Please try again.");
            })
            .finally(() => {
                // Reset button state
                submitButton.textContent = originalButtonText;
                submitButton.disabled = false;
            });
    });
});