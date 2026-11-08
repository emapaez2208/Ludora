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
import { SolicitudList } from './pages/solicitud-list/solicitud-list';
import { developerGuard } from './guards/developer-guard';
import { loginGuard } from './guards/login-guard';
import { ProfileEdit } from './pages/profile-edit/profile-edit';
import { Cart } from './pages/cart/cart';
import { clientGuard } from './guards/client-guard';
import { SalesDetails } from './pages/sales-details/sales-details';
import { Payment } from './pages/payment/payment';
import { Sales } from './pages/sales/sales';
import { FormAdmin } from './pages/form-admin/form-admin';
import { Library } from './pages/library/library';
import { clientOrDeveloperGuard } from './guards/client-or-developer-guard';
import { GameManagement } from './pages/game-management/game-management';
import { SalesList } from './pages/sales-list/sales-list';

export const routes: Routes = [
    {path: '', component: Home},
    {path: 'login', component: Login, canActivate: [loginGuard]},
    {path: 'register', component: Register, canActivate: [loginGuard]},
    {path: 'registerAdmin' , component: FormAdmin, canActivate: [authGuard]},
    {path: 'profile', component: Profile, canActivate: [authGuard]},
    {path: 'profile/edit', component: ProfileEdit, canActivate: []},
    {path: 'catalog', component: Catalog},
    {path: 'application', component: SolicitudList , canActivate:[adminGuard]},
    {path: 'about', component: Know},
    {path: 'catalog/:id', component: GameDetail},
    {path: 'games/new', component: FormGame, canActivate: [developerGuard]},
    {path: 'games/edit/:id', component: FormGame, canActivate: [developerGuard]},
    {path: 'sales', component: SalesList, canActivate: [adminGuard]},
    {path: 'sales/:id', component: SalesDetails, canActivate: [clientGuard]},
    {path: 'cart', component: Cart,canActivate: [clientGuard]},
    {path: 'library', component: Library, canActivate: [clientOrDeveloperGuard]},
    {
        path: 'management',
        canActivate: [adminGuard],
        children: [
            { path: '', pathMatch: 'full', redirectTo: 'games' },
            { path: 'games', component: GameManagement },
            { path: 'users', component: UserList },
        ],
    },
    {path: 'pay/:type', component: Payment},
    { path: '**', redirectTo: ''}
];
