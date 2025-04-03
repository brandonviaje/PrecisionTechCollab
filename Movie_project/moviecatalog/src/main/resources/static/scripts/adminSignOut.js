// Function to handle sign out
function signOut() {
    // Clear all admin authentication data from localStorage
    localStorage.removeItem('isAdminSignedIn');
    localStorage.removeItem('adminUsername');
    localStorage.removeItem('adminPassword');

    // Redirect to the admin sign in page
    window.location.href = "../components/adminSignIn.html";
}