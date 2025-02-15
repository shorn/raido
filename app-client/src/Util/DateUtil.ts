// https://stackoverflow.com/a/14509447/924597
import { forceError } from "Error/ErrorUtil";

export function parseDateFromEpoch(millis: string): Date {
  const epoch = parseInt(millis, 10);
  return new Date(epoch);
}

/** expected to be passed to the JSON.parse() method to "fix" Dates. */
export function dateTimeReviver(key : string, value: any) {
  if (typeof value === 'string') {
    if( value.length === 24 && value[4] === '-' && value[value.length-1] === 'Z' ){
      // looks, sounds and smells like a duck
      try {
        const dateValue = parseServerDate(value);
        if( !dateValue ){
          console.warn("ignored un-parsable server date", value);
          return value;
        }
        else {
          return dateValue;
        }
      }
      catch( err ){
        console.warn("ignored un-parsable server date",
          forceError(err).message, value );
        return value;
      }
    }
  }
  return value;
}

const shortTimeFormatOptions: Intl.DateTimeFormatOptions = {
  hour12: false, hour: "2-digit", minute: "2-digit"
};

export function parseJwtDate(date: string|number|undefined): Date | undefined{
  if( !date ){
    return undefined;
  }

  if( !Number.isInteger(date) ){
    console.debug("date was not an integer", date);
    return undefined;
  }
  
  return new Date(Number(date) * 1000);
}

/**
 * server datetime are expected to look like: 2022-04-13T01:07:47.154Z
 * I vaguely recall Safari having some non-standard date parsing issue?
 * Need to test.
 */
export function parseServerDate(date: string): Date{
  return new Date(date);
}

/**
 * Client Config.buildDate value comes from unix command, looks like:
 * `2023-02-01T06:31:13+00:00`
 * Slightly different from the "server date" documented above.
 * Maybe this is the one with Safari issues, dunno.
 */
export function parseClientDate(date: string): Date{
  return new Date(date);
}


export function formatLocalDateAsIsoShortDateTime(date?: Date){
  if( !date ){
    return ""
  }

  return formatLocalDateAsIso(date) + " " + formatShortTime(date);
}

export function formatLocalDateAsFileSafeIsoShortDateTime(date?: Date){
  if( !date ){
    return ""
  }

  return formatLocalDateAsIso(date) + "-" + 
    formatShortTime(date).replace(":", "-");
}

export function formatShortTime(
  date?:Date,
  locale:string|undefined = undefined
):string{
  if( !date ){
    return ""
  }
  return date.toLocaleTimeString(locale, shortTimeFormatOptions);
}

/* if you just call date.toISOString(), you get the day in UTC, i.e.
without timezone applied; so could be yesterday, today or tomorrow, depending
on your TZ and when the data is for. 
 */
export function formatLocalDateAsIso(date?: Date){
  if( !date ){
    return "";
  }
  
  const month = Number(date.getMonth()+1).toString();
  const day = Number(date.getDate()).toString();
  
  return date.getFullYear() + "-" + 
    month.padStart(2, "0") + "-" + 
    day.padStart(2, "0");
}

/**
 * return a new Date() object, if undefined will return new Date() + days
 */
export function addDays(d: Date | undefined, days: number): Date{
  if( !d ){
    const result = new Date();
    result.setDate(result.getDate() + days);
    return result;
  }
  
  const result = new Date();
  result.setDate(d.getDate() + days);
  return result;
}

export function cloneDate(d?: Date): Date|undefined{
  if( !d ){
    return undefined;
  }
  
  return new Date(d.getTime());
}

export function isValidDate(d?: Date): boolean {
  if( !d ){
    return false;
  }
  return !!d.getDate();
}