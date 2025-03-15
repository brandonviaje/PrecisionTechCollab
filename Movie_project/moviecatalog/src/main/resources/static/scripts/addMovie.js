// Handle form submission with file upload
$('#movieForm').on('submit', function(e) {
    e.preventDefault();

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
        },
        error: function(xhr) {
            console.log('Error adding movie:', xhr.responseText);
            $('#responseMessage').text(`Error: ${xhr.responseText}`).css('color', 'red');
        }
    });
});