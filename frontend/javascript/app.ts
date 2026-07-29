interface DogApiResponse {
  message: string;
  status: string;
}

async function fetchDogImage(): Promise<void> {
  const resultDiv = document.getElementById("result") as HTMLDivElement | null;

  if (!resultDiv) {
    console.error("Result div not found");
    return;
  }

  resultDiv.innerHTML = "";

  try {
    const response = await fetch("https://dog.ceo/api/breeds/image/random");
    if (!response.ok) {
      throw new Error(`Network response was not okay: ${response.statusText}`);
    }
    const data: DogApiResponse = await response.json();

    // Create image element and data
    const img: HTMLImageElement = document.createElement("img");
    img.id = "dog-image";
    img.src = data.message;
    img.alt = "Random Dog Image";

    const urlParam: HTMLParagraphElement = document.createElement("p");
    urlParam.textContent = `Image URL: ${data.message}`;

    // Append image and URL to result div
    resultDiv.appendChild(img);
    resultDiv.appendChild(urlParam);
  } catch (error) {
    console.error("Error fetching dog image:", error);

    const errorMsg: HTMLParagraphElement = document.createElement("p");
    errorMsg.id = "error-message";
    
    if (error instanceof Error) {
      errorMsg.textContent = `Error fetching dog image: ${error.message}`;
    } else {
      errorMsg.textContent = "An unknown error occurred while fetching the dog image.";
    }

    resultDiv.appendChild(errorMsg);
  }
}

// Add event listener to the button to fetch a new dog image when clicked
const fetchButton = document.getElementById("fetchImage") as HTMLButtonElement | null;

fetchButton?.addEventListener("click", (_event: MouseEvent): void => {
  fetchDogImage();
});

// Fetch a dog image when the page loads
window.addEventListener("load", (_event: Event): void => {
  fetchDogImage();
});