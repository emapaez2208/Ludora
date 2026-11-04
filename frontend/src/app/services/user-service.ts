import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { ClientRequest } from '../models/client-request';
import { ClientResponse } from '../models/client-response';
import { Developer } from '../models/developer';
import { AdminResponse } from '../models/admin-response';
import { ClientUpdate } from '../models/client-update';
import { DeveloperUpdate } from '../models/developer-update';
import { AdminUpdate } from '../models/admin-update';
import { DeveloperRequest } from '../models/developer-request';
import { PageResponse } from '../models/page-response';
import { ChangePassword } from '../models/change-password';
import { ChangeEmail } from '../models/change-email';


@Service()
export class UserService {

    private readonly url = '/api'
    http = inject(HttpClient)

    getClients(page: number, size: number) {
        return this.http.get<PageResponse<ClientResponse>>(`${this.url}/clients`, { params: { page, size } })
    }

    getDevelopers(page: number, size: number) {
        return this.http.get<PageResponse<Developer>>(`${this.url}/developers`, { params: { page, size } })
    }

    blockAccount(externalId: string) {
        return this.http.patch<void>(`${this.url}/admins/${externalId}/block`, null)
    }

    unblockAccount(externalId: string) {
        return this.http.patch<void>(`${this.url}/admins/${externalId}/unblock`, null)
    }

    
    //---------------------------------- GET PROFILE -------------------------------------//


    clientProfile(){
        return this.http.get<ClientResponse>(`${this.url}/clients/profile`)
    }

    developerProfile(){
        return this.http.get<Developer>(`${this.url}/developers/profile`)
    }

    adminProfile(){
        return this.http.get<AdminResponse>(`${this.url}/admins/profile`)
    }
    
    //---------------------------------- UPDATE PROFILE -------------------------------------//

    clientUpdate(id : string, client : ClientUpdate){
        return this.http.put<ClientResponse>(`${this.url}/clients/${id}`, client)
    }

    developerUpdate(id : string, developer : DeveloperUpdate){
        return this.http.put<Developer>(`${this.url}/developers/${id}`, developer)
    }

    adminUpdate(id : string, admin : AdminUpdate){
        return this.http.put<AdminResponse>(`${this.url}/admins/${id}`, admin)
    }

    clientChangePassword(password : ChangePassword){
        return this.http.put<void>(`${this.url}/clients/profile/changePassword`, password)
    }

    developerChangePassword(password : ChangePassword){
        return this.http.put<void>(`${this.url}/developers/profile/changePassword`, password)
    }

    adminChangePassword(password : ChangePassword){
        return this.http.put<void>(`${this.url}/admins/profile/changePassword`, password)
    }

    clientChangeEmail(email : ChangeEmail){
        return this.http.put<void>(`${this.url}/clients/profile/changeEmail`, email)
    }

    developerChangeEmail(email : ChangeEmail){
        return this.http.put<void>(`${this.url}/developers/profile/changeEmail`, email)
    }

    adminChangeEmail(email : ChangeEmail){
        return this.http.put<void>(`${this.url}/admins/profile/changeEmail`, email)
    }

    // --------------------------------------------------------------------------------------//

    // client user
    addUser(clientRequest : ClientRequest){
        return this.http.post<ClientResponse>(`${this.url}/clients`,clientRequest)
    }

    // develop user

    addDevelop ( developerRequest : DeveloperRequest){
        return this.http.post<Developer>(`${this.url}/developers`, developerRequest)
    }

}