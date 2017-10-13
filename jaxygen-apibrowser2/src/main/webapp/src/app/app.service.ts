import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions, URLSearchParams } from '@angular/http';

import 'rxjs/add/operator/map';

@Injectable()
export class RequestHandler {
    constructor(private _http: Http) { }


    serialize(obj: any) {
        let params: URLSearchParams = new URLSearchParams();
    
    
        for (var key in obj) {
            if (obj.hasOwnProperty(key)) {
                var element = obj[key];
    
                params.set(key, element);
            }
        }
    
        return params;
    }

    sendRequest(url: string, data: any = {}) {

        let body = this.serialize(data);

        let headers = new Headers({ 'Content-Type': 'application/x-www-form-urlencoded', 'Accept': 'application/json', "charset": "utf-8" });


        return this._http.post(url, body, new RequestOptions({ headers: headers, method: "post" })).map((response: Response) => {
            let res = response.json().dto.responseObject;
            return res;
        });



    }
}