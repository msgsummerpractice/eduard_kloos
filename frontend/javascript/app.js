"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
function fetchDogImage() {
    return __awaiter(this, void 0, void 0, function* () {
        const resultDiv = document.getElementById("result");
        if (!resultDiv) {
            console.error("Result div not found");
            return;
        }
        resultDiv.innerHTML = "";
        try {
            const response = yield fetch("https://dog.ceo/api/breeds/image/random");
            if (!response.ok) {
                throw new Error(`Network response was not okay: ${response.statusText}`);
            }
            const data = yield response.json();
            // Create image element and data
            const img = document.createElement("img");
            img.id = "dog-image";
            img.src = data.message;
            img.alt = "Random Dog Image";
            const urlParam = document.createElement("p");
            urlParam.textContent = `Image URL: ${data.message}`;
            // Append image and URL to result div
            resultDiv.appendChild(img);
            resultDiv.appendChild(urlParam);
        }
        catch (error) {
            console.error("Error fetching dog image:", error);
            const errorMsg = document.createElement("p");
            errorMsg.id = "error-message";
            if (error instanceof Error) {
                errorMsg.textContent = `Error fetching dog image: ${error.message}`;
            }
            else {
                errorMsg.textContent = "An unknown error occurred while fetching the dog image.";
            }
            resultDiv.appendChild(errorMsg);
        }
    });
}
// Add event listener to the button to fetch a new dog image when clicked
const fetchButton = document.getElementById("fetchImage");
fetchButton === null || fetchButton === void 0 ? void 0 : fetchButton.addEventListener("click", (_event) => {
    fetchDogImage();
});
// Fetch a dog image when the page loads
window.addEventListener("load", (_event) => {
    fetchDogImage();
});
//# sourceMappingURL=app.js.map