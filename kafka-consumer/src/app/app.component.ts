import { Component } from '@angular/core';
import { MessageService } from './message.service';
import { PictureService } from './picture.service';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  
  customMessage = "test";
  
  constructor(private messageService: MessageService,
    private pictureService: PictureService,
    private sanitizer: DomSanitizer){}
    
    
  getMessages() {
    return this.messageService.msg;
  }

  getPictures() {
    return this.pictureService.pictures;
  }

  sanitize(imageAsString: string) {
    return this.sanitizer.bypassSecurityTrustResourceUrl('data:image/png;base64,'+imageAsString)
  }

  sendMessage(){
    this.messageService.addMessage(this.customMessage);
  }
}

