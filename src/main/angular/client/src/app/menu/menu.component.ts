import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {LoginService} from "../login/login.service";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  constructor(private router: Router, private loginSerivce: LoginService) {
  }

  ngOnInit(): void {
  }

  logout() {
    this.loginSerivce.logout();
    this.router.navigateByUrl("/login");
  }

  goProfile() {
    this.router.navigateByUrl("/profiles");
  }

  goHome() {
    this.router.navigateByUrl("/");
  }
}
