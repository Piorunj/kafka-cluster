import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
declare var SockJS;
declare var Stomp;
@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private http : HttpClient) {
    this.initializeWebSocketConnection();
  }
  public stompClient;
  public msg = [];
  initializeWebSocketConnection() {
    const serverUrl = 'http://localhost:8081/socket';
    const ws = new SockJS(serverUrl);
    this.stompClient = Stomp.over(ws);
    const that = this;
    // tslint:disable-next-line:only-arrow-functions
    this.stompClient.connect({}, function(frame) {
      that.stompClient.subscribe('/message', (message) => {
        if (message.body) {
          that.msg.push(message.body);
        }
      });
    });
  }

  addMessage(message: string) {
    console.log("send message");
    return this.http.post("http://localhost:8081/message/"+message, null).subscribe();
  }

  addImage(image: File){
    const uploadData = new FormData();
    uploadData.append('file', image);
    return this.http.post('http://localhost:8081/message/picture', uploadData)
      .subscribe();
  }

}
