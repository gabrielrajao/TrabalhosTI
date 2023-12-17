//Header Authorizantion com o token do usuario (Forma recomendada de enviar tokens de autenticacao)
const headerslist = {
  "Authorization" : 'Bearer ' + sessionStorage.getItem("UserLogado")
}

//Remove UserLogado do SessionStorage e atualiza a pagina
function deluserlogado() {
  sessionStorage.removeItem("UserLogado")
  location.reload()
}


//Funcao testa validade do token pelo backend
async function enviaToken() {
  let result = await fetch("http://localhost:6789/usuario/token", {
    //header com o token
    headers: headerslist,
    //metodo do fetch
    method: "GET"
  })
  //promise
    .then(response => {
      //response.ok pega a range de codigos de status http que sinalizam sucesso (entre 200 e 299) 
      if (response.ok) {
        return true;
      }
      //caso o codigo de status nao tenha sido de sucesso, avisar o usuario, remover o token do sessionstorage e retornar false 
      else {
        alert("Token inválido/expirado, faça login novamente");
        deluserlogado();
        return false;
      }
    })
    return result;
    
    
}

//ativa a funcao enviatoken de forma mais eficiente
async function isUserLogado() {

  //pega as informações de user logado
  var Userlogado = sessionStorage.getItem("UserLogado");
  //checa se tais informações realmente estão no sessionstorage
  if (Userlogado == null) {
    //caso nao, retorna false
    return false;
  }
  //caso estejam, ativa a função enviatoken para testar validade de tais informações 
  else {
    return enviaToken();
  }
}


function paraHora( str ){
 let array = str.split(" - ");
 let tmpmenor = array[0] + ":00";
 let tmpmaior = array[1] + ":00";
 return tmpmenor + " - " + tmpmaior;
}

function JSONGenerator(){

  let mestmp = mes + 1;
  let anotmp = ano;
  let resultado = "[";
  let maxmes = 0;
  if(mestmp -1 == 1){
    if(anotmp % 4 == 0){
      maxmes =29;
    } else{
      maxmes = 28;
    }
  } else if (mestmp -1 % 2 == 0){
    maxmes = 30;
  } else{
    maxmes = 31;
  }

  for(var contagemmes = 0; contagemmes < 5; contagemmes++){
    for(var contagemdias = 1; contagemdias <= maxmes; contagemdias++ ){
        if(contagemmes == 0 && contagemdias == 1){
          contagemdias = dia;
        } else{
          resultado += ",";
        }
        resultado += '{'+
        '"day": '+ contagemdias +','+
        '"month":' + mestmp + ','+
        '"year":' + anotmp + ','+
        '"events":[]'+
        '}'
    }
    mestmp++;
    if(mestmp > 11){
      mestmp = 0;
      anotmp++;
    }
    if(mestmp == 1){
      if(anotmp % 4 == 0){
        maxmes =29;
      } else{
        maxmes = 28;
      }
    } else if (mestmp % 2 == 0){
      maxmes = 31;
    } else{
      maxmes = 30;
    }
  }
  resultado += "]";
  return JSON.parse(resultado);
}

function atualizaJSON(j, d){


  for(let ha = 0; ha < j.length; ha++){
    let jsonindex = j[ha];
    for(let i = 0; i < d.length; i++){
      let index = d[i];
      let horas = paraHora(index.expo);
      let planta = index.nomeplanta;
      let finalizando = '{ "title": "'+ planta + ' - Colocar no sol", "time": "Horário: '+horas+'"  }';
      let JSONparaPush = (JSON.parse(finalizando));
      //rega
      let arrayrega = index.rega.split(",");
      let horarega = arrayrega[0] ;
      let frequenciarega = arrayrega[1];
      let iniciorega = index.datainicio % frequenciarega;
      if( ha == iniciorega ||((ha + iniciorega)  % frequenciarega == 0 && (ha >= iniciorega))){
        let stringrega = '{ "title": "'+ planta + ' - Regar", "time": "Horário: '+horarega+':00"  }';
        jsonindex.events.push(JSON.parse(stringrega));
      }
      
      //poda
      let arraypoda = index.poda.split(",");
      let horapoda = arraypoda[0];
      let frequenciapoda = arraypoda[1];
      let iniciopoda = index.datainicio % frequenciapoda;
      console.log(iniciopoda);
      if( ha == iniciopoda  ||(frequenciapoda > 0 && (ha + iniciopoda) % frequenciapoda == 0 && ha >= iniciopoda)){
        console.log(ha);
        let stringpoda = '{ "title": "'+ planta + ' - Podar", "time": "Horário: '+horapoda+':00"  }';
        jsonindex.events.push(JSON.parse(stringpoda)); 
      }



      jsonindex.events.push(JSONparaPush);
    }
  }
  return j;
}

