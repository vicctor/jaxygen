function handleAnchors(form) {
  form.addEventListener("focusout", function (event) {
    event.preventDefault();
    console.log(event.target.name + ' : ' + event.target.value);
    var anchors = document.getElementsByClassName('anchor');
    var anchors = [].slice.call(anchors);
    var inputValue = event.target.value;
    var inputId = event.target.id;

    anchors.forEach(function (anchor) {
      updateAnchor(anchor.id, inputId, inputValue);
    });
  });
}
function updateAnchor(anchorId, inputId, inputValue) {

  var encodedValue = encodeURI(inputValue);

  var a = document.getElementById(anchorId);
  var anchorSplited = a.href.split('?');
  params = anchorSplited[1].split('&');

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

  var resultAnchor = anchorSplited[0] + '?' + params.join('&');
  document.getElementById(anchorId).href = resultAnchor;


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