import { adminGuard } from './../guards/admin-guard';
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
    
    //para vista del admin

    
    getAll(page : number, size : number, filters : SaleFilters = {}){
        let params = new HttpParams()
                    .set('page', page.toString())
                    .set('size', size.toString())
 
        if (filters.status) params = params.set('status', filters.status)
        if (filters.minDate) params = params.set('minDate', filters.minDate)
        if (filters.maxDate) params = params.set('maxDate', filters.maxDate)
        if (filters.minPrice !== undefined) params = params.set('minPrice', filters.minPrice.toString())
        if (filters.maxPrice !== undefined) params = params.set('maxPrice', filters.maxPrice.toString())
 
        return this.http.get<PageResponse<SaleResponse>>(this.url, { params })
    }
}
