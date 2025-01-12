    // Clock 
    function updateClock() {
        const clockElement = document.getElementById('clock');
        const currentTime = new Date();
        const hours = String(currentTime.getHours()).padStart(2, '0');
        const minutes = String(currentTime.getMinutes()).padStart(2, '0');
        const seconds = String(currentTime.getSeconds()).padStart(2, '0');
        clockElement.textContent = `${hours}:${minutes}:${seconds}`;
    }
    setInterval(updateClock, 1000);
    updateClock();

    // Stopwatch 
    let timer = null;
    let elapsedTime = 0;
    let isRunning = false;
    

    const display = document.getElementById('display');
    const marksList = document.getElementById('marksList');

    function formatTime(ms) {
        const totalSeconds = Math.floor(ms / 1000);
        const hours = String(Math.floor(totalSeconds / 3600)).padStart(2, '0');
        const minutes = String(Math.floor((totalSeconds % 3600) / 60)).padStart(2, '0');
        const seconds = String(totalSeconds % 60).padStart(2, '0');
        return `${hours}:${minutes}:${seconds}`;
    }

    function updateDisplay() {
        display.textContent = formatTime(elapsedTime);
    }

    function startTimer() {
        if (!isRunning) {
            isRunning = true;
            const startTime = Date.now() - elapsedTime;
            timer = setInterval(() => {
                elapsedTime = Date.now() - startTime;
                updateDisplay();
            }, 100);
        }
    }

    function pauseTimer() {
        if (isRunning) {
            isRunning = false;
            clearInterval(timer);
        }
    }

    function resetTimer() {
        pauseTimer();
        elapsedTime = 0;
        updateDisplay();
        marksList.innerHTML = '';
    }

    function markTime() {
        const children = marksList.children.length+1; 
        const mark = formatTime(elapsedTime);
        const li = document.createElement('li');
        
        li.textContent = children+" : " + mark;
        marksList.appendChild(li);
    }

    document.getElementById('start').addEventListener('click', startTimer);
    document.getElementById('pause').addEventListener('click', pauseTimer);
    document.getElementById('reset').addEventListener('click', resetTimer);
    document.getElementById('mark').addEventListener('click', markTime);

    updateDisplay();