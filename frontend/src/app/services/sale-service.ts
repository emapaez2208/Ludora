import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { SaleRequest } from '../models/sale-request';
import { SaleResponse } from '../models/sale-response';
import { PageResponse } from '../models/page-response';

@Service()
export class SaleService {

    private readonly url = '/api/sales'
    private http = inject(HttpClient)

    createSale(saleRequest : SaleRequest){
        return this.http.post<SaleResponse>(this.url, saleRequest)
    }

    paySale(saleId : string){
        return this.http.post<string>(`${this.url}/${saleId}`, undefined)
    }

    getSale(saleId : string){
        return this.http.get<SaleResponse>(`${this.url}/${saleId}`)
    }

    getSalesByClient(page : number = 0,
                     size : number = 30, 
                     clientId : string){
        let params = new HttpParams()
                    .set('page', page.toString())
                    .set('size', size.toString())
        
        return this.http.get<PageResponse<SaleResponse>>(`${this.url}/client/${clientId}`, { params })
    }
}
