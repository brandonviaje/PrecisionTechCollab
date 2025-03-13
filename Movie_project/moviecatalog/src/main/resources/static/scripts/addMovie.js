$(document).ready(function() {
    $('#movieForm').submit(function(event) {
        event.preventDefault();

        // Get JWT token from localStorage
        const token = localStorage.getItem("jwtToken");
        var formData = new FormData();

        // Add movie details to FormData object
        formData.append('title', $('#title').val());
        formData.append('releaseDate', $('#releaseDate').val());
        formData.append('pgRating', $('#pgRating').val());
        formData.append('synopsis', $('#synopsis').val());
        formData.append('genres', $('#genres').val());
        formData.append('productionCompanies', $('#productionCompanies').val());
        formData.append('runtime', $('#runtime').val());
        formData.append('productionCountries', $('#productionCountries').val());
        formData.append('spokenLanguages', $('#spokenLanguages').val());

        // append the movie poster file to the FormData object
        var posterFile = $('#poster')[0].files[0];
        if (posterFile) {
            formData.append('poster', posterFile);
        }

        // Send AJAX request to backend for movie data and poster upload
        $.ajax({
            url: 'http://localhost:8080/api/movies',  // API endpoint for movie submission
            type: 'POST',
            data: formData,
            processData: false,  // Don't let jQuery process the data
            contentType: false,
            headers: {
                'Authorization': `Bearer ${token}` // Send JWT token in Authorization header
            },
            success: function(response) {
                console.log('Movie added:', response);
                $('#responseMessage').text('Movie added successfully!').css('color', 'green');
                $('#movieForm')[0].reset();  // Clear form fields on success
            },
            error: function(xhr) {
                console.log('Error adding movie:', xhr.responseText);
                $('#responseMessage').text(`Error: ${xhr.responseText}`).css('color', 'red');
            }
        });
    });
});
