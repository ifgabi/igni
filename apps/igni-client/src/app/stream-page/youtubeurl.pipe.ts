import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer, SafeResourceUrl, SafeUrl } from '@angular/platform-browser';

@Pipe({
  name: 'youtubeurl'
})
export class YoutubeurlPipe implements PipeTransform {

  constructor(private sanitizer: DomSanitizer)
  {
    return;
  }

  transform(value: string, ...args: any[]): SafeResourceUrl {
    return this.sanitizer.bypassSecurityTrustResourceUrl(value);
  }

}
