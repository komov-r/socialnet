import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Component({
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  private baseUrl = environment.apiUrl;
  regForm: FormGroup;
  loading: boolean = false;

  constructor(private formBuilder: FormBuilder,
              private http: HttpClient,
              private router: Router) {
  }

  ngOnInit() {
    this.regForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      firstName: ['', Validators.required],
      surname: ['', Validators.required],
      city: ['', Validators.required],
      gender: ['', Validators.required],
      birthDate: ['', Validators.required],
      interests: []
    });
  }

  onSubmit() {


    if (this.regForm.invalid) {
      return;
    }

    this.loading = true;
    this.http.post(`${this.baseUrl}/api/profile`, this.regForm.getRawValue()).subscribe(() => {
        this.loading = false;
        this.router.navigateByUrl("/login");
      },
      (e: HttpErrorResponse) => {

        if (e.status == 409) {
          // @ts-ignore
          window.alert("User name is taken");
        } else {
          // @ts-ignore
          console.log(e)
          // @ts-ignore
          window.alert((e.error.errors == null || e.error.errors[0]==null )? e.message : e.error.errors[0]);
        }

        this.loading = false
      });

  }

  gotToLogin() {
    this.router.navigateByUrl("/login");
  }
}
