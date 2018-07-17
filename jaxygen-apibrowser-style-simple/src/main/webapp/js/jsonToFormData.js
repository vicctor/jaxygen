function jsonToFormData(data, parentKey, type, formData) {
    formData = formData || new FormData();
    for (let i in data) {
        let key = i;

        if (parentKey) {
            switch (type) {
                case 'o':
                    {
                        key = parentKey + '.' + i;
                        break
                    }
                case 'a':
                    {
                        key = parentKey + '[' + i + ']';
                        break
                    }
            }
        }

        if (data.hasOwnProperty(i)) {
            if (Object.prototype.toString.call(data[i]) === '[object Array]') {
                formData.append(key + "Size", data[i].length);
                jsonToFormData(data[i], key, 'a', formData);
            } else if (Object.prototype.toString.call(data[i]) === '[object Object]') {
                jsonToFormData(data[i], key, 'o', formData);
            } else if (!formData.has(i)) {
                formData.append(key + '_Value', data[i]); // TODO or set ???
            }
        }
    }
    return formData;
};

function jsonToFormString(data) {
    var obj = JSON.parse(data);
    var myFormData = jsonToFormData(obj);
    var ret = "";
    for (var pair of myFormData.entries()) {
        if (pair[0] !== "className" && pair[0] !== "methodName" && pair[0] !== "outputType" && pair[0] !== "inputType") {
            var s = "&" + pair[0] + '=' + pair[1];
            var encoded = encodeURI(s);
            console.log(encoded);
            ret += encoded;
        }
    }
    return ret;
}
