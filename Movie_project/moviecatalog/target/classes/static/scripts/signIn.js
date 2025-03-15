function handleSignIn(event) {
    event.preventDefault();

    const username = document.getElementById('login-username').value;
    const password = document.getElementById('password').value;

    // Retrieve users array from localStorage
    const users = JSON.parse(localStorage.getItem("users")) || [];

    // Find user with matching username and password
    const user = users.find(user => user.username === username && user.password === password);

    if (user) {
        alert('Login successful!');

        // Store authentication data in localStorage
        localStorage.setItem('isSignedIn', 'true');
        localStorage.setItem('username', username);
        localStorage.setItem('userName', username);

        // Store user data
        localStorage.setItem('fullName', user.name);
        localStorage.setItem('password', password);

        // Handle join date
        if (!user.joinDate) {
            const joinDate = getCurrentDate();
            user.joinDate = joinDate;
            // Update the user in the users array
            const userIndex = users.findIndex(u => u.username === username);
            if (userIndex !== -1) {
                users[userIndex] = user;
                localStorage.setItem("users", JSON.stringify(users));
            }
            localStorage.setItem('joinDate', joinDate);
        } else {
            localStorage.setItem('joinDate', user.joinDate);
        }

        console.log("Username stored in localStorage:", localStorage.getItem('username'));
        console.log("UserName stored in localStorage:", localStorage.getItem('userName'));

        // Redirect to index page
        window.location.href = "../index.html";
    } else {
        alert('Invalid username or password!');
    }
}