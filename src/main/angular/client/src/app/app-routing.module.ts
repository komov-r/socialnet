import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {ProfileComponent} from "./profile/profile.component";
import {UsersComponent} from "./users/users.component";
import {RegisterComponent} from "./register/register.component";
import {AuthGuard} from "./helpers/auth.guard";


const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'reg', component: RegisterComponent},
  {path: 'profiles', component: UsersComponent, canActivate: [AuthGuard]},
  {path: '', component: ProfileComponent, canActivate: [AuthGuard]},
];


export const appRoutingModule = RouterModule.forRoot(routes, {
  scrollPositionRestoration: 'enabled',
  anchorScrolling: 'enabled',
  useHash: true
});

