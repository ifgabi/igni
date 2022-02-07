import { EventEmitter, Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpHeaders, HttpResponse } from '@angular/common/http';

export interface LoginResponse{
  username: string;
  userData: UserDto;
}

export interface SignupResponse{
  userData:UserDto;
  usernameErrorFlags:Array<string>;
  passwordErrorFlags:Array<string>;
  emailErrorFlags:Array<string>;
}

export interface UserDto{
  id:number;
  username:string;
  email:string;
  roles:Array<RoleDto>;
  channels:Array<ChannelDto>;
}

export interface RoleDto{
  id:number;
  name:string;
  privileges:Array<PrivilegeDto>;
}

export interface PrivilegeDto{
  id:number;
  name:string;
}

export interface ChannelDto{
  id:string;
  channelUsers:Array<ChanneluserDto>;
}

export interface ChanneluserDto{
  id:number;
  username:string;
}

@Injectable({
  providedIn: 'root'
})
@Injectable()
export class AccountService {

  //Event when user logged is attempted
  loginEvent:EventEmitter<boolean> = new EventEmitter();

  //Event when logged out is attempted
  logoutEvent:EventEmitter<boolean> = new EventEmitter();

  //Event when authentication is pooled
  authenticatedUpdateEvent:EventEmitter<boolean> = new EventEmitter();

  //Event when server is unresponsive
  serverUnresponsiveEvent:EventEmitter<boolean> = new EventEmitter();

  signupUsernameErrorEvent:EventEmitter<string> = new EventEmitter();
  signupPasswordErrorEvent:EventEmitter<string> = new EventEmitter();
  signupEmailErrorEvent:EventEmitter<string> = new EventEmitter();

  //TODO make use of currentuser
  currentUser: UserDto | null;
  // eslint-disable-next-line @typescript-eslint/no-inferrable-types
  authenticated: boolean = false;

  username: string | null | undefined;

  pollingInterval:number | null | undefined;

  constructor(private http: HttpClient) {
    this.currentUser = null;

    this.pollingInterval = window.setInterval(this.isAuthenticatedInterval.bind(this), 5000);
    this.isAuthenticatedInterval();
  }

  async isAuthenticatedInterval():Promise<void>
  {
    await this.authenticationUpdate();
    //TODO emit invalid session
  }


  //TODO Data/LoginDTO Data/LogoutDTO
  //TODO NGRX stores of User interface instead of this.username:string
  authenticate(username:string, password:string) {
    this.http.post<HttpResponse<LoginResponse>>("http://localhost:8080" + '/login', {
        "username": username,
        "password": password
    }, {
      headers: new HttpHeaders(),
      withCredentials: true,
      responseType: 'json',
      observe: 'response'
    } ).subscribe( (resp: HttpResponse<object>) => {
      const lr: LoginResponse = resp.body as LoginResponse;
      const logged: boolean = lr.userData !== null;
      if(logged)
      {
          this.authenticated = true;
          this.username = lr.username;
          this.currentUser = lr.userData;

          this.loginEvent.emit(true);
      }
      if(!logged)
      {
        this.authenticated = false;
        this.username = null;
        this.currentUser = null;

        this.loginEvent.emit(false);
      }
    });
  }


  unauthenticate()
  {
    this.http.post("http://localhost:8080" + "/logout", {},
    {
      headers: new HttpHeaders(),
      withCredentials: true,
      responseType: 'json',
      observe: 'response'
    } ).subscribe( buffer => {

      const status: number = buffer['status'];
      if(status === 200)
      {
        this.authenticated = false;
        this.logoutEvent.emit(true);
      }
      else
      {
        this.logoutEvent.emit(false);
      }
    });

  }

  async authenticationUpdate()
  {
    let buffer: HttpResponse<object> | undefined;
    try{
      buffer = await this.http.get("http://localhost:8080" + "/checksession",
      {
        headers: new HttpHeaders(),
        withCredentials: true,
        responseType: 'json',
        observe: 'response'
      } ).toPromise();
    }
    catch(error)
    {
      this.serverUnresponsiveEvent.emit(true);
      return;
    }

    const statusCode: number = buffer?.status?.valueOf() ?? -1;
    if(statusCode !== 200)
    {
      this.serverUnresponsiveEvent.emit(true);
      return;
    }

    const body: any = buffer?.body ?? null;

    const authed: boolean = body?.alive ?? false;
    this.authenticated = authed;
    this.authenticatedUpdateEvent.emit(authed);
  }

  //TODO signUpDTO
  signUp(username: string, email: string, password: string)
  {
    this.http.post<HttpResponse<SignupResponse>>("http://localhost:8080" + "/signup", {
        "username": username,
        "password": password,
        "email": email
    }, {
      headers: new HttpHeaders(),
      withCredentials: true,
      responseType: 'json',
      observe: 'response'
    } ).subscribe(
      (resp: HttpResponse<object>) => {
        const bodyresp: SignupResponse = resp.body as SignupResponse;
        if(bodyresp.usernameErrorFlags.length == 0 && bodyresp.passwordErrorFlags.length == 0 && bodyresp.emailErrorFlags.length == 0)
        {
          this.authenticated = true;
          this.username = bodyresp.userData.username;

          this.loginEvent.emit(true);
          return;
        }

        if(bodyresp.usernameErrorFlags.indexOf("INVALID_CHARACTERS") != -1)
          this.signupUsernameErrorEvent.emit("Invalid characters inside username. Please use only alphabetical and digit characters.");

        if(bodyresp.usernameErrorFlags.indexOf("TOOLONG") != -1)
          this.signupUsernameErrorEvent.emit("Username is too long, use a maximum of 16 characters.")

        if(bodyresp.usernameErrorFlags.indexOf("TOOSHORT") != -1)
          this.signupUsernameErrorEvent.emit("Username is too short, use a minimum of 5 characters.")

        if(bodyresp.usernameErrorFlags.indexOf("ALREADYEXISTS") != -1)
          this.signupUsernameErrorEvent.emit("Username already exists, please use a different username.")

        if(bodyresp.passwordErrorFlags.indexOf("INVALID") != -1)
          this.signupPasswordErrorEvent.emit("A valid password consists of 7 characters or more, a minimum of 1 special character and a minimum of 1 capital letter.")

        if(bodyresp.passwordErrorFlags.indexOf("CONTAINS_SPACES") != -1)
          this.signupPasswordErrorEvent.emit("A password cannot contain spaces.")

        if(bodyresp.emailErrorFlags.indexOf("INVALID") != -1)
          this.signupEmailErrorEvent.emit("Invalid email.")

        if(bodyresp.emailErrorFlags.indexOf("ALREADYEXISTS") != -1)
          this.signupEmailErrorEvent.emit("Email is already signed up, please use a different email or consider recovering password for that account.")
    });
  }
}
