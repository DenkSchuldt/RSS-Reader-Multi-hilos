var http = require('http');
const PORT = 3000;

function handleRequest(request, response){
  dispatch(request.url);
}

var server = http.createServer(handleRequest);

server.listen(PORT, function(){
  console.log("Server listening on: http://localhost:%s", PORT);
});

function dispatch(url){
  var options = { };
  switch(url) {
    case "/eluniverso":
      options = {
        host : 'www.eluniverso.com',
        path : '/rss/all.xml',
        port : 80,
        method : 'GET'
      }
    break;
    case "/bbc":
      options = {
        host : 'www.bbc.com',
        path : '/mundo/index.xml',
        port : 80,
        method : 'GET'
      }
    break;
    case "/cnn":
      options = {
        host : 'rss.cnn.com',
        path : '/rss/edition.rss',
        port : 80,
        method : 'GET'
      }
    break;
    case "/telegraph":
      options = {
        host : 'www.telegraph.co.uk',
        path : '/news/worldnews/rss',
        port : 80,
        method : 'GET'
      }
    break;
  }
  var request = http.request(options, function(response){
    var body = ""
    response.on('data', function(data) {
      body += data;
    });
    response.on('end', function() {
      console.log(body);
      response.send(body);
    });
  });
  request.on('error', function(e) {
    console.log('Problem with request: ' + e.message);
  });
  request.end();
}
