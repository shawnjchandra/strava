let sortByRace = "judul"; 
let sortOrderRace = "asc";    

function sortRaceTable(column) {

    console.log(column);
    if (sortByRace === column) {
        sortOrderRace = sortOrderRace === "asc" ? "desc" : "asc";
    } else {
        sortByRace = column;
        sortOrderRace = "asc";
    }

    let heads = {
        'judul': document.getElementById('judul'),
        'createdAt': document.getElementById('createdAt'),
        'kuota_max': document.getElementById('kuota_max'),
        'jarak': document.getElementById('jarak')

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

    fetchRaceActivities();
}

function fetchRaceActivities() {
    let keywords = document.querySelector("input[name='raceName']").value || "";

    fetch(`/admin/filter/race?raceName=${keywords}&sortBy=${sortByRace}&sortOrder=${sortOrderRace}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            
            updateRaceTable(result.data);

        } else {
            // Better error handling
            
            let tbody = document.querySelector(".tbody-race");
            tbody.innerHTML = `<tr><td colspan="6" style="text-align: center;">${result.message || "No activities found!"}</td></tr>`;
        }
    })
    .catch(error => {
        console.error('Error:', error);
        let tbody = document.querySelector(".tbody-race");
        tbody.innerHTML = `<tr><td colspan="6" style="text-align: center;">An error occurred while fetching data.</td></tr>`;
    });
}

function updateRaceTable(runners) {
    let tbody = document.querySelector(".tbody-race");
    tbody.innerHTML = "";

    runners.forEach(race => {
        let row = document.createElement('tr');
        row.innerHTML = `
            <td>${race.judul || ''}</td>
            <td>${race.jarak ? race.jarak + ' km' : ''}</td>
            <td>${race.createdAt || ''}</td>
            <td>${race.kuota_max || ''}</td>
            <td>
                <a href="@{/race/challenge/${race.idActivity}}" class="go-challenge">
                    Check
                </a>
            </td>
            
            

        
        `
        ;
        tbody.appendChild(row);
    });
}



// Event listeners
document.addEventListener('DOMContentLoaded', function() {

    // Search button 
    document.querySelector('.search-button-race').addEventListener('click', fetchRaceActivities);

    // untuk key Enter
    document.querySelector("input[name='raceName']").addEventListener('keypress', function(e) {

        // console.log(e);
        if (e.key === 'Enter') {
            fetchRaceActivities();
        }
    });


});

$(document).ready(function(){
    $(".clear-race").on("click", function(){
        window.location.href = "/admin/index";  
        window.location.reload();
    });
});