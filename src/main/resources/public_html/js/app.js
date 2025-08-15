// Asigna eventos al cargar la página
document.addEventListener("DOMContentLoaded", function () {
    // document.getElementById("btnMenu").addEventListener("click", getMenu);
    // document.getElementById("btnPedido").addEventListener("click", showForm);
    document.getElementById("btnSubmit").addEventListener("click", loadPostOptions);
});
let userName = null;

function loadPostOptions() {
    const nameValue = document.getElementById("nameInput").value.trim();
    if (!nameValue) {
        alert("Por favor ingresa tu nombre");
        return;
    }

    const nameEncoded = encodeURIComponent(nameValue);

    fetch("/options?name=" + nameEncoded, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name: nameValue })
    })
        .then(response => {
            userName = response.url.split("name=")[1];
            getOptions(userName);
            response.text()

      })
        .catch(err => console.error("Error al cargar las opciones:", err));
}
function getOptions(data) {
    console.log("Data received:", data);
    fetch("http://localhost:35000/layouts/options.html")
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error HTTP: ${response.status}`);
            }
            return response.text();
        })
        .then(html => {
            document.body.innerHTML = html;
            if (data) {
                const section = document.getElementById("continer");
                if (section) {
                    const greeting = document.createElement("p");
                    greeting.id = "greeting";
                    greeting.textContent = `Welcome ${data}!`;
                    section.insertBefore(greeting, section.firstChild);
                }
            }
        })
        .catch(err => {
            console.error("No se pudo cargar las opciones:", err);
        });
}

function getMenu() {
    fetch("http://localhost:35000/layouts/menu.html")
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error HTTP: ${response.status}`);
            }
            return response.text();
        })
        .then(html => {
            document.body.innerHTML = html;
        })
        .catch(err => {
            console.error("No se pudo cargar el menú:", err);
        });
}
function getHome() {
    getOptions(userName);
}
function showForm() {
    fetch("http://localhost:35000/layouts/order.html")
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error HTTP: ${response.status}`);
            }
            return response.text();
        })
        .then(html => {
            document.body.innerHTML = html;
            Order();
        })
        .catch(err => {
            console.error("No se pudo cargar el formulario:", err);
        });
}

function Order() {
    const form = document.getElementById("orderForm");
    if (!form) {
        console.error("No se encontró el formulario");
        return;
    }

    form.addEventListener("submit", function (event) {
        event.preventDefault();
        alert("Order submitted! Thank you.");
        getHome();
    });
}
