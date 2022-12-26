alert('hello');

let hertz = 220;
let waveform = 'sine';
let sustain = 'OFF';

const toggle = document.querySelector('.toggle input');

toggle.addEventListener('click', () => {
  const onOff = toggle.parentNode.querySelector('.onOff');
  sustain = toggle.checked ? 'ON' : 'OFF';
  console.log(sustain);
});

//add the catch and specifics for the drop down.
function getHertz() {
  hertz = document.getElementById('getHertz').value;
  hertz = hertz / 2;
  drawGrid(hertz);
}

//add a catch for stopping the oscillator when change happens during a sustained note
//would this be a feature?
function getWaveform() {
  waveform = document.getElementById('waveform').value;
  drawGrid(hertz);
}

function makeString(f) {
  return [f, f * 2, f * 3, f * 4, f * 5, f * 6, f * 7, f * 8];
}

//perhaps populate an array of string values from the database
function makeGrid(f) {
  let string1 = f;
  let string2 = (string1 * (4 / 3)) / 2;
  let string3 = (string2 * (4 / 3)) / 2;
  let string4 = (string3 * (4 / 3)) / 2;

  return [makeString(string1), makeString(string2), makeString(string3), makeString(string4)];
}

function drawGrid(f) {
  let notes = makeGrid(f);

  for (let i = 0; i < notes.length; i++) {
    var element = document.getElementById(i);
    var children = element.children;
    for (let j = 0; j < children.length; j++) {
      var child = children[j];
      child.setAttribute('data-note', notes[i][j]);
      child.setAttribute('state', 'off');
      child.addEventListener('click', toggleSynth);
    }
  }
}

const audioCtx = new AudioContext();
audioCtx.suspend();

let synths = {}; //holding values

function toggleSynth(event) {
  let button = event.target;
  let frequency = button.getAttribute('data-note');
  if (button.getAttribute('state') == 'off' && sustain == 'OFF') {
    console.log('testing no sustain');
    button.setAttribute('state', 'on');
    button.style.background = '#00FF00';
    //have to create a new audio context for each note that is not sustained
    const audioCt = new AudioContext();
    let oscillatorNode = audioCt.createOscillator();
    let gainNode = audioCt.createGain();
    let output = audioCt.destination;
    oscillatorNode.connect(gainNode);
    gainNode.gain.setValueAtTime(0.1, audioCt.currentTime);
    gainNode.connect(output);
    oscillatorNode.frequency.value = frequency;
    oscillatorNode.type = waveform;
    oscillatorNode.start(0);
    oscillatorNode.stop(0.8);

    button.setAttribute('state', 'off');
    button.style.background = button.getAttribute('background-color');
  } else if (button.getAttribute('state') == 'off' && sustain == 'ON') {
    //AND SUSTAIN TOGGLE
    button.setAttribute('state', 'on');
    button.style.background = '#00FF00';
    let oscillatorNode = makeOscillator();
    oscillatorNode.start();
    oscillatorNode.type = waveform;
    oscillatorNode.frequency.setValueAtTime(frequency, audioCtx.currentTime);
    synths[frequency] = oscillatorNode;
  } else {
    button.setAttribute('state', 'off');
    button.style.background = button.getAttribute('background-color');
    synths[frequency].stop();
  }
  console.log(synths);

  audioCtx.resume();
}

function makeOscillator() {
  let oscillatorNode = audioCtx.createOscillator();
  let gainNode = audioCtx.createGain();
  let output = audioCtx.destination;

  oscillatorNode.connect(gainNode);
  gainNode.gain.setValueAtTime(0.1, audioCtx.currentTime);
  gainNode.connect(output);
  // oscillatorNode.start()

  return oscillatorNode;
}

drawGrid(hertz);
