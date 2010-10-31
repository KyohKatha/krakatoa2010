BEGIN { i = 1; 
   info = "" 
   number = 0
   }
/@filename/  { info = "      \"" $2 "\", " }
/@comment/   { info = info  "\""  $0  "\", "}
/\/\/\#/     { number = i}
             { i++ }
END {  print info "\"" number  "\"," }             
             