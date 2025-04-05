// backButton.js

// Function to go back to the previous page
function goBack() {
    window.history.back(); // Goes back to the previous page in the browser history
}

// Function to dynamically add the back button to the page
function addBackButton() {
    const backButton = document.createElement('div');
    backButton.classList.add('back-button');
    backButton.innerText = '⬅';
    backButton.onclick = goBack;

    // Append the back button to the body of the page
    document.body.appendChild(backButton);
}

// Call the function to add the back button when the page loads
window.onload = addBackButton;
