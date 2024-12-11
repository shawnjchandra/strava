document.getElementById('activityForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const date = document.getElementById('date').value;
    const title = document.getElementById('title').value;
    const description = document.getElementById('description').value;
    const duration = document.getElementById('duration').value;
    const distance = document.getElementById('distance').value;
    const photo = document.getElementById('photo').files[0];

    const reader = new FileReader();
    reader.onloadend = function () {
        const activity = {
            date: date,
            title: title,
            description: description,
            duration: duration,
            distance: distance,
            photo: reader.result
        };

        addActivityToList(activity);
        document.getElementById('activityForm').reset();
    };
    
    if (photo) {
        reader.readAsDataURL(photo);
    }
});

function addActivityToList(activity) {
    const activitiesList = document.getElementById('activities');
    const listItem = document.createElement('li');
    listItem.innerHTML = `
        <strong>${activity.title}</strong><br>
        Date: ${activity.date}<br>
        Description: ${activity.description}<br>
        Duration: ${activity.duration} minutes<br>
        Distance: ${activity.distance} km<br>
        <img src="${activity.photo}" alt="${activity.title}" style="width:100px; height:auto;"/><br>
    `;
    activitiesList.appendChild(listItem);
}