var eventsArr;

async function pegaCalendarios(){
  let validation = await isUserLogado();
  if(validation == false){
    alert("Não foi possivel validar o usuário, favor logar novamente");
    window.location.href="./Login.html"
  }
  return fetch("http://localhost:6789/agenda/calendario", {
    headers: headerslist,
    method: "GET",
    mode: "cors"
  }).then(response=>{
    if(response.ok){
      return response.json()
    }
    else{
      throw(response.status);
    }
  }).then(data =>{
    let JSON = JSONGenerator();
    console.log(data);
    return JSON = atualizaJSON(JSON, data)
  }).catch(error =>{
    return  JSONGenerator();
  })
}



async function inicializar(){
  eventsArr = await pegaCalendarios();
  
  initCalendar();

  
}

document.onload = inicializar();




var hoje = new Date();
var dia = hoje.getDate();
var mes = hoje.getMonth();
var ano = hoje.getFullYear();

const calendar = document.querySelector(".calendar"),
  date = document.querySelector(".date"),
  daysContainer = document.querySelector(".days"),
  prev = document.querySelector(".prev"),
  next = document.querySelector(".next"),
  todayBtn = document.querySelector(".today-btn"),
  //gotoBtn = document.querySelector(".goto-btn"),
  dateInput = document.querySelector(".date-input"),
  eventDay = document.querySelector(".event-day"),
  eventDate = document.querySelector(".event-date"),
  eventsContainer = document.querySelector(".events"),
  //addEventBtn = document.querySelector(".add-event"),
  addEventWrapper = document.querySelector(".add-event-wrapper "),
  addEventCloseBtn = document.querySelector(".close "),
  addEventTitle = document.querySelector(".event-name "),
  addEventFrom = document.querySelector(".event-time-from "),
  addEventTo = document.querySelector(".event-time-to "),
  addEventSubmit = document.querySelector(".add-event-btn ");

let today = new Date();
let activeDay;
let month = today.getMonth();
let year = today.getFullYear();

const months = [
  "Janeiro",
  "Fevereiro",
  "Março",
  "Abril",
  "Maio",
  "Junho",
  "Julho",
  "Agosto",
  "Setembro",
  "Outubro",
  "Novembro",
  "Dezembro",
];

// const eventsArr = [
//   {
//     day: 13,
//     month: 11,
//     year: 2022,
//     events: [
//       {
//         title: "Event 1 lorem ipsun dolar sit genfa tersd dsad ",
//         time: "10:00 AM",
//       },
//       {
//         title: "Event 2",
//         time: "11:00 AM",
//       },
//     ],
//   },
// ];


/*

const eventsArr = [];
getEvents();
 */

//function to add days in days with class day and prev-date next-date on previous month and next month days and active on today
function initCalendar() {
  const firstDay = new Date(year, month, 1);
  const lastDay = new Date(year, month + 1, 0);
  const prevLastDay = new Date(year, month, 0);
  const prevDays = prevLastDay.getDate();
  const lastDate = lastDay.getDate();
  const day = firstDay.getDay();
  const nextDays = 7 - lastDay.getDay() - 1;

  date.innerHTML = months[month] + " " + year;

  let days = "";

  for (let x = day; x > 0; x--) {
    days += `<div class="prev-date">${prevDays - x + 1}</div>`;
  }

  for (let i = 1; i <= lastDate; i++) {
    //check if event is present on that day
    let event = false;
    eventsArr.forEach((eventObj) => {
      if (
        eventObj.day === i &&
        eventObj.month === month + 1 &&
        eventObj.year === year
      ) {
        event = true;
      }
    });
    if (
      i === new Date().getDate() &&
      year === new Date().getFullYear() &&
      month === new Date().getMonth()
    ) {
      activeDay = i;
      getActiveDay(i);
      updateEvents(i);
      if (event) {
        days += `<div class="day today active event">${i}</div>`;
      } else {
        days += `<div class="day today active">${i}</div>`;
      }
    } else {
      if (event) {
        days += `<div class="day event">${i}</div>`;
      } else {
        days += `<div class="day ">${i}</div>`;
      }
    }
  }

  for (let j = 1; j <= nextDays; j++) {
    days += `<div class="next-date">${j}</div>`;
  }
  daysContainer.innerHTML = days;
  addListner();
}

