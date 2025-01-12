let countdownInterval;
let countdownTime = 60; // 60 seconds for countdown timer
let lapHistory = [];

const countdownDisplay = document.getElementById('countdown-display');
const lapList = document.getElementById('lap-list');
const startTimerButton = document.getElementById('start-timer');
const startButton = document.getElementById('start');
const pauseButton = document.getElementById('pause');
const resetButton = document.getElementById('reset');
const markButton = document.getElementById('mark');
const stopwatchDisplay = document.getElementById('display');
const addTimeButton = document.getElementById('add-time');
const subtractTimeButton = document.getElementById('subtract-time');
const resetTimerButton = document.getElementById('reset-timer');

// Countdown Timer Logic
startTimerButton.addEventListener('click', () => {
    if (countdownInterval) clearInterval(countdownInterval); // Stop any existing countdown
    countdownInterval = setInterval(() => {
        if (countdownTime > 0) {
            countdownTime--;
            const minutes = Math.floor(countdownTime / 60);
            const seconds = countdownTime % 60;
            countdownDisplay.textContent = `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
        } else {
            clearInterval(countdownInterval);
            alert('Time\'s up!');
        }
    }, 1000);
});

// Adjust Countdown Timer Time (Add/Subtract)
addTimeButton.addEventListener('click', () => {
    countdownTime += 10; // Add 10 seconds
    updateCountdownDisplay();
});

subtractTimeButton.addEventListener('click', () => {
    countdownTime -= 10; // Subtract 10 seconds (ensure no negative time)
    if (countdownTime < 0) countdownTime = 0;
    updateCountdownDisplay();
});

resetTimerButton.addEventListener('click', () => {
    countdownTime = 60; // Reset to the initial time
    updateCountdownDisplay();
});

function updateCountdownDisplay() {
    const minutes = Math.floor(countdownTime / 60);
    const seconds = countdownTime % 60;
    countdownDisplay.textContent = `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
}

// Stopwatch Logic
let stopwatchInterval;
let stopwatchTime = 0;

startButton.addEventListener('click', () => {
    stopwatchInterval = setInterval(() => {
        stopwatchTime++;
        const hours = Math.floor(stopwatchTime / 3600);
        const minutes = Math.floor((stopwatchTime % 3600) / 60);
        const seconds = stopwatchTime % 60;
        stopwatchDisplay.textContent = `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
    }, 1000);
});

pauseButton.addEventListener('click', () => {
    clearInterval(stopwatchInterval);
});

resetButton.addEventListener('click', () => {
    clearInterval(stopwatchInterval);
    stopwatchTime = 0;
    stopwatchDisplay.textContent = '00:00:00';
});

markButton.addEventListener('click', () => {
    const lapTime = stopwatchDisplay.textContent;
    lapHistory.push(lapTime);
    const lapItem = document.createElement('li');
    lapItem.textContent = lapTime;
    lapList.appendChild(lapItem);
});
