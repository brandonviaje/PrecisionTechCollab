document.addEventListener("DOMContentLoaded", function() {
    // Get user data from localStorage
    const username = localStorage.getItem('userName') || localStorage.getItem('username');
    const password = localStorage.getItem('password') || ""; // Get password from localStorage
    const fullName = localStorage.getItem('fullName') || username; // Use username as fallback
    const joinDate = localStorage.getItem('joinDate') || getCurrentDate(); // Get join date or set current date

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

    // Display full name if available
    const fullNameElement = document.getElementById('account-fullname');
    if (fullNameElement && fullName) {
        fullNameElement.textContent = fullName;
    }

    // Display member since date
    const joinDateElement = document.getElementById('member-since');
    if (joinDateElement && joinDate) {
        joinDateElement.textContent = joinDate;
    }

    // Handle password display and toggle functionality
    const togglePasswordBtn = document.getElementById('toggle-password');
    const passwordElement = document.getElementById('account-password');

    if (togglePasswordBtn && passwordElement) {
        // Initialize the password field with masked characters
        passwordElement.textContent = '••••••';

        // Make sure we can access the actual password later
        passwordElement.dataset.password = password;
        passwordElement.classList.add('masked-password');

        togglePasswordBtn.addEventListener('click', function() {
            const icon = this.querySelector('i');

            if (passwordElement.classList.contains('masked-password')) {
                // Show password - use the stored password from dataset
                passwordElement.textContent = passwordElement.dataset.password || password;
                passwordElement.classList.remove('masked-password');
                if (icon) {
                    icon.classList.remove('fa-eye');
                    icon.classList.add('fa-eye-slash');
                }
                this.setAttribute('title', 'Hide password');
            } else {
                // Hide password
                passwordElement.textContent = '••••••';
                passwordElement.classList.add('masked-password');
                if (icon) {
                    icon.classList.remove('fa-eye-slash');
                    icon.classList.add('fa-eye');
                }
                this.setAttribute('title', 'Show password');
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
            localStorage.removeItem('fullName');
            localStorage.removeItem('password');
            localStorage.removeItem('joinDate');

            // Redirect to sign in page
            alert('You have been signed out');
            window.location.href = '../components/signIn.html';
        });
    }

    // Add update profile functionality if needed
    const updateProfileBtn = document.getElementById('update-profile');
    if (updateProfileBtn) {
        updateProfileBtn.addEventListener('click', function(e) {
            e.preventDefault();
            alert('Profile update functionality will be implemented soon.');
        });
    }
});

// Helper function to get current date in a formatted string
function getCurrentDate() {
    const now = new Date();
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return now.toLocaleDateString('en-US', options);
}

// Helper function to calculate membership duration
function getMembershipDuration(joinDateStr) {
    const joinDate = new Date(joinDateStr);
    const now = new Date();

    const diffTime = Math.abs(now - joinDate);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    if (diffDays < 30) {
        return diffDays + " days";
    } else if (diffDays < 365) {
        const months = Math.floor(diffDays / 30);
        return months + (months === 1 ? " month" : " months");
    } else {
        const years = Math.floor(diffDays / 365);
        return years + (years === 1 ? " year" : " years");
    }
}