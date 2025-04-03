document.addEventListener("DOMContentLoaded", function () {
    // First, ensure the CSS is loaded
    ensureAccessibilityCSSLoaded();

    // Check for and initialize the contrast toggle button
    const button = document.getElementById("toggle-contrast");

    // Apply saved preference regardless of button presence
    const savedContrast = localStorage.getItem("highContrast");
    if (savedContrast === "true") {
        document.documentElement.classList.add("high-contrast");
        console.log(" Applied saved high contrast setting");
    }

    // Only set up button functionality if the button exists on this page
    if (!button) {
        console.log(" No contrast toggle button on this page");
        return;
    } else {
        console.log(" Button found.");

        // Update button text based on current state
        button.textContent = document.documentElement.classList.contains("high-contrast")
            ? "Disable High Contrast"
            : "Enable High Contrast";
    }

    function toggleContrast(event) {
        // Prevent the default action (e.g., page refresh)
        event.preventDefault();

        // Toggle the high-contrast class on the HTML element for global effect
        document.documentElement.classList.toggle("high-contrast");

        // Update localStorage after toggling
        const isHighContrast = document.documentElement.classList.contains("high-contrast");
        localStorage.setItem("highContrast", isHighContrast ? "true" : "false");

        // Update button text
        button.textContent = isHighContrast ? "Disable High Contrast" : "Enable High Contrast";
        console.log(" High contrast mode set to:", isHighContrast);
    }

    // Attach the event listener
    button.addEventListener("click", toggleContrast);
});

// Function to ensure the CSS file is loaded
function ensureAccessibilityCSSLoaded() {
    const cssPath = "../css/access.css";

    // Check if the stylesheet is already loaded
    let alreadyLoaded = false;
    const stylesheets = document.querySelectorAll('link[rel="stylesheet"]');

    stylesheets.forEach(sheet => {
        if (sheet.href.includes("access.css")) {
            alreadyLoaded = true;
        }
    });

    // If not loaded, add it to the head
    if (!alreadyLoaded) {
        const link = document.createElement('link');
        link.rel = 'stylesheet';
        link.href = cssPath;
        document.head.appendChild(link);
        console.log(" Accessibility CSS was auto-loaded");
    } else {
        console.log("Accessibility CSS was already loaded");
    }
}