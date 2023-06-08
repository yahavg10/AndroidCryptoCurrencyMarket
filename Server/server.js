const express = require("express");
const app = express();
const socket = require('socket.io'); //requires socket.io module
const fs = require('fs');
const bodyParser = require("body-parser");
const cors = require("cors");
const axios = require("axios");
var PORT = process.env.PORT || 4224;

const ApiKey = "7C7BFF23-13E4-4BB8-A02F-C44BF89191D9";
const headers = {"X-CoinAPI-Key": ApiKey,};

const server = app.listen(PORT); //tells to host server on localhost:3000
app.use(express.static('public')); //show static files in 'public' directory
console.log('Server is running');
const io = socket(server);


//Socket.io Connection------------------
io.on('connection', (socket) => {

    console.log("New socket connection: " + socket.id)

    socket.on('getList', () => {
      getCoinsList().then((data) => {
        console.log(JSON.stringify(data, null, 2));
        io.emit('getList', JSON.stringify(data));
      }).catch((error) => {
        console.error(error);
      });
    })

    socket.on('getCoin', (namec, d) => {
      console.log(namec, d);
      getCoinData(namec, d).then((data) => {
        console.log(JSON.stringify(data, null, 2));
        io.emit('getCoin', JSON.stringify(data));
      }).catch((error) => {
        console.error(error);
      });
    })


})




async function getCoinData(name,d)
{  
  if(d == "Day")
  {
    var timeStart = new Date();
    timeStart.setHours(0);
    timeStart = timeStart.toISOString();
    timeEnd = new Date().toISOString();
    periodId = "30MIN";
  }
  if(d == "Month")
  {
    timeStart = new Date();
    var year = timeStart.getFullYear();
    var month = timeStart.getMonth();
    timeStart = new Date(year, month, 0).toISOString();
    timeEnd = new Date(year, month, 30).toISOString();
    periodId = "1DAY";
  }
  if(d == "Year")
  {
    timeStart = new Date();
    var year = timeStart.getFullYear();
    var month = timeStart.getMonth();

    timeStart = new Date(year-1, month, timeStart.getDate()).toISOString();
    timeEnd = new Date().toISOString();
    periodId = "10DAY";
  }

  const url = `https://rest.coinapi.io/v1/exchangerate/${name}/USD/history?period_id=${periodId}&time_start=${timeStart}&time_end=${timeEnd}&limit=100`;
  try {
    const response = await axios.get(url, { headers });
    const data = response.data.map(({ time_period_start, rate_open }) => { return {date: time_period_start,price: rate_open,};});
    return data;
  } catch (err) {
  console.log("Found an error while communicating with CoinApi.io");
  throw err; // Propagate the error further if needed
}
}



async function getCoinsList() {
  const url = "https://rest.coinapi.io/v1/assets";

  try {
    const response = await axios.get(url, { headers });
    const dataJ = response.data.map(({ asset_id }) => asset_id);
    return dataJ;
  } catch (err) {
    console.log("Found an error while communicating with CoinApi.io");
    throw err; // Propagate the error further if needed
  }
}
//https://fixer.io/documentation
