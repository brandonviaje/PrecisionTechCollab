$(document).ready(function() {
  const actorId = new URLSearchParams(window.location.search).get('id');
  
  if (actorId) {
    const actorDetailsUrl = `https://api.themoviedb.org/3/person/${actorId}?api_key=cf334fe88eeddcdc728d651ffed41008`;

    $.getJSON(actorDetailsUrl)
      .done(function(actorData) {
        const { name, biography, place_of_birth, birthday, profile_path } = actorData;

        const actorProfileImage = profile_path ? `https://image.tmdb.org/t/p/w500/${profile_path}` : 'https://via.placeholder.com/150'; // Placeholder if no profile found

        $('#actor-name').text(name);
        $('#actor-profile').attr('src', actorProfileImage);
        $('#actor-biography').text(biography);
        $('#actor-place-of-birth').text(place_of_birth);
        $('#actor-birthday').text(birthday);
      })
      .fail(function(error) {
        console.error("Error fetching actor details:", error);
      });
  }
});
