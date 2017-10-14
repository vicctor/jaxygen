import {
  Component,
  OnInit
} from '@angular/core';

import { HomeService } from './home.service';

import {PanelMenuModule,MenuItem} from 'primeng/primeng';

@Component({
  selector: 'home',
  providers: [HomeService],
  styleUrls: ['./home.component.scss'],
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {

  private selectedServer;
  private items: MenuItem[];
  constructor(private homeService: HomeService) {

  }

  public ngOnInit() {
    this.items = [
      {
          label: 'File',
          icon: 'fa-file-o',
          items: [{
                  label: 'New', 
                  icon: 'fa-plus',
                  items: [
                      {label: 'Project'},
                      {label: 'Other'},
                  ]
              },
              {label: 'Open'},
              {label: 'Quit'}
          ]
      },
      {
          label: 'Edit',
          icon: 'fa-edit',
          items: [
              {label: 'Undo', icon: 'fa-mail-forward'},
              {label: 'Redo', icon: 'fa-mail-reply'}
          ]
      },
      {
          label: 'Help',
          icon: 'fa-question',
          items: [
              {
                  label: 'Contents'
              },
              {
                  label: 'Search', 
                  icon: 'fa-search', 
                  items: [
                      {
                          label: 'Text', 
                          items: [
                              {
                                  label: 'Workspace'
                              }
                          ]
                      },
                      {
                          label: 'File'
                      }
              ]}
          ]
      },
      {
          label: 'Actions',
          icon: 'fa-gear',
          items: [
              {
                  label: 'Edit',
                  icon: 'fa-refresh',
                  items: [
                      {label: 'Save', icon: 'fa-save'},
                      {label: 'Update', icon: 'fa-save'},
                  ]
              },
              {
                  label: 'Other',
                  icon: 'fa-phone',
                  items: [
                      {label: 'Delete', icon: 'fa-minus'}
                  ]
              }
          ]
      }
  ];
  
    this.changeServer('JaxygenAPIBroker');
  }

  public changeServer(serverName) {

    this.homeService.getModuleServices(serverName)
      .subscribe(
      res => {
        this.selectedServer = res.services;
        this.selectedServer.name = serverName;
      },
      error => {
        console.log('error');
      });
  }

}
