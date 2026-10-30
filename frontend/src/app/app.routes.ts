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
import { ProfileEdit } from './pages/profile-edit/profile-edit';

export const routes: Routes = [
    {path: '', component: Home},
    {path: 'login', component: Login, canActivate: [loginGuard]},
    {path: 'register', component: Register, canActivate: [loginGuard]},
    {path: 'user-list', component: UserList, canActivate: [adminGuard]},
    {path: 'profile', component: Profile, canActivate: [authGuard]},
    {path: 'profile/edit', component: ProfileEdit, canActivate: []},
    {path: 'catalog', component: Catalog},
    {path: 'about', component: Know},
    {path: 'catalog/:id', component: GameDetail},
    {path: 'games/new', component: FormGame, canActivate: [developerGuard]},
    {path: 'games/edit/:id', component: FormGame, canActivate: [developerGuard]},
    { path: '**', redirectTo: '' }
];
