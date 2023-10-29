document.getElementById('addDishButton').disabled = true;

let selectedDishes = [];
let selectedDishCosts = {};
let selectedDishCounts = {};
let totalCost = 0;

function filterDishesByType() {
    let selectedType = document.getElementById('typeSelect').value;
    let dishes = document.getElementById('dishSelect').options;
    for (let i = 0; i < dishes.length; i++) {
        let dish = dishes[i];
        dish.style.display = dish.getAttribute('data-type-id') === selectedType ? '' : 'none';
    }
    document.getElementById('dishSelect').selectedIndex = -1;
    document.getElementById('addDishButton').disabled = true;
}

function addDish() {
    let dishSelect = document.getElementById("dishSelect");
    let dishId = dishSelect.value;
    let dishDetails = dishSelect.options[dishSelect.selectedIndex].text.split(" - ");
    let dishName = dishDetails[0];
    let dishCost = parseFloat(dishDetails[1]);

    selectedDishCounts[dishId] = (selectedDishCounts[dishId] || 0) + 1;
    selectedDishCosts[dishId] = dishCost;
    let selectedDishesDiv = document.getElementById("selectedDishes");
    let dishDiv = document.getElementById("dish" + dishId);
    let dishTextDiv;

    if (dishDiv) {
        dishTextDiv = dishDiv.querySelector(".dish-text");
        dishTextDiv.textContent = dishName + " - " + dishCost + " (x" + selectedDishCounts[dishId] + ")";
    } else {
        dishDiv = document.createElement("div");
        dishDiv.setAttribute("id", "dish" + dishId);

        dishTextDiv = document.createElement("div");
        dishTextDiv.classList.add("dish-text");
        dishTextDiv.textContent = dishName + " - " + dishCost + " (x" + selectedDishCounts[dishId] + ")";
        dishDiv.appendChild(dishTextDiv);

        let hiddenInput = document.createElement("input");
        hiddenInput.setAttribute("type", "hidden");
        hiddenInput.setAttribute("name", "dish_ids");
        hiddenInput.setAttribute("value", dishId);
        dishDiv.appendChild(hiddenInput);

        let addBtn = document.createElement("button");
        addBtn.textContent = "+";
        addBtn.classList.add("btn-adjust");
        addBtn.onclick = function () {
            changeDishCount(dishId, 1);
        };

        let removeBtn = document.createElement("button");
        removeBtn.textContent = "-";
        removeBtn.classList.add("btn-adjust");
        removeBtn.onclick = function () {
            changeDishCount(dishId, -1);
        };


        dishDiv.appendChild(addBtn);
        dishDiv.appendChild(removeBtn);

        selectedDishesDiv.appendChild(dishDiv);
    }
    totalCost += dishCost;
    document.getElementById("totalCost").textContent = "Совокупная стоимость: " + totalCost + "руб.";
}

document.getElementById('dishSelect').addEventListener('change', function () {
    document.getElementById('addDishButton').disabled = this.value === '';
});

function changeDishCount(dishId, countChange) {
    selectedDishCounts[dishId] += countChange;
    totalCost += selectedDishCosts[dishId] * countChange;
    let dishDiv = document.getElementById("dish" + dishId);
    let dishTextDiv = dishDiv.querySelector(".dish-text");
    let dishName = dishTextDiv.textContent.split(" - ")[0];
    dishTextDiv.textContent = dishName + " - " + selectedDishCosts[dishId] + " (x" + selectedDishCounts[dishId] + ")";

    if (selectedDishCounts[dishId] === 0) {
        dishDiv.remove();
        delete selectedDishCounts[dishId];
        const index = selectedDishes.indexOf(dishId);
        if (index > -1) {
            selectedDishes.splice(index, 1);
        }
    }
    document.getElementById("totalCost").textContent = "Совокупная стоимость: " + totalCost + "руб.";
}

function resetOrder() {
    selectedDishes = [];
    selectedDishCosts = {};
    selectedDishCounts = {};
    totalCost = 0;
    document.getElementById("totalCost").textContent = "Совокупная стоимость: " + totalCost + "руб.";
    ;
    let selectedDishesDiv = document.getElementById("selectedDishes");
    while (selectedDishesDiv.firstChild) {
        selectedDishesDiv.removeChild(selectedDishesDiv.firstChild);
    }
}


window.onload = function () {
    document.getElementById('typeSelect').value = "";
    document.getElementById('dishSelect').value = "";
    document.getElementById('table_id').value = "";
    filterDishesByType();
};

document.getElementById('tableSelect').addEventListener('change', function () {
    document.getElementById('table_id').value = this.value;
});

document.getElementById('orderForm').addEventListener('submit', function (event) {
    event.preventDefault();

    let dishIds = Array.from(document.querySelectorAll('input[name="dish_ids"]')).map(input => input.value);
    if (dishIds.length === 0) {
        alert("Пожалуйста, выберите хотя бы одно блюдо перед созданием заказа.");
        return;
    }

    let tableId = document.getElementById('table_id').value;

    let orderData = {
        dish_ids: dishIds,
        dish_counts: selectedDishCounts,
        table_id: tableId
    };

    console.log(orderData)
    fetch('/create-order', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(orderData)
    }).then(function (response) {
        if (response.ok) {
            console.log(JSON.stringify(orderData));
            resetOrder();
            document.getElementById('orderForm').reset();
            document.getElementById('typeSelect').value = "";
            document.getElementById('table_id').value = "";
            filterDishesByType();
            return response.json();
        } else {
            throw new Error('Ошибка сервера, код: ' + response.status);
        }
    }).then(function (data) {
        console.log('Заказ успешно создан:', data);
    }).catch(function (error) {
        console.error('Ошибка:', error);
    });
});