//function to add month and year on prev and next button
var monthlimiter = 0;
/*function prevMonth() {
  
  if (month != mes) {
    month--;
    monthlimiter--;
    if(month < 0){
      month = 11;
      year--;
    }
  } else{
    alert("Não é possivel voltar mais")
  }
  initCalendar();
}



function nextMonth() {
  if (monthlimiter < 5) {
    monthlimiter++;
    month++;
    if(month > 11){
      month = 0;
      year++;
    }

  } else{
    alert("Não é possivel avançar mais");
  }

  initCalendar();
}

prev.addEventListener("click", prevMonth);
next.addEventListener("click", nextMonth);
*/

//function to add active on day
function addListner() {
  const days = document.querySelectorAll(".day");
  days.forEach((day) => {
    day.addEventListener("click", (e) => {
      getActiveDay(e.target.innerHTML);
      updateEvents(Number(e.target.innerHTML));
      activeDay = Number(e.target.innerHTML);
      //remove active
      days.forEach((day) => {
        day.classList.remove("active");
      });

    });
  });
}

function irParaHoje(){
  today = new Date();
  month = today.getMonth();
  year = today.getFullYear();
  initCalendar();
}

/*dateInput.addEventListener("input", (e) => {
  dateInput.value = dateInput.value.replace(/[^0-9/]/g, "");
  if (dateInput.value.length === 2) {
    dateInput.value += "/";
  }
  if (dateInput.value.length > 7) {
    dateInput.value = dateInput.value.slice(0, 7);
  }
  if (e.inputType === "deleteContentBackward") {
    if (dateInput.value.length === 3) {
      dateInput.value = dateInput.value.slice(0, 2);
    }
  }
}); */

//gotoBtn.addEventListener("click", gotoDate);

function gotoDate() {

  const dateArr = dateInput.value.split("/");
  if (dateArr.length === 2) {
    if (dateArr[0] > 0 && dateArr[0] < 13 && dateArr[1].length === 4) {
      month = dateArr[0] - 1;
      year = dateArr[1];
      initCalendar();
      return;
    }
  }
  alert("Invalid Date");
}

//function get active day day name and date and update eventday eventdate
function getActiveDay(date) {
  const day = new Date(year, month, date);
  const dayName = day.toString().split(" ")[0];
  eventDay.innerHTML = dayName;
  eventDate.innerHTML = date + " " + months[month] + " " + year;
}

//function update events when a day is active
function updateEvents(date) {
  let events = "";
  eventsArr.forEach((event) => {

    if (
      date == event.day &&
      month + 1 == event.month &&
      year == event.year
    ) {
      event.events.forEach((event) => {
      
        events += `<div class="event">
            <div class="title">
              <i class="fas fa-circle"></i>
              <h3 class="event-title">${event.title}</h3>
            </div>
            <div class="event-time">
              <span class="event-time">${event.time}</span>
            </div>
        </div>`;
      });
      
    }
  });
  if (events === "") {
    events = `<div class="no-event">
            <h3>No Events</h3>
        </div>`;
  }
  eventsContainer.innerHTML = events;

}

//function to add event
/*addEventBtn.addEventListener("click", () => {
  addEventWrapper.classList.toggle("active");
}); */

addEventCloseBtn.addEventListener("click", () => {
  addEventWrapper.classList.remove("active");
});

/* document.addEventListener("click", (e) => {
  if (e.target !== addEventBtn && !addEventWrapper.contains(e.target)) {
    addEventWrapper.classList.remove("active");
  }
}); */

//allow 50 chars in eventtitle
addEventTitle.addEventListener("input", (e) => {
  addEventTitle.value = addEventTitle.value.slice(0, 60);
});

//allow only time in eventtime from and to
addEventFrom.addEventListener("input", (e) => {
  addEventFrom.value = addEventFrom.value.replace(/[^0-9:]/g, "");
  if (addEventFrom.value.length === 2) {
    addEventFrom.value += ":";
  }
  if (addEventFrom.value.length > 5) {
    addEventFrom.value = addEventFrom.value.slice(0, 5);
  }
});

