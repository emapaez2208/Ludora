import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { SaleRequest } from '../models/sale-request';
import { SaleResponse } from '../models/sale-response';
import { PageResponse } from '../models/page-response';
import { SaleFilters } from '../models/sale-filters';

@Service()
export class SaleService {

    private readonly url = '/api/sales'
    private http = inject(HttpClient)

    createSale(saleRequest : SaleRequest){
        return this.http.post<SaleResponse>(this.url, saleRequest)
    }

    paySale(saleId : string){
        return this.http.post<string>(`${this.url}/pay/${saleId}`, {}, { responseType: 'text' })
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


    //para vista del admin (ahora)

    getAll(page : number = 0,
           size : number = 10,
           filters : SaleFilters = {}){
        let params = new HttpParams()
                    .set('page', page.toString())
                    .set('size', size.toString())

        const { minDate, maxDate, ...simple } = filters

        Object.entries(simple).forEach(([key, value]) => {
            if (value !== null && value !== undefined && value !== '') {
                params = params.set(key, String(value))
            }
        })

        if (minDate) params = params.set('minDate', `${minDate}T00:00:00`)
        if (maxDate) params = params.set('maxDate', `${maxDate}T23:59:59`)

        return this.http.get<PageResponse<SaleResponse>>(this.url, { params })
    }
}