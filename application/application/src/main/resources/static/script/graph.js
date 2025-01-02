// Global chart variable
let myChart = null;

function processActivitiesData(activities, period) {
    const currentDate = new Date();
    let filteredActivities;
    
    // Ubah activities dari objek jadi array
    let activitiesArray = [];
    if (activities && typeof activities === 'object') {
        activitiesArray = Object.keys(activities).map(key => activities[key]);
    }


    switch(period) {
        case 'week':
            const weekAgo = new Date(currentDate.getTime() - 7 * 24 * 60 * 60 * 1000);
            filteredActivities = activitiesArray.filter(activity => 
                new Date(activity.createdAt) >= weekAgo
            );
            break;
        case 'month':
            const monthAgo = new Date(currentDate.getTime() - 30 * 24 * 60 * 60 * 1000);
            filteredActivities = activitiesArray.filter(activity => 
                new Date(activity.createdAt) >= monthAgo
            );
            break;
        case 'year':
            const yearAgo = new Date(currentDate.getTime() - 365 * 24 * 60 * 60 * 1000);
            filteredActivities = activitiesArray.filter(activity => 
                new Date(activity.createdAt) >= yearAgo
            );
            break;
        default:
            filteredActivities = activitiesArray;
    }


    const groupedData = new Map();
    
    filteredActivities.forEach(activity => {
        const date = new Date(activity.createdAt);
        let key;
        
        switch(period) {
            case 'week':
                key = date.toLocaleDateString('en-US', { weekday: 'short' });
                break;
            case 'month':
                key = date.toLocaleDateString('en-US', { day: 'numeric' });
                break;
            case 'year':
                key = date.toLocaleDateString('en-US', { month: 'short' });
                break;
        }
        
        const currentTotal = groupedData.get(key) || 0;
        groupedData.set(key, currentTotal + parseFloat(activity.jarak || 0));
    });


    let sortedLabels;
    const weekDays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    
    switch(period) {
        case 'week':
            sortedLabels = weekDays;
            break;
        case 'month':
            sortedLabels = Array.from({length: 31}, (_, i) => String(i + 1));
            break;
        case 'year':
            sortedLabels = months;
            break;
    }

    const distances = sortedLabels.map(label => groupedData.get(label) || 0);

    return {
        labels: sortedLabels,
        distances: distances
    };
}

function updateChart(period) {
    const ctx = document.getElementById('myChart').getContext('2d');
    

    if (myChart) {
        myChart.destroy();
    }
    
    const processedData = processActivitiesData(window.activitiesData, period);
    
    myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: processedData.labels,
            datasets: [{
                label: 'Jarak (km)',
                data: processedData.distances,
                borderColor: 'rgba(75, 192, 192, 1)',
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderWidth: 2,
                tension: 0.1,
                fill: true
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: `Activity Distance (${period})`
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return `Distance: ${context.parsed.y.toFixed(1)} km`;
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Distance (km)'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: period === 'week' ? 'Day' : period === 'month' ? 'Date' : 'Month'
                    }
                }
            }
        }
    });
}


document.addEventListener('DOMContentLoaded', function() {
  
    const buttons = document.querySelector('.tabs').getElementsByTagName('button');
    

    Array.from(buttons).forEach(button => {
        button.addEventListener('click', function() {

            Array.from(buttons).forEach(btn => btn.classList.remove('active'));
    
            this.classList.add('active');

            const period = this.textContent.trim().toLowerCase();
            updateChart(period);
        });
    });


    updateChart('week');
});