// app.js

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
    fetch("http://localhost:35000/")
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

    form.addEventListener("submit", function(event) {
        event.preventDefault();
        alert("Order submitted! Thank you.");
        getHome(); 
    });
}
// Asigna eventos al cargar la página
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("btnMenu").addEventListener("click", getMenu);
    document.getElementById("btnPedido").addEventListener("click", showForm);
});
