function fetchDogImage() {
  const resultDiv = document.getElementById("result");
  resultDiv.innerHTML = ''; // Clear previous results

  fetch("https://dog.ceo/api/breeds/image/random")
    .then((response) => {
      // Check if the response is ok
      if (!response.ok) {
        throw new Error("Network response was not ok: " + response.statusText);
      }
      return response.json();
    })
    .then((data) => {
      // Create an image element and set its source to the fetched dog image URL
      const img = document.createElement("img");
      img.id = "dog-image";
      img.src = data.message;
      img.alt = "Random Dog";

      const urlParam = document.createElement("p");
      urlParam.textContent = "Image URL: " + data.message;

      resultDiv.appendChild(img);
      resultDiv.appendChild(urlParam);
    })
    .catch((error) => {
      console.error("There was a problem with the fetch operation:", error);
      const errorMessage = document.createElement("p");
      errorMessage.id = "error-message";
      errorMessage.textContent = "Error fetching dog image: " + error.message;
      resultDiv.appendChild(errorMessage);
    });
}

// Add event listener to the button to fetch a new dog image when clicked
document.getElementById("fetchImage").addEventListener("click", fetchDogImage);

// Fetch a dog image when the page loads
window.addEventListener("load", fetchDogImage);
