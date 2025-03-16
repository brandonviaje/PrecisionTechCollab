$(document).ready(function() {
    // Get actor_id from URL query parameters
    const actorId = new URLSearchParams(window.location.search).get('id');

    if (actorId) {
        // Request to the TMDb API to get actor details
        const actorDetailsUrl = `https://api.themoviedb.org/3/person/${actorId}?api_key=cf334fe88eeddcdc728d651ffed41008`;

        $.getJSON(actorDetailsUrl)
            .done(function(actorData) {
                // Destructure the actor data response from TMDb
                const { name, biography, place_of_birth, birthday, profile_path } = actorData;

                // Set the values to the HTML elements
                $('#actor-name').text(name);
                $('#actor-biography').text(biography);
                $('#actor-place-of-birth').text(place_of_birth);
                $('#actor-birthday').text(birthday);

                // If a profile path exists, set the image source
                const profileUrl = profile_path ? `https://image.tmdb.org/t/p/w500${profile_path}` : 'https://via.placeholder.com/500';
                $('#actor-profile').attr('src', profileUrl);
            })
            .fail(function(error) {
                console.error("Error fetching actor details:", error);
            });
    }
});
