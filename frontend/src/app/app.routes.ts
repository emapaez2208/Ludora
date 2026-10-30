import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { UserList } from './pages/user-list/user-list';
import { Profile } from './pages/profile/profile';
import { Catalog } from './pages/catalog/catalog';
import { Know } from './pages/know/know';
import { FormGame } from './pages/form-game/form-game';
import { GameDetail } from './pages/game-detail/game-detail';
import { authGuard } from './guards/auth-guard';
import { adminGuard } from './guards/admin-guard';
import { developerGuard } from './guards/developer-guard';
import { loginGuard } from './guards/login-guard';

export const routes: Routes = [
    {path: '/home', component: Home},
    {path: 'login', component: Login, canActivate: [loginGuard]},
    {path: 'register', component: Register, canActivate: [loginGuard]},
    {path: 'user-list', component: UserList, canActivate: [adminGuard]},
    {path: 'profile', component: Profile, canActivate: [authGuard]},
    {path: 'catalog', component: Catalog},
    {path: 'about', component: Know},
    {path: 'catalog/:id', component: GameDetail},
    {path: '', redirectTo: '/home', pathMatch: 'full'},
    {path: 'games/new', component: FormGame, canActivate: [developerGuard]},
    {path: 'games/edit/:id', component: FormGame, canActivate: [developerGuard]},
    { path: '**', redirectTo: '' }
];
