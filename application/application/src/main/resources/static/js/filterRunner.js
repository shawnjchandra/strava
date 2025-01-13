let sortBy = "id_runner"; 
let sortOrder = "asc";    

function sortTable(column) {

    console.log(column);
    if (sortBy === column) {
        sortOrder = sortOrder === "asc" ? "desc" : "asc";
    } else {
        sortBy = column;
        sortOrder = "asc";
    }

    let heads = {
        'id_runner': document.getElementById('id_runner'),
    };

 

    Object.keys(heads).forEach(key => {
        let head = heads[key];
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
    let keywords = document.querySelector("input[name='runnerName']").value || "";

    fetch(`/admin/filter/runner?runnerName=${keywords}&sortBy=${sortBy}&sortOrder=${sortOrder}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            
            updateTable(result.data);

        } else {
            // Better error handling
            
            let tbody = document.querySelector(".tbody-runner");
            tbody.innerHTML = `<tr><td colspan="6" style="text-align: center;">${result.message || "No activities found!"}</td></tr>`;
        }
    })
    .catch(error => {
        console.error('Error:', error);
        let tbody = document.querySelector(".tbody-runner");
        tbody.innerHTML = `<tr><td colspan="6" style="text-align: center;">An error occurred while fetching data.</td></tr>`;
    });
}

function updateTable(runners) {
    let tbody = document.querySelector(".tbody-runner");
    tbody.innerHTML = "";

    runners.forEach(runner => {
        let row = document.createElement('tr');
        row.innerHTML = `
            <td>${runner.id_runner || ''}</td>
            <td>${runner.user.nama || ''}</td>`

            if(runner.active){
                row.innerHTML += `
            
                    <td><span class="status-active">Active</span></td>
                    <td>
                        <button class="btn-active" data-id="${runner.id_runner}">
                            BAN
                        </button>
                    </td>
                
         
                `;
            }else{
                row.innerHTML += `
             
                    <td ><span class="status-inactive">Inactive</span></td>
                    <td>
                        <button class="btn-inactive" data-id="${runner.id_runner}">
                            UNBAN
                        </button>
                    </td>
                    
        
                `;
            }
            
            
     
    
        ;
        tbody.appendChild(row);
    });
}



// Event listeners
document.addEventListener('DOMContentLoaded', function() {

    // Search button click handler
    document.querySelector('.search-button-runner').addEventListener('click', fetchActivities);

    // Handle enter key in search input
    document.querySelector("input[name='runnerName']").addEventListener('keypress', function(e) {

        console.log(e);
        if (e.key === 'Enter') {
            fetchActivities();
        }
    });


});

$(document).ready(function(){
    $(".clear-runner").on("click", function(){
        window.location.href = "/admin/index";  
        window.location.reload();
    });
});