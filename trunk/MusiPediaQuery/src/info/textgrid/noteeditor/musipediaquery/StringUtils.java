package info.textgrid.noteeditor.musipediaquery;

public class StringUtils {

	  private StringUtils() {}

	  private static String [][] htmlEscape =
	     {{  "&lt;"     , "<" } ,  {  "&gt;"     , ">" } ,
	      {  "&amp;"    , "&" } ,  {  "&quot;"   , "\"" } ,
	      {  "&agrave;" , "a" } ,  {  "&Agrave;" , "A" } ,
	      {  "&aacute;" , "‡" } ,  {  "&Aacute;" , "ç" } ,
	      {  "&acirc;"  , "â" } ,  {  "&auml;"   , "Š" } ,
	      {  "&Auml;"   , "€" } ,  {  "&Acirc;"  , "Â" } ,
	      {  "&aring;"  , "å" } ,  {  "&Aring;"  , "Å" } ,
	      {  "&aelig;"  , "æ" } ,  {  "&AElig;"  , "Æ" } ,
	      {  "&ccedil;" , "ç" } ,  {  "&Ccedil;" , "Ç" } ,
	      {  "&eacute;" , "Ž" } ,  {  "&Eacute;" , "ƒ" } ,
	      {  "&egrave;" , "è" } ,  {  "&Egrave;" , "E" } ,
	      {  "&ecirc;"  , "e" } ,  {  "&Ecirc;"  , "E" } ,
	      {  "&euml;"   , "ë" } ,  {  "&Euml;"   , "Ë" } ,
	      {  "&iuml;"   , "ï" } ,  {  "&Iuml;"   , "Ï" } ,
	      {  "&iacute;" , "’" } ,  {  "&Iacute;" , "ê" } ,
	      {  "&oacute;" , "—" } ,  {  "&Oacute;" , "î" } ,
	      {  "&ocirc;"  , "ô" } ,  {  "&Ocirc;"  , "Ô" } ,
	      {  "&ouml;"   , "ö" } ,  {  "&Ouml;"   , "Ö" } ,
	      {  "&oslash;" , "ø" } ,  {  "&Oslash;" , "Ø" } ,
	      {  "&szlig;"  , "ß" } ,  {  "&ugrave;" , "ù" } ,
	      {  "&uacute;" , "œ" } ,  {  "&Uacute;" , "ò" } ,
	      {  "&Ugrave;" , "Ù" } ,  {  "&ucirc;"  , "u" } ,
	      {  "&Ucirc;"  , "U" } ,  {  "&uuml;"   , "Ÿ" } ,
	      {  "&Uuml;"   , "†" } ,  {  "&nbsp;"   , " " } ,
	      {  "&copy;"   , "\u00a9" } ,
	      {  "&reg;"    , "\u00ae" } ,
	      {  "&euro;"   , "\u20a0" }
	     };

	  public static final String unescapeHTML(String s, int start){
	     int i, j, k;

	     i = s.indexOf("&", start);
	     start = i + 1;
	     if (i > -1) {
	        j = s.indexOf(";" ,i);
	        /*
	           we don't want to start from the beginning
	           the next time, to handle the case of the &
	           thanks to Pieter Hertogh for the bug fix!
	        */
	        if (j > i) {
	           // ok this is not most optimized way to
	           // do it, a StringBuffer would be better,
	           // this is left as an exercise to the reader!
	           String temp = s.substring(i , j + 1);
	           // search in htmlEscape[][] if temp is there
	           k = 0;
	           while (k < htmlEscape.length) {
	             if (htmlEscape[k][0].equals(temp)) break;
	             else k++;
	           }
	           if (k < htmlEscape.length) {
	             s = s.substring(0 , i)
	                    + htmlEscape[k][1] + s.substring(j + 1);
	             return unescapeHTML(s, i); // recursive call
	           }
	         }
	     }
	     return s;
	  }
}