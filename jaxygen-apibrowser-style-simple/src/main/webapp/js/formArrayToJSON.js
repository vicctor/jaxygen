// ----------------BUG---------------------------
// --=== Note that there is no Map handling ===--
// ----------------------------------------------

function test() {
    var resultJSON = toJSONRequest(testArr);
    console.log(str(resultJSON));
}
function toJSONRequest(arr) {
    var obj = objectize(arr);
    var o = handleDotsRunner(obj);
    var oo = handleBracketsRunner(o);
    return oo;
}

function objectize(arr) {
    var obj = {};
    arr.forEach(function (a) {
        obj[a.name] = a.value;
    });
    return obj;
}

//#############################################

function handleDotsRunner(obj) {
    var ret = {};
    for (key in obj) {
        var val = obj[key];
        handleDots(key, val, ret);
    }
    //console.log("ret: " + str(ret))
    return ret;
}

function handleDots(key, val, ret) {

    var iindex = key.indexOf(".");
    if (iindex > -1) {
        var keys = key.split(".");
        var key0 = keys[0];
        var key1 = key.substr(iindex + 1, key.length);
        //console.log("key0 " + key0)
        //console.log("key1 " + key1)
        if (ret[key0]) {
        } else {
            ret[key0] = {};
        }
        handleDots(key1, val, ret[key0]);
    } else {
        ret[key] = val;
    }
}

//#############################################

function handleBracketsRunner(obj) {
    var ret = {};
    for (key in obj) {
        var val = obj[key];
        handleBrackets(key, val, ret);
    }
    return ret;
}

function handleBrackets(key, variable, ret) {

    var val;
    if (typeof (variable) === 'object') {
        val = handleBracketsRunner(variable);
    } else {
        val = variable;
    }
    var iindex = key.indexOf("[");
    if (iindex > -1) {
        var keys = key.split("[");
        var key0 = keys[0];
        if (ret[key0]) {
        } else {
            ret[key0] = [];
        }
        ret[key0].push(val);
    } else {
        ret[key] = val;
    }
}
//#############################################

function str(obj) {
    return JSON.stringify(obj, null, 2);
}

var testArr = [
    {
        "name": "criteriaFilter.constraints[0].constraintType",
        "value": "EQUALS"
    },
    {
        "name": "criteriaFilter.constraints[0].field",
        "value": "qwe"
    },
    {
        "name": "criteriaFilter.constraints[0].value",
        "value": "qwe"
    },
    {
        "name": "criteriaFilter.constraints[1].constraintType",
        "value": "EQUALS"
    },
    {
        "name": "criteriaFilter.constraints[1].field",
        "value": "qwe"
    },
    {
        "name": "criteriaFilter.constraints[1].value",
        "value": "qwe"
    },
    {
        "name": "criteriaFilter.value",
        "value": "AND"
    },
    {
        "name": "page",
        "value": "0"
    },
    {
        "name": "pageSize",
        "value": "0"
    },
    {
        "name": "sort.field",
        "value": ""
    },
    {
        "name": "sort.sortOrder",
        "value": "ASCENDING"
    }
];