addEventTo.addEventListener("input", (e) => {
  addEventTo.value = addEventTo.value.replace(/[^0-9:]/g, "");
  if (addEventTo.value.length === 2) {
    addEventTo.value += ":";
  }
  if (addEventTo.value.length > 5) {
    addEventTo.value = addEventTo.value.slice(0, 5);
  }
});

//function to add event to eventsArr
addEventSubmit.addEventListener("click", () => {
  const eventTitle = addEventTitle.value;
  const eventTimeFrom = addEventFrom.value;
  const eventTimeTo = addEventTo.value;
  if (eventTitle === "" || eventTimeFrom === "" || eventTimeTo === "") {
    alert("Please fill all the fields");
    return;
  }

  //check correct time format 24 hour
  const timeFromArr = eventTimeFrom.split(":");
  const timeToArr = eventTimeTo.split(":");
  if (
    timeFromArr.length !== 2 ||
    timeToArr.length !== 2 ||
    timeFromArr[0] > 23 ||
    timeFromArr[1] > 59 ||
    timeToArr[0] > 23 ||
    timeToArr[1] > 59
  ) {
    alert("Invalid Time Format");
    return;
  }

  const timeFrom = convertTime(eventTimeFrom);
  const timeTo = convertTime(eventTimeTo);

  //check if event is already added
  
  let eventExist = false;
  eventsArr.forEach((event) => {
    if (
      event.day === activeDay &&
      event.month === month + 1 &&
      event.year === year
    ) {
      event.events.forEach((event) => {
        if (event.title === eventTitle) {
          eventExist = true;
        }
      });
    }
  });
  if (eventExist) {
    alert("Event already added");
    return;
  }
  const newEvent = {
    title: eventTitle,
    time: timeFrom + " - " + timeTo,
  };

  let eventAdded = false;
  if (eventsArr.length > 0) {
    eventsArr.forEach((item) => {
      if (
        item.day === activeDay &&
        item.month === month + 1 &&
        item.year === year
      ) {
        item.events.push(newEvent);
        eventAdded = true;
      }
    });
  }

  if (!eventAdded) {
    eventsArr.push({
      day: activeDay,
      month: month + 1,
      year: year,
      events: [newEvent],
    });
  }


  addEventWrapper.classList.remove("active");
  addEventTitle.value = "";
  addEventFrom.value = "";
  addEventTo.value = "";
  updateEvents(activeDay);
  //select active day and add event class if not added
  const activeDayEl = document.querySelector(".day.active");
  if (!activeDayEl.classList.contains("event")) {
    activeDayEl.classList.add("event");
  }
});

//function to delete event when clicked on event
eventsContainer.addEventListener("click", (e) => {
  if (e.target.classList.contains("event")) {
    if (confirm("Are you sure you want to delete this event?")) {
      const eventTitle = e.target.children[0].children[1].innerHTML;
      eventsArr.forEach((event) => {
        if (
          event.day === activeDay &&
          event.month === month + 1 &&
          event.year === year
        ) {
          event.events.forEach((item, index) => {
            if (item.title === eventTitle) {
              event.events.splice(index, 1);
            }
          });
          //if no events left in a day then remove that day from eventsArr
          if (event.events.length === 0) {
            eventsArr.splice(eventsArr.indexOf(event), 1);
            //remove event class from day
            const activeDayEl = document.querySelector(".day.active");
            if (activeDayEl.classList.contains("event")) {
              activeDayEl.classList.remove("event");
            }
          }
        }
      });
      updateEvents(activeDay);
    }
  }
});

/*
//function to save events in local storage
function saveEvents() {
  localStorage.setItem("events", JSON.stringify(eventsArr));
}


//function to get events from local storage
function getEvents() {
  //check if events are already saved in local storage then return event else nothing
  if (localStorage.getItem("events") === null) {
    return;
  }
  eventsArr.push(...JSON.parse(localStorage.getItem("events")));
}
*/

function convertTime(time) {
  //convert time to 24 hour format
  let timeArr = time.split(":");
  let timeHour = timeArr[0];
  let timeMin = timeArr[1];
  let timeFormat = timeHour >= 12 ? "PM" : "AM";
  timeHour = timeHour % 12 || 12;
  time = timeHour + ":" + timeMin + " " + timeFormat;
  return time;
}