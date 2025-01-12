(() => {
    document.getElementById('export-btn').addEventListener('click', function() {
        const { jsPDF } = window.jspdf;
        const doc = new jsPDF();
        
        // Set title
        doc.setFontSize(16);
        doc.text('Dashboard Aktivitas Lari', 105, 15, { align: 'center' });
        
        // Add chart
        const canvas = document.getElementById('myChart');
        const chartImage = canvas.toDataURL('image/jpeg', 1.0);
        doc.addImage(chartImage, 'JPEG', 10, 25, 190, 100);
        
        // Add table title
        doc.setFontSize(14);
        doc.text('Data Aktivitas', 105, 135, { align: 'center' });
        
        // Table headers
        const headers = ['Tanggal', 'Judul', 'Jarak (km)', 'Durasi', 'Elevasi (m)'];
        const data = window.activitiesData.map(activity => [
            new Date(activity.createdAt).toLocaleDateString('id-ID'),
            activity.judul,
            activity.jarak.toString(),
            activity.durasi,
            activity.elevasi.toString()
        ]);
        
      
        doc.setFontSize(10);
        const cellWidth = 38;
        const cellHeight = 10;
        
     
        let currentY = 140;
        const marginTop = 20; 
        const pageHeight = doc.internal.pageSize.height;
        const marginBottom = 20;  
        
        // Bagian header
        const drawHeaders = (y) => {
            doc.setFont(undefined, 'bold');
            headers.forEach((header, i) => {
                doc.rect(10 + (i * cellWidth), y, cellWidth, cellHeight);
                doc.text(header, 10 + (i * cellWidth) + cellWidth/2, y + 6, { align: 'center' });
            });
            doc.setFont(undefined, 'normal');
            return y + cellHeight;  // Return next Y position
        };
        
        // Draw header awal
        currentY = drawHeaders(currentY);
        
        // Draw data
        data.forEach((row, rowIndex) => {
            // Check if we need a new page
            if (currentY + cellHeight > pageHeight - marginBottom) {
                doc.addPage();
                currentY = marginTop;  // Reset posisi Y untuk page baruu
                currentY = drawHeaders(currentY);  // Draw headers dan update posisi y
            }
            
            // Draw setiap row
            row.forEach((cell, columnIndex) => {
                doc.rect(10 + (columnIndex * cellWidth), currentY, cellWidth, cellHeight);
                
                // Truncate text kalau terlalu panjang
                let text = cell;
                if (doc.getStringUnitWidth(text) * 10 > cellWidth - 4) {
                    text = text.substring(0, 15) + '...';
                }
                
                doc.text(text, 10 + (columnIndex * cellWidth) + cellWidth/2, currentY + 6, { align: 'center' });
            });
            
            // Pindah ke row selanjutnya
            currentY += cellHeight;
        });
        
        // Save PDF
        doc.save('dashboard-aktivitas.pdf');
    });
})();