import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";

@Injectable({providedIn: 'root'})
export class ProfileService {
  private baseUrl = environment.apiUrl;


  constructor(private http: HttpClient) {
  }

  getProfile() {
    return this.http.get<UserProfile>(`${this.baseUrl}/api/profile`);
  }

  getProfiles() {
    return this.http.get<UserProfilesList>(`${this.baseUrl}/api/profiles`);
  }

  findProfiles(req: ProfilesRequest) {

    return this.http.get<UserProfilesList>(`${this.baseUrl}/api/profiles`, {params: {firstName:req.firstName, surname:req.surname}});
  }

}

export class UserProfilesList {
  users: UserProfile[]
}

export class UserProfile {
  id: number;
  firstName: string;
  surname: string;
  city: string;
  gender: string;
  birthDate: string;
  interests: string;
}

export class ProfilesRequest {
  firstName: string="";
  surname: string="";


}
