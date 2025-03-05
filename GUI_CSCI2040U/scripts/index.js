const apiUrl = 'https://api.themoviedb.org/3/trending/movie/week?api_key=cf334fe88eeddcdc728d651ffed41008';

function fetchMovies() {
  $.getJSON(apiUrl)
    .done(function (data) {
      data.results.forEach(function (media) {
        const movieCard = createMovieCard(media);
        $(".movies").append(movieCard); // Append to movies div
      });
    })
    .fail(function (error) {
      console.error("Error Fetching data:", error);
    });
}

function createMovieCard(media) {
  const { title, name, poster_path, id } = media;
  return `
    <div class="movie_item">
      <a href="components/movieDetails.html?id=${id}">
        <img src="https://image.tmdb.org/t/p/w300/${poster_path}" class="movie_img_rounded" alt="${title || name}">
      </a>
      <div class="title">${title || name}</div>
    </div>
  `;
}

// Ensure jQuery runs the function after the page loads
$(document).ready(fetchMovies);
