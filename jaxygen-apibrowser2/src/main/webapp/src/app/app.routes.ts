import { Routes } from '@angular/router';
import { HomeComponent } from './core/home';

export const ROUTES: Routes = [
  { path: '',      component: HomeComponent },
  { path: 'home',  component: HomeComponent },
  { path: '**',    component: HomeComponent },
];
