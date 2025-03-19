$(document).ready(function() {
    const movieId = new URLSearchParams(window.location.search).get('id');

    if (movieId) {
        const apiUrl = `/api/movies/${movieId}`;
        $.getJSON(apiUrl)
            .done(function(data) {
                console.log(data);
                const {title, releaseDate, pgRating, poster_path, productionCompanies, spokenLanguages, runtime, genres, synopsis} = data

                // Populate the form fields
                $('#title').val(title);
                $('#releaseDate').val(releaseDate);
                $('#pgRating').val(pgRating);
                $('#synopsis').val(synopsis);
                $('#genres').val(genres);
                $('#runtime').val(runtime);
                $('#productionCompanies').val(productionCompanies);
                $('#spokenLanguages').val(spokenLanguages.split(',').join(', '));

            }).fail(function(error) {
            console.error("Error fetching movie details:", error);
            alert('Error fetching movie details. Please try again.');
        });

        // Handle the form submission
        $('#update-movie-form').submit(function(event) {
            event.preventDefault();
            $('#submit-button').prop('disabled', true);
            var formData = new FormData(this);

            $.ajax({
                url: `/api/movies/${movieId}/update`,
                type: 'PATCH',
                data: formData,
                contentType: false,
                processData: false,
                success: function(response) {
                    $('#responseMessage').text('Movie updated successfully!').css('color', 'green');
                },
                error: function(xhr) {
                    $('#responseMessage').text(`Error: ${xhr.responseText}`).css('color', 'red');
                }
            });

        });
    } else {
        alert('No movie selected!');
    }
});