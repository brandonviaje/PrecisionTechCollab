const Url = 'https://api.themoviedb.org/3/person/popular?api_key=cf334fe88eeddcdc728d651ffed41008';

function fetchActors() {
  $.getJSON(Url)
    .done(function (data) {
      data.results.forEach(function (media) {
        const actorCard = createActorCard(media);
        $(".actors").append(actorCard); // Append to movies div
      });
    })
    .fail(function (error) {
      console.error("Error Fetching data:", error);
    });
}

function createActorCard(media) {
  const { name, profile_path, id } = media;
  return `
    <div class="actor-item">
        <div class="actor-photo-container">
          <a href="/components/actorDetails.html?id=${id}">
            <img src="https://image.tmdb.org/t/p/w300/${profile_path}" class="actor_img_rounded" alt="${name}">
          </a>
        </div>
        <div class="actor-name">${name}</div>
    </div>
  `;
}

$(document).ready(fetchActors);