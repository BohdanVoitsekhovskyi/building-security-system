import { Routes } from '@angular/router';
import { WelcomeComponent } from './pages/welcome/welcome.component';
import { HomeComponent } from './pages/home/home.component';
import { TestComponent } from './pages/main/test/test.component';
import { CreateComponent } from './pages/main/create/create.component';
import { LogComponent } from './pages/main/log/log.component';
import { LoginComponent } from './pages/account/login/login.component';
import { SigninComponent } from './pages/account/signin/signin.component';

export const routes: Routes = [
  {
    path: '',
    component: WelcomeComponent,
  },
  {
    path: 'home',
    component: HomeComponent,
  },
  {
    path: 'test',
    component: TestComponent,
  },
  {
    path: 'create',
    component: CreateComponent,
  },
  {
    path: 'log',
    component: LogComponent,
  },

  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'signin',
    component: SigninComponent,
  },
];
