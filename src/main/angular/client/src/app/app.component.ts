import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {BehaviorSubject} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  chatSelectedListener:BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(public route: ActivatedRoute,private router: Router) {
  }

  public renderMenu():boolean {
    let b = this.router.isActive("/login",false) || this.router.isActive("/reg",false) ;
    return !b;
  }

}
