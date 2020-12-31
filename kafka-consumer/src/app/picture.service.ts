import { Injectable } from '@angular/core';

declare var SockJS;
declare var Stomp;

@Injectable({
  providedIn: 'root'
})
export class PictureService {

  constructor() {
    this.initializeWebSocketConnection();
  }
  public stompClient;
  public pictures = [];
  initializeWebSocketConnection() {
    const serverUrl = 'http://localhost:8081/socket';
    const ws = new SockJS(serverUrl);
    this.stompClient = Stomp.over(ws);
    const that = this;
    // tslint:disable-next-line:only-arrow-functions
    this.stompClient.connect({}, function(frame) {
      that.stompClient.subscribe('/picture', (message) => {
        if (message.body) {
          that.pictures.push(message.body);
        }
      });
    });
  }
}
