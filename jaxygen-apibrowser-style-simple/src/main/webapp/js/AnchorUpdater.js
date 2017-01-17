function registerUpdateShareLink(form) {
    form.addEventListener("focusout", updateShareLink);
}

function updateShareLink(event) {
        event.preventDefault();
        console.log(event.target.name + ' : ' + event.target.value);
        var inputValue = event.target.value;
        var inputId = event.target.id;
        var url = window.location.href;
        var updatedUrl = updateUrl(url, inputId, inputValue);
        document.getElementById("share_it").value = updatedUrl;
    }
function handleAnchors(form) {
    form.addEventListener("focusout", function (event) {
        event.preventDefault();
        console.log(event.target.name + ' : ' + event.target.value);
        var anchors = document.getElementsByClassName('anchor');
        var anchors = [].slice.call(anchors);
        var inputValue = event.target.value;
        var inputId = event.target.id;

        anchors.forEach(function (anchor) {
            var a = document.getElementById(anchor.id);
            var url = a.href;
            var updatedUrl = updateUrl(url, inputId, inputValue);
            document.getElementById(anchor.id).href = updatedUrl;
        });        
    });    
}
function updateUrl(url, inputId, inputValue) {

    var encodedValue = encodeURI(inputValue);
    var urlSplited = url.split('?');
    params = urlSplited[1].split('&');

    var elem = params.find(findParams);
    if (elem == null) {
        elem = inputId + '_Value=';
        elem = encodeURI(elem);
        params.push(elem);
    }
    var elemArr = elem.split('=');
    var elemKey = elemArr[0];

    var indexOfElem = params.indexOf(elem);
    params[indexOfElem] = elemKey + '=' + encodedValue;

    var resultUrl = urlSplited[0] + '?' + params.join('&');
    return resultUrl;


    function findParams(str) {
        var idd = inputId;
        idd = idd.replace(/\[/g, '\\[');
        idd = idd.replace(/\]/g, '\\]');
        idd = idd.replace(/\./g, '\\.');
        var reg = new RegExp(idd + '_Value=')
        var decodedStr = decodeURI(str);
        return decodedStr.match(reg);
    }
}
;