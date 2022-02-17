import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Pipe({
  name: 'twitchurl'
})
export class TwitchurlPipe implements PipeTransform {

  constructor(private sanitizer: DomSanitizer)
  {
    return;
  }

  transform(value: string, ...args: any[]): SafeResourceUrl {
    return this.sanitizer.bypassSecurityTrustResourceUrl(value);
  }

}
