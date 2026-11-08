import { SaleItem } from "./sale-item"

export interface SaleResponse {
    externalId : string
    date : string
    status : string
    totalPrice : number
    earnedPoints : number
    hasDiscount : boolean
    userName : string
    items : SaleItem[]
}
