import {
  Component,
  OnInit
} from '@angular/core';

import { HomeService } from './home.service';


@Component({
  selector: 'home',
  providers: [HomeService],
  styleUrls: ['./home.component.scss'],
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {

  private data:string = '';
  constructor(private homeService: HomeService) {

  }

  public ngOnInit() {
    this.homeService.getTileOperators()
    .subscribe(
    res => {
      this.data = res;
    },
    error => {
      console.log('error');
    });
   
  }

}
