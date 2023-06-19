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
    console.log("Disconnected");
}

function sendNotification(message) {
    console.log("message : " + "알림전송 메세지")
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