// Handle form submission with file upload
$('#movieForm').on('submit', function(e) {
    e.preventDefault();
    $('#submit-button').prop('disabled', true);
    var formData = new FormData(this);

    $.ajax({
        url: '/api/movies', // Your backend endpoint
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        success: function(response) {
            $('#responseMessage').text('Movie added successfully!').css('color', 'green');
            $('#movieForm')[0].reset();
            const event = new CustomEvent('movieAdded'); // This is the custom event
            window.dispatchEvent(event);
            location.reload();
        },
        error: function(xhr) {
            console.log('Error adding movie:', xhr.responseText);
            $('#responseMessage').text(`Error: ${xhr.responseText}`).css('color', 'red');
            $('#submit-button').prop('disabled', false);
        }
    });
});