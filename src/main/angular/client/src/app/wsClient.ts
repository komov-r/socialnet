import {Injectable} from "@angular/core";
import * as Stomp from 'stompjs';
import {LoginService} from "./login/login.service";


@Injectable({providedIn: 'root'})
export class WsClient {
  stompClient: any;
  connected: boolean;

  subscriberReconnects = new Map();
  subscriptions = new Map();

  constructor(private authenticationService: LoginService) {
    let ws = new WebSocket("ws://" + location.hostname + ":8080/notifications");
    this.stompClient = Stomp.over(ws);

    console.log("token " + this.authenticationService.currentUserValue.token);
  }

  _connect() {
    if (this.connected) {
      return Promise.resolve(this.stompClient);
    } else {
      const self = this;
      return new Promise((res, rej) => {
        console.log("Connecting to ws");

        self.stompClient.connect(
          {"X-AUTH-TOKEN": this.authenticationService.currentUserValue.token},
          frame => {
            self.connected = true;
            res(self.stompClient);
          },
          this.errorCallBack);
      });
    }
  }

  _subscribe(endpoint, callback) {
    const self = this;
    const subscrFunc = (cl: any) => {
      const sub = cl.subscribe(endpoint, frame => {
        callback(JSON.parse(frame.body));
      });
      self.subscriptions.set(endpoint, sub);
    }
    this.subscriberReconnects.set(endpoint, subscrFunc);
    this._connect().then(subscrFunc);
  }

  subscriberHistory(historyCallback) {
    this._subscribe("/user/queue/history", historyCallback);
  }

  unSubscriberHistory() {
    const sub = this.subscriptions.get("/user/queue/history");
    if (sub) {
      sub.unsubscribe();
    }
  }

  subscriberMsg(msgCallback) {
    this._subscribe("/user/queue/message", msgCallback);
  }

  _disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }

  // on error, schedule a reconnection attempt
  errorCallBack(error) {
    console.log("errorCallBack -> " + error);
    this.connected = false;
    let self = this;
    setTimeout(() => {
      self._connect().then(cl => {
        self.subscriberReconnects.forEach((k, v) => v(cl));
      });
    }, 5000);
  }

}
