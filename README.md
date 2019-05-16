# Safari Form Values Decryptor for JAVA

## Introduction

This program allows you to decrypt the content of the file _/Users/${user}/Library/Safari/Form Values_.
The output file will be a **Binary Plist file** which can be opened with any program supporting such format.

## How to use it

First thing is to compile the program:
```console
user@domain:~$ mvn clean compile assembly:single
```

Next you will have to execute the program like this:
```console
user@domain:~$ java -jar program.jar PASSWORD INPUT_FILE OUTPUT_FILE
```

with:  
**PASSWORD**: the 32 bytes hexadecimal representation Safari's autofill database found in the keychain for the account "Safari" and Service "PersonalFormsAutoFillDatabase"  
**INPUT_FILE**: the path to the _Form Values_
**OUTPUT_FILE: the path to the output file