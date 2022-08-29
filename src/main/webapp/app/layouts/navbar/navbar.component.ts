import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';

import { VERSION } from 'app/app.constants';
import { LANGUAGES } from 'app/config/language.constants';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { EntityNavbarItems } from 'app/entities/entity-navbar-items';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;
  entitiesNavbarItems: any[] = [];

  constructor(
    private loginService: LoginService,
    private translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private router: Router
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.entitiesNavbarItems = EntityNavbarItems;
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
  }

  changeLanguage(languageKey: string): void {
    this.sessionStorageService.store('locale', languageKey);
    this.translateService.use(languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  login(): void {
    this.router.navigate(['/login']);
  }
  pregledObjekata(): void {
    this.router.navigate(['/pregled-objekata']);
  }
  pregledVaspitaca(): void {
    this.router.navigate(['/pregled-vaspitaca']);
  }
  pregledVazecegDnevnika(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account) {
        if (account.authorities[0] === 'ROLE_VASPITAC') {
          localStorage.setItem('username', account.login);
        }
      }
    });

    this.router.navigate(['/prica']);
  }
  pregledDece(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account) {
        if (account.authorities[0] === 'ROLE_VASPITAC') {
          localStorage.setItem('vaspitac', account.login);
        }
      }
    });

    this.router.navigate(['/dete']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  prikaziIzostanke(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account) {
        if (account.authorities[0] === 'ROLE_VASPITAC') {
          localStorage.setItem('username', account.login);
        }
      }
    });
    this.router.navigate(['/ne-dolasci']);
  }

  showOlderDneviks(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account) {
        if (account.authorities[0] === 'ROLE_VASPITAC') {
          localStorage.setItem('username', account.login);
          localStorage.setItem('olderDnevniks', 'true');
        }
      }
    });

    this.router.navigate(['/dnevnik']);
  }
}
