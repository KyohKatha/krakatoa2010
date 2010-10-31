BEGIN { found = 0 }
" Program" ~ $0 { found = 1 }
END {
   print FILENAME;
   if ( !found ) print "Missing Program" + FILENAME
 }