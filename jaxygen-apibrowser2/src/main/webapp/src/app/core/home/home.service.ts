import { Injectable, Inject } from '@angular/core';
import { RequestHandler } from '../../app.service';

@Injectable()
export class HomeService {
    constructor(private requestHandler: RequestHandler) { 
    }

    getModuleServices(moduleName) {
        return this.requestHandler.sendRequest('/api-broker/ServiceBrowser/getModuleServices', {moduleName:moduleName});
    }
   
}