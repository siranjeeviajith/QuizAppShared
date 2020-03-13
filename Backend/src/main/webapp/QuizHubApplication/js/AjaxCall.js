function makeAjaxCall(url, payload) {
    if (!url)
        throw new Error("Invalid url to make ajax request");
    if (!payload)
        throw new Error("Invalid Payload");
    if (!payload.method)
        throw new Error("Invalid Method");
    if ((payload.method == "POST") && (!payload.request))
        throw new Error("POST method requires input from user");
    return new Promise((resolve, reject) => {
        method = payload.method;
        if (typeof(payload.request) == "string") {
            url += payload.request;
        }

        var http = new XMLHttpRequest();
        http.open(method, url, payload.async);
        http.setRequestHeader("API-KEY", "QUIZ_APP_KEY_78194260");
        if (method == "GET") {
            console.log("inside get");
            http.send();
        } else if (method == "POST") {
            console.log("inside post");
            http.setRequestHeader("Content-Type", "application/json");
            http.send(JSON.stringify(payload.request));
        } else if (method == "HEAD") {

            console.log("inside head");
            http.send();
        }
        http.onreadystatechange = function() {
            if (http.readyState === 4) {
                if (http.status === 200) {
                    console.log("XHR call done successfully");
                    var resp = http.responseText;
                    var respJson = JSON.parse(resp);
                    resolve(respJson);
                } else {
                    reject(http.status);
                    console.log("XHR failed");
                }
            } else {
                console.log("XHR processing going on");
            }
        }
    });

}