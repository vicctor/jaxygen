import { Injectable, Inject } from '@angular/core';
import { RequestHandler } from '../../app.service';

@Injectable()
export class HomeService {
    constructor(private requestHandler: RequestHandler) { 
    }

    getTileOperators() {
        return this.requestHandler.sendRequest('/api-broker/org.jaxygen.apibroker.services.ServersRepostoryService/getServers', {projectId:3});
    }
   
}