var stompClient = null;

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}

function sendNotification(message) {
    stompClient.send("/send-notification", {}, message);
}

document.getElementById("notification-form").addEventListener("submit", function(event) {
    event.preventDefault();
    var messageInput = document.getElementById("notification-input");
    var message = messageInput.value;
    sendNotification(message);
    messageInput.value = "";
});

connect();