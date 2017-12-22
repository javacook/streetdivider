This library is abel to divide (German) a concatenated street names
consisting of the pure street name the house number and its affix
into its single parts. Examples:

Some streets contain a number as suffix or infix so that it is 
impossible to decide whether this number is a house number or 
a part of the street name itselfs. An example is "Straße 101".
This could be the street "Straße" with house number "101" but 
in reality this the number "101" is part of the street name 
(in Berlin). To make this decision unambiguous the street 
"Straße 101" was added to a list of "special streets". This list
can be found in the file "specialstreets.txt".

More Examples: 

input                | street           | house no | affix
-------------------- | ---------------- | -------- | -------
Gartenstr. 25a       | Gartenstr.       | 25       | a
Brückenstr. 12a-13c  | Brückenstr.      | 12       | a-13c
Straße 101 Nr. 12    | Straße 101       | 12       | null
In den 30 Morgen 34b | In den 30 Morgen | 34       | b
C 3 54               | C 3              | 54       | null



