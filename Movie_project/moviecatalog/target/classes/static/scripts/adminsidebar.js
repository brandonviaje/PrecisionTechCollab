// adminsidebar.js
document.addEventListener("DOMContentLoaded", function () {
    // Check if sidebar container exists
    const sidebarContainer = document.getElementById("sidebar-container");
    if (!sidebarContainer) {
        console.error("Sidebar container not found");
        return;
    }

    // Load sidebar
    fetch("../partial/sidebar.html")
        .then(response => response.text())
        .then(data => {
            sidebarContainer.innerHTML = data;
        })
        .catch(error => console.error("Error loading the sidebar:", error));
});