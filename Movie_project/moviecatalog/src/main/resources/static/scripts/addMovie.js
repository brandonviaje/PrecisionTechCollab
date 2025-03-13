$(document).ready(function() {
    $('#movieForm').submit(function(event) {
        event.preventDefault();

        // Get JWT token from localStorage
        const token = localStorage.getItem("jwtToken");

        // Prepare movie data object
        const movieData = {
            title: $('#title').val(),
            releaseDate: $('#releaseDate').val(),
            pgRating: $('#pgRating').val(),
            synopsis: $('#synopsis').val(),
            genres: $('#genres').val(),
            productionCompanies: $('#productionCompanies').val(),
            runtime: $('#runtime').val(),
            productionCountries: $('#productionCountries').val(),
            spokenLanguages: $('#spokenLanguages').val()
        };

        // Send AJAX request to backend
        $.ajax({
            url: 'http://localhost:8080/api/movies',
            type: 'POST',
            contentType: 'application/json',
            headers: {
                'Authorization': `Bearer ${token}`, // Send JWT token in Authorization header
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(movieData),
            success: function(response) {
                console.log('Movie added:', response);
                $('#responseMessage').text('Movie added successfully!').css('color', 'green');
                $('#movieForm')[0].reset();
            },
            error: function(xhr) {
                console.log('Error adding movie:', xhr.responseText);
                $('#responseMessage').text(`Error: ${xhr.responseText}`).css('color', 'red');
            }
        });
    });
});