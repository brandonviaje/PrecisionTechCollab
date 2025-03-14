function navigateToSignInPage() {
    const select = document.getElementById("signInSelect");
    const selectedValue = select.value;

    if (selectedValue === "user") {
        window.location.href = "../components/signIn.html";  // Replace with the correct user sign-in page
    } else if (selectedValue === "admin") {
        window.location.href = "../components/adminSignIn.html";  // Replace with the correct admin sign-in page
    }
}
