import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {environment} from '../../environments/environment';


@Injectable({providedIn: 'root'})
export class LoginService {
  private currentUserSubject: BehaviorSubject<Token>;
  private baseUrl = environment.apiUrl;

  public currentUser: Observable<Token>;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<Token>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): Token {
    return this.currentUserSubject.value;
  }

  login(username, password) {
    let url = `${this.baseUrl}/api/login`;

    return this.http.post<any>(url, {"login": username, "password": password})
      .pipe(map(user => {
        // store user details and token in local storage to keep user logged in between page refreshes
        // this is not secure, I know
        localStorage.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
        return user;
      }));
  }

  logout() {
    // remove user from local storage and set current user to null
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }
}

export class Token {
  id: number;
  token: string
}
