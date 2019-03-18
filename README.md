# TimeTracker

TimeTracker is a Time registration tool. You can use it for all time relevant things that needs to be tracked.

[DATABASE SETUP]
1. Table "mitarbeiter"
   + Column 1: id (PK) - int
   + Column 2: forname - varchar
   + Column 3: lastname - varchar
   + Column 4: computername - varchar
   + Column 5: authorization - int
2. Table "zeiterfassungen"
   + Column 1: id (PK) - int
   + Column 2: userid - varchar
   + Column 3: projectid - varchart
   + Column 4: start - time
   + Column 5: end - time
   + Column 6: pause - decimal
   + Column 7: description - varchar
   + Column 8: date - date
