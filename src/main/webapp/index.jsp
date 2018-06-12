<html>
<head>
    <script type="text/javascript" src="jquery-3.2.1.min.js"></script>
</head>
<body>
<h2>Hello World!</h2>
<div>
    <input id="url_for_send" type="text">
    <input id="message_for_send" type="text">
    <button onclick="send()">send</button>
</div>

<script type="text/javascript">
    /*
    var replyCallbacks = {};
    function addReplyCallback(url, sequence, callback) {
        var callbacks = replyCallbacks[url];
        if (callbacks) {
            callbacks[sequence] = callback;
        } else {
            replyCallbacks[url] = {};
            replyCallbacks[url][sequence] = callback;
        }
    }
    function removeReplyCallback(url, sequence) {
        var callback = replyCallbacks[url][sequence];
        replyCallbacks[url][sequence] = undefined;
        return callback;
    }

    var ws = new WebSocket('ws://localhost:8080/app/mobile?nuid=nuid:gggg&ndid=ndid:ggg&access-token=xxx&secret-key=gggg');
    ws.onopen = function open(event) {
        console.log('websocket connected.');
        var request = {
            url: '/test',
            sequence: 1,
            parameter: '{}'
        };
        ws.send(JSON.stringify(request));

    };
    ws.onmessage = function reason(event) {
        console.log('websocket recv reason ' + event.data);
        var reply = JSON.parse(event.data);
        console.log(reply);

        switch (reply.type) {
        case 'Request':

            break;
        case 'Reply':
            var callback = removeReplyCallback(reply.url, reply.sequence);
            if (callback) {
                callback(reply.data);
            }
            break;
        default:
            break;
        }
    };
    ws.onerror = function error(event) {
        ws.close();
    };
    ws.onclose = function close(event) {
        console.log('websocket disconnected.');
        ws = null;
    };
    var wsSend = (function() {
        var _sequence = 1;
        return function (url, parameter, callback) {
            var sequence = _sequence++;
            addReplyCallback(url, sequence, callback);
            ws.send(JSON.stringify({ url: url, sequence: sequence, type: 'Request', command: 'Simple', parameter: JSON.stringify(parameter) }));
        };
    })();
    function send() {
        wsSend($('#url_for_send').val(), { text: $('#message_for_send').val() }, function (data) {
            console.log('hhhhhhhhhhhh', data);
        });
    }*/
</script>
</body>
</html>
