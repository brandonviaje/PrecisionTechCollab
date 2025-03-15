document.addEventListener("DOMContentLoaded", function() {
    // Get user data from localStorage
    const username = localStorage.getItem('userName') || localStorage.getItem('username');
    const password = localStorage.getItem('password') || "red"; // Default password if not stored

    // Display username in the welcome message
    const displayUsernameElement = document.getElementById('display-username');
    if (displayUsernameElement && username) {
        displayUsernameElement.textContent = username;
    }

    // Display username in the account details section
    const accountUsernameElement = document.getElementById('account-username');
    if (accountUsernameElement && username) {
        accountUsernameElement.textContent = username;
    }

    // Handle password display and toggle functionality
    const togglePasswordBtn = document.getElementById('toggle-password');
    const passwordElement = document.getElementById('account-password');

    if (togglePasswordBtn && passwordElement) {
        // Store the actual password but don't display it initially
        passwordElement.dataset.password = password;

        togglePasswordBtn.addEventListener('click', function() {
            const icon = this.querySelector('i');

            if (passwordElement.classList.contains('masked-password')) {
                // Show password
                passwordElement.textContent = passwordElement.dataset.password;
                passwordElement.classList.remove('masked-password');
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                // Hide password
                passwordElement.textContent = '••••••';
                passwordElement.classList.add('masked-password');
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        });
    }

    // Check if user is logged in
    const isSignedIn = localStorage.getItem('isSignedIn');
    if (isSignedIn !== 'true') {
        // If not logged in, redirect to sign in page
        alert('Please sign in to view your account');
        window.location.href = '../components/signIn.html';
    }

    // Add sign out functionality to the sidebar sign out button
    const sidebarSignOutBtn = document.getElementById('sidebar-signout');
    if (sidebarSignOutBtn) {
        sidebarSignOutBtn.addEventListener('click', function(e) {
            e.preventDefault();

            // Clear login information
            localStorage.removeItem('isSignedIn');
            localStorage.removeItem('userName');
            localStorage.removeItem('username');

            // Redirect to sign in page
            alert('You have been signed out');
            window.location.href = '../components/signIn.html';
        });
    }
});