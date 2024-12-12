(()=>{
    // Data untuk grafik
    var ctx = document.getElementById('myChart').getContext('2d');
    
    var myChart = new Chart(ctx, {
      type: 'line',  // Tipe grafik: line chart
      data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'Mei'],  // Bulan
        datasets: [{
          label: 'Jarak (km)',
          data: [10, 20, 30, 40, 50],  // Data jarak bulanan
          borderColor: 'rgba(75, 192, 192, 1)',
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
          borderWidth: 2
        }]
      },
      options: {
        responsive: true,
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });

    myChart.update()
})();

  