#!/bin/bash

#  This file is part of the LDAP Persistence API (LPA).
#  
#  Copyright Trenton D. Adams <lpa at trentonadams daught ca>
#  
#  LPA is free software: you can redistribute it and/or modify it under
#  the terms of the GNU Lesser General Public License as published by the
#  Free Software Foundation, either version 3 of the License, or (at your
#  option) any later version.
#  
#  LPA is distributed in the hope that it will be useful, but WITHOUT ANY
#  WARRANTY; without even the implied warranty of MERCHANTABILITY or
#  FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
#  License for more details.
#  
#  You should have received a copy of the GNU Lesser General Public 
#  License along with LPA.  If not, see <http://www.gnu.org/licenses/>.
#  
#  See the COPYING file for more information.


function addLicense()
{
  FILE=$1
  grep 'GNU Lesser General Public' $FILE > /dev/null
  if [ $? -ne 0 ]; then
    cat /tmp/license-header.txt $FILE > $FILE.tmp; 
    mv $FILE.tmp $FILE; 
    printf "added  - $FILE\n";
#  else
#    printf "exists - $FILE\n";
  fi;
}

# java files
printf "/**\n" > /tmp/license-header.txt
cat license-header.txt | sed -e 's/^/ \* /' >> /tmp/license-header.txt
printf " */\n" >> /tmp/license-header.txt;
for FILE in $(find src/main/java -name '*.java') $(find src/test/java -name '*.java'); do 
  addLicense $FILE
done

# ldif and property files
cat license-header.txt | sed -e 's/^/#  /' > /tmp/license-header.txt
for FILE in $(find src/test/resources -name '*.properties') $(find src/test/resources -name '*.ldif'); do 
  addLicense $FILE
done

# html or xml files
printf "<!--\n" > /tmp/license-header.txt
cat license-header.txt | sed -e 's/</\&lt;/g' -e 's/>/\&gt;/g' >> /tmp/license-header.txt
printf "  -->\n" >> /tmp/license-header.txt;
for FILE in $(find src/ -name '*.html') $(find src/ -name '*.xml') $(find ./ -name 'pom.xml'); do 
  addLicense $FILE
done
