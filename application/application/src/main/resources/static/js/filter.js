let sortBy = "createdat"; 
let sortOrder = "asc";    

function sortTable(column) {

    console.log(column);
    if (sortBy === column) {
        sortOrder = sortOrder === "asc" ? "desc" : "asc";
    } else {
        sortBy = column;
        sortOrder = "asc";
    }

    const heads = {
        'judul': document.getElementById('judul'),
        'createdAt': document.getElementById('createdAt'),
        'durasi': document.getElementById('durasi'),
        'jarak': document.getElementById('jarak'),
        'elevasi': document.getElementById('elevasi')
    };

    // console.table(heads);

    Object.keys(heads).forEach(key => {
        const head = heads[key];
        if (key === column) {
            // Set active column icon based on sort order
            head.className = sortOrder === 'asc' 
                ? 'fas fa-sort-up sort-icon sort-icon-asc' 
                : 'fas fa-sort-down sort-icon sort-icon-desc';
        } else {
            // Reset other columns to default
            head.className = 'fa-regular fa-equals';
        }
    });

    fetchActivities();
}

function fetchActivities() {
    const keywords = document.querySelector("input[name='keywords']").value || "";
    const actType = document.querySelector(".sport-select").value || "";
    const page = 0; // For pagination


    fetch(`/activity/filter?page=${page}&keywords=${keywords}&actType=${actType}&sortBy=${sortBy}&sortOrder=${sortOrder}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            
            updateTable(result.data.data);
            updatePagination(result.currentPage, result.totalItems, result.itemsPerPage, "/activity");
            document.getElementById('quantity').textContent = result.data.data.length + " Activities";

        } else {
            // Better error handling
            
            const tbody = document.querySelector("tbody");
            tbody.innerHTML = `<tr><td colspan="6" style="text-align: center;">${result.message || "No activities found!"}</td></tr>`;
        }
    })
    .catch(error => {
        console.error('Error:', error);
        const tbody = document.querySelector("tbody");
        tbody.innerHTML = `<tr><td colspan="6" style="text-align: center;">An error occurred while fetching data.</td></tr>`;
    });
}

function updateActQuantity(data){
    
}

function updateTable(activities) {
    const tbody = document.querySelector("tbody");
    tbody.innerHTML = "";

    activities.forEach(activity => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${activity.judul || ''}</td>
            <td>${activity.tipeTraining || ''}</td>
            <td onclick="sortTable('createdAt')">${formatDate(activity.createdAt) || ''}</td>
            <td onclick="sortTable('durasi')">${activity.durasi || ''}</td>
            <td>${activity.jarak ? activity.jarak + ' km' : ''}</td>
            <td>${activity.elevasi ? activity.elevasi + ' meter' : ''}</td>
            <td>
            <form action="/activity/edit" method="get" style="display: inline;">
                <input type="hidden" name="id_activity" value="${activity.idActivity}">
                <button type="submit" class="edit-button">Edit</button>
            </form>
            <form action="/activity/delActivity" method="post" style="display: inline;">
                <input type="hidden" name="id_activity" value="${activity.idActivity}">
                <button type="submit" class="delete-button">Delete</button>
            </form>
        </td>
        `;
        tbody.appendChild(row);
    });
}

function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.getFullYear() + '-' + 
           String(date.getMonth() + 1).padStart(2, '0') + '-' + 
           String(date.getDate()).padStart(2, '0') + ' ' + 
           String(date.getHours()).padStart(2, '0') + ':' + 
           String(date.getMinutes()).padStart(2, '0');
}

function updatePagination(currentPage, totalItems, itemsPerPage, url){
    const totalPages = Math.ceil(totalItems / itemsPerPage);
    const paginationDiv = document.querySelector(".pagination");
    
    let paginationHTML = "";

    if(currentPage > 0){
        paginationHTML += `<a th:href="@{${url}(page=${currentPage - 1})}">&lt; Previous</a>`;
    }

    for(let i =0; i< totalPages; i++){
        paginationHTML += `<a th:href="@{${url}(page=${i})}" 
               th:text="${i + 1}"
               th:class="${currentPage == i ? 'active' : ''}">1</a>`;
    }

    if(currentPage < totalPages - 1){
        paginationHTML += `<a th:href="@{${url}(page=${currentPage + 1})}">Next &gt;</a>`;
    }

    paginationDiv.innerHTML = paginationHTML;
}

function navigatePage(page){
    currentPage = page;
    fetchActivities();
}



// Event listeners
document.addEventListener('DOMContentLoaded', function() {
    // Initial fetch
    // fetchActivities();

    // Search button click handler
    document.querySelector('.search-button').addEventListener('click', fetchActivities);

    // Handle enter key in search input
    document.querySelector("input[name='keywords']").addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            fetchActivities();
        }
    });

    // Sport type change handler
    document.querySelector('.sport-select').addEventListener('change', fetchActivities);
});

$(document).ready(function(){
    $(".clear").on("click", function(){
        window.location.href = "/activity";  // Simple redirect to reset all filters
        window.location.reload();
    });
});