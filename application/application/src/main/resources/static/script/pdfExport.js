(()=>{
    // Fungsi untuk mengekspor dashboard ke PDF
    document.getElementById('export-btn').addEventListener('click', function() {
        const { jsPDF } = window.jspdf;
        const doc = new jsPDF();
    
        // Menambahkan teks ke PDF
        doc.text('Dashboard Aktivitas Lari', 10, 10);
    
        // Menambahkan grafik ke PDF (ambil canvas dari Chart.js)
        const canvas = document.getElementById('myChart');
        doc.addImage(canvas, 'PNG', 10, 20, 180, 90);  // Menambahkan gambar grafik
    
        // Menambahkan statistik
        doc.text('Total Jarak: 150 km', 10, 120);
        doc.text('Kecepatan Rata-rata: 12 km/jam', 10, 130);
        doc.text('Total Waktu: 12 jam', 10, 140);
    
        // Menyimpan PDF
        doc.save('dashboard-lari.pdf');
    });

})();