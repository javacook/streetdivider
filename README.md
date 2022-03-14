# Streetdivider 

This library is able to divide a concatenated (German) street name
consisting of the pure street name, the house number, and its affix,
into its single parts.

Some streets contain a number as a suffix or infix themselves so that 
it is impossible to decide whether this number is a house number or 
belongs to the street name. An example is "Straße 101" in Berlin.
At first glance this seems to be a street "Straße" with 
house number "101", but in fact the number "101" is part of 
the street name. To make this decision unambiguous the street 
"Straße 101" is added to a list of "special streets". This list
can be found in the file [specialstreets.txt](src/main/resources/specialstreets.txt) and can/must be updated
periodically. 

More Examples: 

| input                | street           | house no | affix |
|----------------------|------------------|----------|-------|
| Gartenstr. 25a       | Gartenstr.       | 25       | a     |
| Brückenstr. 12a-13c  | Brückenstr.      | 12       | a-13c |
| Straße 101 Nr. 12    | Straße 101       | 12       |       |
| C 3 54               | C 3              | 54       |       |
| In den 30 Morgen 34b | In den 30 Morgen | 34       | b     |

In the examples above you can see the very strange street names of the
city Manheim. The inner of the city in organized as a matrix with names
like A4 or C3.

## Usage
Kotlin:
```
val streetDivider = StreetDivider()
println(streetDivider.parse("Gartenstr. 25a"))
``` 
Java:
```
var streetDivider = new StreetDivider();
System.out.println(streetDivider.parse("Gartenstr. 25a"));
```
Output:
```
-> Location(street=Gartenstr., houseNumber=25, houseNoAffix=a)
```

### Maven

Repository: [Maven Central](https://repo.maven.apache.org/maven2/de/kotlincook/textmining/streetdivider/)
```
<dependency>
    <groupId>de.kotlincook.textmining</groupId>
    <artifactId>streetdivider</artifactId>
    <version>1.9</version>
</dependency>
```
### Gradle 
```
implementation group: 'de.kotlincook.textmining', name: 'streetdivider', version: '1.9'
```

## Security 
This tool has runtime dependencies to 
* `org.jetbrains.kotlin:kotlin-stdlib:1.6.10`
* `org jetbrains.kotlin:kotlin-stdlib-common:1.6.10`
* `org jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.10`
* `org jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10`
* `org.jetbrains: annotations:13.0`

Vulnerabilities were not known on 3/13/2022.