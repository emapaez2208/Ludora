import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { ClientRequest } from '../models/client-request';
import { ClientResponse } from '../models/client-response';
import { Developer } from '../models/developer';
import { AdminResponse } from '../models/admin-response';
import { PageResponse } from '../models/page-response';

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

    
     //-------------------------------------------------------------------------------------------//


    clientProfile(){
        return this.http.get<ClientResponse>(`${this.url}/clients/profile`)
    }

    developerProfile(){
        return this.http.get<Developer>(`${this.url}/developers/profile`)
    }

    adminProfile(){
        return this.http.get<AdminResponse>(`${this.url}/admins/profile`)
    }
    

    // client user
    addUser(clientRequest : ClientRequest){
        return this.http.post<ClientRequest>(this.url,clientRequest)
    }

}