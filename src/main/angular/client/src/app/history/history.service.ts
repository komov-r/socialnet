import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";

@Injectable({providedIn: 'root'})
export class HistoryService {
  private baseUrl = environment.apiUrl;


  constructor(private http: HttpClient) {
  }

  getHistory() {
    return this.http.get<HistoryItem[]>(`${this.baseUrl}/api/history`);
  }

  getProfileHistory(_id: Number) {

    return this.http.get<HistoryItem[]>(`${this.baseUrl}/api/history`, {params: {id: _id + ""}});
  }

}

export class HistoryItem {
  event: HistoryEvent;
}

export class HistoryEvent {
  userId: number;
  userDescription: string;
  objectId: number;
  objectDescription: string;
  objectType: string;
  eventType: string;
  eventDate: string;
}
