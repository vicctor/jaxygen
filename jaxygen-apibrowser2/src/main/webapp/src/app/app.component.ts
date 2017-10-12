import {
  Component,
  OnInit,
  ViewEncapsulation
} from '@angular/core';


@Component({
  selector: 'app',
  encapsulation: ViewEncapsulation.None,
  styleUrls:['./app.style.scss'],
  templateUrl: './app.component.html'
})

export class AppComponent implements OnInit {
  public angularclassLogo = 'assets/img/angularclass-avatar.png';
  public name = 'Angular 2 Webpack Starter';
  public url = 'https://twitter.com/AngularClass';

  constructor() {}

  public ngOnInit() {
  }

}
