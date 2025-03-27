document.addEventListener("DOMContentLoaded", function () {
    // Load header
    fetch("../partial/header.html")
        .then(response => response.text())
        .then(data => {
            document.getElementById("header-container").innerHTML = data;

            setTimeout(() => {
                initializeHeaderUI();
                initializeSearchButton();
            }, 100);
        })
        .catch(error => console.error("Error loading the header:", error));

    // Load footer
    fetch("../partial/footer.html")
        .then(response => response.text())
        .then(data => {
            document.getElementById("footer-container").innerHTML = data;
        })
        .catch(error => console.error("Error loading the footer:", error));
});

function initializeSearchButton() {
    const searchInput = document.getElementById('search-query');
    const searchButton = document.getElementById('search-btn'); // Assuming your search button has the ID 'search-btn'

    // Function to toggle search button state based on input value
    function toggleSearchButton() {
        if (searchInput.value.trim() === '') {
            searchButton.disabled = true;  // Disable the button if the input is empty
        } else {
            searchButton.disabled = false;  // Enable the button if there is text in the input
        }
    }

    // Add event listener to the search input field
    searchInput.addEventListener('input', toggleSearchButton);

    // Initialize the button state when the page loads
    toggleSearchButton();
}


// Initialize header UI based on login status
function initializeHeaderUI() {
    const isUserSignedIn = localStorage.getItem('isSignedIn');
    const isAdminSignedIn = localStorage.getItem('isAdminSignedIn');

    // Check both possible storage keys for username compatibility
    const userName = localStorage.getItem('userName') || localStorage.getItem('username');
    const adminUserName = localStorage.getItem('adminUsername');

    console.log("User login status:", isUserSignedIn);
    console.log("Username:", userName);
    console.log("Admin login status:", isAdminSignedIn);
    console.log("Admin username:", adminUserName);

    // Get UI elements - use querySelectorAll
    const signInSection = document.querySelector('#signInSection');
    const signUpBtn = document.querySelector('#signUpBtn');
    const userSection = document.querySelector('#userSection');
    const adminSection = document.querySelector('#adminSection');
    const usernameElement = document.querySelector('#username');
    const adminUsernameElement = document.querySelector('#adminUsername');
    const signOutBtn = document.querySelector('#signOutBtn');
    const adminSignOutBtn = document.querySelector('#adminSignOutBtn');

    // Skip if elements aren't found (might happen if header isn't fully loaded)
    if (!signInSection || !signUpBtn) {
        console.warn("Basic header elements not found. Header might not be fully loaded.");
        return;
    }

    // Update UI based on login status
    if (isAdminSignedIn === 'true' && adminUserName) {
        // ADMIN IS LOGGED IN
        signInSection.style.display = 'none'; // Hide Sign In section
        signUpBtn.style.display = 'none'; // Hide Sign Up button

        // Make sure user section is hidden if it exists
        if (userSection) {
            userSection.style.display = 'none';
        }

        // Show admin section if it exists
        if (adminSection) {
            adminSection.style.display = 'flex';
            if (adminUsernameElement) {
                adminUsernameElement.textContent = adminUserName;
            }
        }

        console.log("Admin is signed in, updated header UI");

        // Set up admin sign out functionality
        if (adminSignOutBtn) {
            adminSignOutBtn.addEventListener('click', function () {
                localStorage.removeItem('isAdminSignedIn');
                localStorage.removeItem('adminUsername');
                localStorage.removeItem('adminPassword');

                console.log("Admin signed out");
                window.location.reload();
            });
        }
    } else if (isUserSignedIn === 'true' && userName) {
        // REGULAR USER IS LOGGED IN
        signInSection.style.display = 'none'; // Hide Sign In section
        signUpBtn.style.display = 'none'; // Hide Sign Up button

        // Hide admin section if it exists
        if (adminSection) {
            adminSection.style.display = 'none';
        }

        // Show user section if it exists
        if (userSection) {
            userSection.style.display = 'flex';
            if (usernameElement) {
                usernameElement.textContent = userName;
            }
        }

        console.log("User is signed in, updated header UI");

        // Set up user sign out functionality
        if (signOutBtn) {
            signOutBtn.addEventListener('click', function () {
                localStorage.removeItem('isSignedIn');
                localStorage.removeItem('userName');
                localStorage.removeItem('username');
                localStorage.removeItem('password');

                console.log("User signed out");
                window.location.reload(); // Reload to update UI
            });
        }
    } else {
        // NO ONE IS LOGGED IN
        signInSection.style.display = 'inline-block'; // Show Sign In section
        signUpBtn.style.display = 'inline-block'; // Show Sign Up button

        // Hide user and admin sections if they exist
        if (userSection) {
            userSection.style.display = 'none';
        }
        if (adminSection) {
            adminSection.style.display = 'none';
        }

        console.log("No one is signed in, updated header UI");
    }
}

// Function to sign in a user (can be called from other scripts)
function signIn(userName) {
    localStorage.setItem('isSignedIn', 'true');
    localStorage.setItem('userName', userName);

    console.log("User signed in programmatically:", userName);
    window.location.reload();
}

// Function to sign in an admin (can be called from other scripts)
function adminSignIn(adminUserName) {
    localStorage.setItem('isAdminSignedIn', 'true');
    localStorage.setItem('adminUsername', adminUserName);

    console.log("Admin signed in programmatically:", adminUserName);
    window.location.reload();
}
export function isUserSignedIn() {
    return localStorage.getItem('isSignedIn') === 'true';
}

export function isAdminSignedIn() {
    return localStorage.getItem('isAdminSignedIn') === 'true';
}

// Run initialization on window load as well, to catch cases where the header was already loaded
window.onload = function () {
    // If header is already in the DOM, initialize it
    if (document.querySelector('#signInSection')) {
        initializeHeaderUI();
    }
};