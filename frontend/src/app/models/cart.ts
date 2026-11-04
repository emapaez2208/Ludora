import { ClientResponse } from "./client-response";

export interface Cart {
  client: ClientResponse;    //ya que me lo devuelve el endpoint, se podria utilisar para mostrar algo
  games: CartGame[];         // lista de juegos
  totalPrice: number;        
}

// este seria juego
// en lista carrito

export interface CartGame{
  externalId: string;
  name: string;
  frontPage: string;
  price: number;
  discountedPrice: number | null;
  company: string;
}




