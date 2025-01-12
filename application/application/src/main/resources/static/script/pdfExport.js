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
        
        // Table styling
        doc.setFontSize(10);
        const cellWidth = 38;
        const cellHeight = 10;
        
        // First page starts after the chart
        let currentY = 140;
        const marginTop = 20;  // Margin from top of page for subsequent pages
        const pageHeight = doc.internal.pageSize.height;
        const marginBottom = 20;  // Margin from bottom of page
        
        // Draw headers function
        const drawHeaders = (y) => {
            doc.setFont(undefined, 'bold');
            headers.forEach((header, i) => {
                doc.rect(10 + (i * cellWidth), y, cellWidth, cellHeight);
                doc.text(header, 10 + (i * cellWidth) + cellWidth/2, y + 6, { align: 'center' });
            });
            doc.setFont(undefined, 'normal');
            return y + cellHeight;  // Return next Y position
        };
        
        // Draw initial headers
        currentY = drawHeaders(currentY);
        
        // Draw data
        data.forEach((row, rowIndex) => {
            // Check if we need a new page
            if (currentY + cellHeight > pageHeight - marginBottom) {
                doc.addPage();
                currentY = marginTop;  // Reset Y position for new page
                currentY = drawHeaders(currentY);  // Draw headers and update Y position
            }
            
            // Draw row
            row.forEach((cell, columnIndex) => {
                doc.rect(10 + (columnIndex * cellWidth), currentY, cellWidth, cellHeight);
                
                // Truncate text if too long
                let text = cell;
                if (doc.getStringUnitWidth(text) * 10 > cellWidth - 4) {
                    text = text.substring(0, 15) + '...';
                }
                
                doc.text(text, 10 + (columnIndex * cellWidth) + cellWidth/2, currentY + 6, { align: 'center' });
            });
            
            // Move to next row
            currentY += cellHeight;
        });
        
        // Save the PDF
        doc.save('dashboard-aktivitas.pdf');
    });
})();