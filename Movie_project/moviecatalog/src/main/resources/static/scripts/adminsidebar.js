// adminsidebar.js
document.addEventListener("DOMContentLoaded", function () {
    // Check if sidebar container exists
    const sidebarContainer = document.getElementById("sidebar-container");
    if (!sidebarContainer) {
        console.error("Sidebar container not found");
        return;
    }

    // Check if admin is signed in
    const isAdminSignedIn = localStorage.getItem('isAdminSignedIn') === 'true';

    if (!isAdminSignedIn) {
        // Redirect to admin sign in if not signed in
        alert('You must be signed in as an admin to access this page.');
        window.location.href = "../components/adminSignIn.html";
        return;
    }

    // Load sidebar
    fetch("../partial/sidebar.html")
        .then(response => response.text())
        .then(data => {
            sidebarContainer.innerHTML = data;

            // Add a slight delay to ensure DOM is updated
            setTimeout(() => {
                // Add click event listener to the sign-out link
                const signOutLink = document.getElementById('sign-out-link');
                if (signOutLink) {
                    signOutLink.addEventListener('click', function(event) {
                        event.preventDefault();

                        // Clear admin authentication data
                        localStorage.removeItem('isAdminSignedIn');
                        localStorage.removeItem('adminUsername');
                        localStorage.removeItem('adminPassword');

                        // Also clear regular user authentication data (if present)
                        localStorage.removeItem('isSignedIn');
                        localStorage.removeItem('username');
                        localStorage.removeItem('userName');
                        localStorage.removeItem('fullName');
                        localStorage.removeItem('password');
                        localStorage.removeItem('joinDate');

                        // Alert the user
                        alert('Successfully signed out!');

                        // Redirect to the admin sign-in page
                        window.location.href = "../components/adminSignIn.html";
                    });
                    console.log("Admin sidebar sign-out event listener attached");
                } else {
                    console.error("Sign-out link not found in sidebar");
                }
            }, 100);
        })
        .catch(error => console.error("Error loading the sidebar:", error));
});